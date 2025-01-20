package com.sprint.mission.discodeit.enums;

import org.apache.commons.lang3.StringUtils;

public enum UserType {
    COMMON, // 일반 유저 (Default)
    BOT,    // 봇
    STAFF;   // 관리자

    // TODO: 근데 왜 static일까........?
    public static UserType fromString(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("UserType은 null이 될 수 없습니다.");
        }
        try {
            return UserType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 UserType: " + value);
        }
    }
}
