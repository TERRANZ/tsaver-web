package ru.terra.twochsaver.web.engine;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.server.config.Config;
import ru.terra.twochsaver.web.db.controller.ImgJpaController;
import ru.terra.twochsaver.web.db.controller.ThrJpaController;
import ru.terra.twochsaver.web.db.entity.Img;
import ru.terra.twochsaver.web.db.entity.Thr;
import ru.terra.twochsaver.web.engine.dto.SyncDTO;
import ru.terra.twochsaver.web.engine.dto.TwochFile;
import ru.terra.twochsaver.web.engine.dto.TwochThread;
import ru.terra.twochsaver.web.timer.TsaverTimerManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Date: 21.01.15
 * Time: 19:35
 */
public class DownloadEngine {

    private static DownloadEngine instance = new DownloadEngine();
    private ExecutorService threadPool = Executors.newFixedThreadPool(20);
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<String, CountDownLatch> countdowns = new HashMap<>();
    private Map<String, Integer> counts = new HashMap<>();
    private ThrJpaController thrJpaController = new ThrJpaController();
    private ImgJpaController imgJpaController = new ImgJpaController();
    private static SimpleDateFormat sdf = new SimpleDateFormat("d.M.y H:m");

    private DownloadEngine() {
        List<File> files = new ArrayList<>();

        addTree(new File("download"), files);
        files.stream().filter(File::isDirectory).forEach(file -> {
            counts.put(file.getName(), file.list().length);
            countdowns.put(file.getName(), new CountDownLatch(0));
        });
    }

    static void addTree(File file, Collection<File> all) {
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                all.add(child);
                addTree(child, all);
            }
        }
    }

    public static DownloadEngine getInstance() {
        return instance;
    }

    public synchronized void start(final String url, final boolean force) {
        logger.info("Starting: " + url);
        Future<?> submit = threadPool.submit((Runnable) () -> {

            Thr thr = thrJpaController.findByUrl(url);
            if (thr == null) {
                thr = new Thr();
                thr.setUpdated(new Date());
                thr.setAdded(new Date());
                thr.setChecked(0);
                thr.setFinished(0);
                thr.setCount(0);
                thr.setImgList(new ArrayList<Img>());
                thr.setUrl(url);
                thrJpaController.create(thr);
            } else {
                if (thr.getChecked() > 30)
                    thr.setFinished(1);
                try {
                    thrJpaController.update(thr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!force)
                if (thr.getFinished() == 1) {
                    TsaverTimerManager.getInstance().removeThreadJob(url);
                    return;
                }

            final Integer thread = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".")));
            String resUrl = url.substring(0, url.indexOf("/res"));
            final String board = resUrl.substring(resUrl.lastIndexOf("/") + 1);
            URLConnection conn = null;
            try {
                conn = new URL("https://2ch.hk/makaba/mobile.fcgi?task=get_thread&board=" + board + "&thread=" + thread + "&num=" + thread).openConnection();
            } catch (IOException e) {
                logger.error("Unable to connect to server", e);
            }
            conn.setConnectTimeout(10000);
            ObjectMapper mapper = new ObjectMapper();
            TwochThread[] readedThread = new TwochThread[0];
            boolean error = false;
            try {
                readedThread = mapper.readValue(conn.getInputStream(), TwochThread[].class);
            } catch (IOException e) {
                error = true;
                TsaverTimerManager.getInstance().removeThreadJob(url);
                thr.setFinished(1);
                try {
                    thrJpaController.update(thr);
                } catch (Exception e1) {
                    logger.error("Unable to update thread", e1);
                }
                logger.error("Unable to read json", e);
            }
            if (!error) {
                Integer imagesCount = 0;
                for (TwochThread twochThread : readedThread)
                    imagesCount += twochThread.getFiles().size();

                logger.info("Images count: " + imagesCount);

                if (thr.getCount() == imagesCount)
                    thr.setChecked(thr.getChecked() + 1);
                else
                    thr.setChecked(0);

                thr.setCount(imagesCount);
                try {
                    thrJpaController.update(thr);
                } catch (Exception e) {
                    logger.error("Unable to update thread", e);
                }

                counts.put(board + thread, imagesCount);
                countdowns.put(board + thread, new CountDownLatch(imagesCount));

                final String folderName = "download/" + board + thread;
                new File(folderName).mkdirs();
                new Thread(() -> addBtSyncFolder(folderName)).start();

                final String finalResUrl = resUrl + "/";
                for (TwochThread twochThread : readedThread)
                    for (final TwochFile file : twochThread.getFiles()) {
                        final String finalImageUrl = finalResUrl + file.getPath();
                        final Thr finalThr = thr;
                        threadPool.submit((Runnable) () -> {
                            try {
                                if (imgJpaController.findImg(finalImageUrl) == null) {
                                    downloadImage(folderName, finalImageUrl);
                                    Img img = new Img();
                                    img.setThrId(finalThr);
                                    img.setUrl(finalImageUrl);
                                    img.setMd5hash(file.getMd5());
                                    imgJpaController.create(img);
                                }

                                CountDownLatch countDownLatch = getCountDown(board + thread);
                                if (countDownLatch != null)
                                    countDownLatch.countDown();
                            } catch (Exception e) {
                                logger.error("Unable to download image " + finalImageUrl, e);
                            }
                        });
                    }
            }
        });
    }

    private void addBtSyncFolder(String folder) {
        try {
            String jarDir = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            String currentDir = new File(jarDir).getParent();

            String name = Config.getConfig().getValue("btsync.name", "");
            String password = Config.getConfig().getValue("btsync.pass", "");
            String host = Config.getConfig().getValue("btsync.host", "http://xn--80aafhfrpg0adapheyc1nya.xn--p1ai:8888");
            String authString = name + ":" + password;
            String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes(Charset.defaultCharset()));
            logger.debug("Encoded auth string = " + authStringEnc);

            URLConnection btsConn = new URL(host + "/api?method=add_folder&dir=" + currentDir + "/" + folder).openConnection();
            btsConn.setRequestProperty("Authorization", "Basic " + authStringEnc);
            btsConn.setConnectTimeout(10000);
            ObjectMapper mapper = new ObjectMapper();
            SyncDTO res = mapper.readValue(btsConn.getInputStream(), SyncDTO.class);
            logger.debug("Sync for folder " + currentDir + "/" + folder + " reported: " + res.error + " message: " + res.message);
        } catch (IOException e) {
            logger.error("Unable to add folder to sync", e);
        }
    }

    private CountDownLatch getCountDown(String bt) {
        synchronized (countdowns) {
            return countdowns.get(bt);
        }
    }

    private void downloadImage(String folder, String url) throws IOException {
        for (int i = 0; i <= 2; i++) {
            URL imageUrl = new URL(url);
            Path path = Paths.get(folder + url.substring(url.lastIndexOf("/")));
            if (!path.toFile().exists())
                Files.copy(imageUrl.openStream(), path, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Downloaded: " + url);
            break;
        }
    }

    public synchronized List<Stat> getStat() {
        return thrJpaController.findThrEntities().stream().map(thr -> new Stat(thr.getUrl(), thr.getCount(), thr.getCount() - imgJpaController.getImagesCountForThread(thr), thr.getFinished(), thr.getChecked(), sdf.format(thr.getAdded()), sdf.format(thr.getUpdated()))).collect(Collectors.toList());
    }

    public Show getShow(String bt) {
        Thr thr = thrJpaController.findByUrl(bt);
        if (thr == null)
            return null;

        return new Show(thr.getUrl(), thr.getCount(), thr.getCount() - imgJpaController.getImagesCountForThread(thr), thr.getFinished(), thr.getChecked(), sdf.format(thr.getAdded()), sdf.format(thr.getUpdated()),
                imgJpaController.getImagesForThread(thr).stream().map(Img::getUrl).collect(Collectors.toList()));
    }
}
