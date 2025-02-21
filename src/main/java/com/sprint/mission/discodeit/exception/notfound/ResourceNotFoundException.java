package com.sprint.mission.discodeit.exception.notfound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceNotFoundException extends RuntimeException {
    public static final Logger loggeer = LoggerFactory.getLogger(ResourceNotFoundException.class);

    public ResourceNotFoundException(String message) {
        super(message);
        loggeer.error("Resource not found: {}", message);
    }
}
