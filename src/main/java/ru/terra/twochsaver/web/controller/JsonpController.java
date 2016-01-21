package ru.terra.twochsaver.web.controller;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.json.JSONWithPadding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.server.config.Config;
import ru.terra.server.controller.AbstractResource;
import ru.terra.server.dto.SimpleDataDTO;
import ru.terra.twochsaver.web.engine.DownloadEngine;
import ru.terra.twochsaver.web.engine.Stat;
import ru.terra.twochsaver.web.timer.TsaverTimerManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 27.07.15
 * Time: 13:34
 */
@Path("/jsonp")
@Produces({"application/x-javascript", "application/json", "application/xml"})
public class JsonpController extends AbstractResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Path("start")
    @GET
    public JSONWithPadding startDownload(@QueryParam("callback") String callback, @Context HttpContext hc, @QueryParam("url") String url) {
        try {
            TsaverTimerManager.getInstance().addUpdateThreadJob(url);
            return new JSONWithPadding(new SimpleDataDTO<>("ok"), callback);
        } catch (Exception e) {
            logger.error("Unable to start download", e);
            return new JSONWithPadding(new SimpleDataDTO<>(Arrays.toString(e.getStackTrace())), callback);
        }
    }

    @Path("stat")
    @GET
    public JSONWithPadding getStat(@QueryParam("callback") String callback) {
        return new JSONWithPadding(new GenericEntity<List<Stat>>(DownloadEngine.getInstance().getStat()) {
        }, callback);
    }

    @Path("refresh")
    @GET
    public JSONWithPadding refresh(@QueryParam("callback") String callback, @QueryParam("bt") String bt) throws Exception {
        DownloadEngine.getInstance().start(bt, true);
        URI redirect = URI.create(Config.getConfig().getValue("host", "http://192.168.1.3/") + "/main.html#status");
        return new JSONWithPadding(new SimpleDataDTO<>("ok"), callback);
    }

    @Path("show")
    @GET
    public JSONWithPadding getShow(@QueryParam("callback") String callback, @QueryParam("bt") String bt) {
        return new JSONWithPadding(DownloadEngine.getInstance().getShow(bt), callback);
    }
}
