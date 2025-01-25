package com.sprint.mission.discodeit.logger.repository;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.logger.LogFormatter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.UUID;
import java.util.logging.*;

import static com.sprint.mission.discodeit.constant.ExitStatus.DIR_CREATION_ERROR;

public class RepositoryLogger {
    private static final Logger LOGGER   = Logger.getLogger(RepositoryLogger.class.getName());
    private static final String LOG_DIR  = "log";
    private static final String LOG_NAME = "repository";
    private static final String LOG_EXT  = ".log";
    private static final String LOG_PATH = LOG_DIR + File.separator + LOG_NAME + LOG_EXT;
    private FileHandler fileHandler;

    static {
        File logDirectory = new File(LOG_DIR);
        if (!logDirectory.exists())
            if (!logDirectory.mkdir()) {
                try {
                    throw new FileSystemException(LOG_DIR);
                } catch (FileSystemException e) {
                    System.err.println("Failed to mkdir: " + e.getMessage());
                    System.exit(DIR_CREATION_ERROR.ordinal());
                }
            }
    }
    private RepositoryLogger() {
        try {
            fileHandler = new FileHandler(LOG_PATH, true);
        } catch (SecurityException | IOException e) {
            severe(e);
        }

        Formatter logFormatter = new LogFormatter();
        fileHandler.setFormatter(logFormatter);

        fileHandler.setLevel(Level.WARNING);

        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(fileHandler);
    }

    private static final class InstanceHolder {
        private final static RepositoryLogger INSTANCE = new RepositoryLogger();
    }
    public static RepositoryLogger getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void warning(String message, UUID id) {
        LOGGER.warning(message + ", ID: " + id.toString());
    }
    public void warning(ErrorCode errorCode, String message, UUID id) {
        LOGGER.warning("[" + errorCode.toString() + "] " + message + ", ID: " + id.toString());
    }
    public void severe(Exception exception) {
        StackTraceElement root = exception.getStackTrace()[0];
        LOGGER.severe(exception.getClass().getName() +
                " occurred in '" + root.getClassName() + "'" +
                " with '" + root.getMethodName() + "'");
    }
}