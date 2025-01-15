package com.sprint.mission.discodeit.log.service;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.*;

public class ServiceLogger {
    private static final Logger LOGGER   = Logger.getLogger(ServiceLogger.class.getName());
    private static final String LOG_PATH = "service.log";
    private FileHandler    fileHandler;
    private ConsoleHandler consoleHandler;

    private ServiceLogger() {
        try {
            fileHandler = new FileHandler(LOG_PATH, true);
            consoleHandler = new ConsoleHandler();
        } catch (SecurityException | IOException e) {
            severe(e, this.getClass());
        }

        Formatter serviceFormatter = new ServiceFormatter();
        fileHandler.setFormatter(serviceFormatter);
        fileHandler.setLevel(Level.WARNING);

        consoleHandler.setFormatter(serviceFormatter);
        consoleHandler.setLevel(Level.WARNING);

        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(fileHandler);
        LOGGER.addHandler(consoleHandler);
    }

    private static final class InstanceHolder {
        private final static ServiceLogger INSTANCE = new ServiceLogger();
    }
    public static ServiceLogger getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void warning(String message, UUID id) {
        LOGGER.warning(message + ", ID: " + id.toString());
    }
    public void warning(ErrorCode errorCode, String message, UUID id) {
        LOGGER.warning("[" + errorCode.toString() + "] " + message + ", ID: " + id.toString());
    }
    public void severe(Exception exception, Class<?> occuredClass) {
        LOGGER.severe(exception.getClass().getName() + " occurred in " + occuredClass.getName());
    }
}
