package ru.terra.twochsaver.web.controller;

import com.sun.jersey.api.core.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.ZipUtil;
import ru.terra.server.config.Config;
import ru.terra.server.controller.AbstractResource;
import ru.terra.twochsaver.web.constants.FileConstants;
import ru.terra.twochsaver.web.engine.DownloadEngine;
import ru.terra.twochsaver.web.engine.Stat;
import ru.terra.twochsaver.web.timer.TsaverTimerManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Date: 21.01.15
 * Time: 19:30
 */
@Path("/")
public class TwochController extends AbstractResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Path("start")
    @GET
    public Response startDownload(@Context HttpContext hc, @QueryParam("url") String url) {
        try {
            TsaverTimerManager.getInstance().addUpdateThreadJob(url);
            return Response.ok(url).build();
        } catch (Exception e) {
            logger.error("Unable to start download", e);
            return Response.serverError().build();
        }
    }

    @Path("stat")
    @GET
    public List<Stat> getStat() {
        return DownloadEngine.getInstance().getStat();
    }

    @Path("pack")
    @GET
    public Response pack(@Context HttpContext hc, @QueryParam("bt") String bt) throws URISyntaxException {
        String ret = FileConstants.EXPORT_DIR + "/" + bt + ".zip";
        ZipUtil.pack(new File(FileConstants.DOWNLOAD_DIR + "/" + bt), new File(ret), 0);
        URI redirect = URI.create(Config.getConfig().getValue("host", "http://192.168.1.3/") + ret);
        logger.info("Redirecting to " + redirect);
        return Response.temporaryRedirect(redirect).build();
    }

    @Path("refresh")
    @GET
    public Response refresh(@QueryParam("bt") String bt) throws Exception {
        DownloadEngine.getInstance().start(bt, true);
        URI redirect = URI.create(Config.getConfig().getValue("host", "http://192.168.1.3/") + "/main.html#status");
        return Response.temporaryRedirect(redirect).build();
    }
}
