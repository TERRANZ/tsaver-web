package ru.terra.twochsaver.web;

import ru.terra.server.ServerBoot;
import ru.terra.twochsaver.web.db.controller.ThrJpaController;
import ru.terra.twochsaver.web.db.entity.Thr;
import ru.terra.twochsaver.web.timer.TsaverTimerManager;

import java.io.IOException;

/**
 * Date: 22.07.14
 * Time: 17:06
 */
public class Main {
    public static void main(String... args) throws IOException {
        new Thread(() -> {
            try {
                for (Thr thr : new ThrJpaController().findUnfinished())
                    TsaverTimerManager.getInstance().addUpdateThreadJob(thr.getUrl());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new ServerBoot().start();
    }
}
