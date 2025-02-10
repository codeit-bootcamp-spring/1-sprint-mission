package com.sprint.mission.discodeit.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
    @Override
    public String getHead(Handler h) {
        return System.lineSeparator() + System.lineSeparator() +
                "Format is \"" + "yyyy-MM-dd HH:mm " + "[LEVEL] " + "[ErrorCode] " + "content " + "Id\"" +
                System.lineSeparator() + System.lineSeparator();
    }

    @Override
    public String format(LogRecord record) {
        return getNowDate(record.getMillis()) +
                " [" + record.getLevel() + "] " +
                record.getMessage() +
                System.lineSeparator();
    }

    private String getNowDate(long nowMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(new Date(nowMillis));
    }

    @Override
    public String getTail(Handler h) {
        return System.lineSeparator() +
                "Logging ended on " + getNowDate(System.currentTimeMillis());
    }
}