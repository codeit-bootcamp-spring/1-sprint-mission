package com.sprint.mission.discodeit.exception.duplication;

import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DuplicateResourceException extends RuntimeException {
    public static final Logger loggeer = LoggerFactory.getLogger(ResourceNotFoundException.class);

    public DuplicateResourceException(String message) {
        super(message);
        loggeer.error("Resource not found: {}", message);
    }
}
