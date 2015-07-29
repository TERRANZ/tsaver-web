package ru.terra.twochsaver.web.timer;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Date: 31.03.15
 * Time: 13:04
 */
public class TsaverTimerManager {
    private static TsaverTimerManager instance = new TsaverTimerManager();
    private StdSchedulerFactory sf;
    private Scheduler sched;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private TsaverTimerManager() {
        sf = new StdSchedulerFactory();
        try {
            sched = sf.getScheduler();
            sched.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public static TsaverTimerManager getInstance() {
        return instance;
    }

    public void addUpdateThreadJob(String url) {
        Boolean found = false;
        try {
            for (JobKey jd : sched.getJobKeys(GroupMatcher.anyJobGroup())) {
                if (jd.getName().equalsIgnoreCase(url)) {
                    found = true;
//                    logger.info("Job for board " + board + " and thread " + thread + " already exists");
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        if (!found)
            try {
                logger.info("Job for url " + url);
                JobDetail job = JobBuilder.newJob(UpdateThreadJob.class)
                        .withIdentity(url)
                        .usingJobData("url", url)
                        .build();
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(url + "_trg", "group2")
                        .startNow()
                        .withSchedule(
                                SimpleScheduleBuilder.simpleSchedule()
                                        .withIntervalInMinutes(5)
                                        .repeatForever()
                        )
                        .build();
                sched.scheduleJob(job, trigger);
            } catch (SchedulerException se) {
                se.printStackTrace();
            }
    }

    public void removeThreadJob(String url) {
        try {
            JobKey jobKey = null;
            for (JobKey jd : sched.getJobKeys(GroupMatcher.anyJobGroup())) {
                if (jd.getName().equalsIgnoreCase(url)) {
                    jobKey = jd;
//                    logger.info("Job for board " + board + " and thread " + thread + " already exists");
                }
            }
            if (jobKey != null)
                sched.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public Set<JobKey> getCurrentJobs() {
        try {
            return sched.getJobKeys(GroupMatcher.anyJobGroup());
        } catch (SchedulerException e) {
            logger.error("Unable to get jobs", e);
        }
        return new HashSet<>();
    }
}
