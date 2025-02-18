package com.sprint.mission.discodeit.enums;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import io.micrometer.common.util.StringUtils;

public enum ChannelType {
    PUBLIC,
    PRIVATE;

    public static ChannelType fromString(String value) {
        if (StringUtils.isBlank(value)) {
            throw new CustomException(ErrorCode.CHANNEL_TYPE_NOT_FOUND);
        }
        try {
            return ChannelType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.CHANNEL_TYPE_NOT_FOUND);
        }
    }
}
