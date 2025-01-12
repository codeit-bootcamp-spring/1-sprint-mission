package com.sprint.mission.discodeit.validation.common;

import com.sprint.mission.discodeit.exception.InvalidFormatException;

import java.util.UUID;

public class BaseEntityValidator {
    private final IdValidator   idValidator;
    private final TimeValidator timeValidator;

    private BaseEntityValidator() {
        idValidator   = IdValidator.getInstance();
        timeValidator = TimeValidator.getInstance();
    }

    private static final class InstanceHolder {
        private static final BaseEntityValidator INSTANCE = new BaseEntityValidator();
    }

    public static BaseEntityValidator getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void validateIdFormat(UUID id) throws InvalidFormatException {
        idValidator.validateIdFormat(id);
    }
    public void validateTimeFormat(Long time) throws InvalidFormatException {
        timeValidator.validateTimeFormat(time);
    }
}
