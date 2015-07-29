package ru.terra.twochsaver.web.timer;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import ru.terra.twochsaver.web.engine.DownloadEngine;

/**
 * Date: 31.03.15
 * Time: 13:10
 */
public class UpdateThreadJob implements Job {
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        DownloadEngine.getInstance().start(jec.getMergedJobDataMap().getString("url"), false);
    }
}
