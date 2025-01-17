package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.service.ServiceFactory;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class Validation {
    //검증을 위한 메소드들으 모아놓기 위한 만든 클래스
    private static final UserService userservice= ServiceFactory.getUserService();

    public static void validateUserExists(UUID userId) {
        if (!userservice.getUserById(userId).isPresent()){
            throw new IllegalArgumentException("User not found");
        }
    }
}
