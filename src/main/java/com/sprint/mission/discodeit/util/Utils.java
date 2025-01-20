package com.sprint.mission.discodeit.util;

import java.util.Date;

public class Utils {

    public static Date transTime(long time) { //유닉스 타임을 DATE변환
        Date date = new Date(time);;
        return date;
    }
}
