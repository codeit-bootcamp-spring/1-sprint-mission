package com.sprint.mission.discodeit.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class MyLogger {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    public static void log(Object obj){
        String time = LocalTime.now().format(formatter);
        System.out.printf("[%s] %s\n",time,obj);
    }
}
