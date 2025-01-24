package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ServiceFactory;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.UUID;

public class Validation {
    //검증을 위한 메소드들으 모아놓기 위한 만든 클래스
    private static final UserService userservice= ServiceFactory.getUserService_f();

    public static void validateUserExists(UUID userId) {
        if (!userservice.getUserById(userId).isPresent()){
            throw new IllegalArgumentException("User not found");
        }
    }
}
