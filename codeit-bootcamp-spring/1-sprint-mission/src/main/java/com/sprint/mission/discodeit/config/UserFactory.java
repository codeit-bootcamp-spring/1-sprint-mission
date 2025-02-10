package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.application.auth.PasswordEncoder;
import com.sprint.mission.discodeit.application.service.interfaces.UserService;
import com.sprint.mission.discodeit.application.service.user.JCFUserService;
import com.sprint.mission.discodeit.application.service.user.converter.UserConverter;
import com.sprint.mission.discodeit.repository.user.InMemoryUserRepository;
import com.sprint.mission.discodeit.repository.user.interfaces.UserRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class UserFactory {

    private static final UserRepository USER_REPOSITORY = createUserRepository();
    private static final UserService USER_SERVICE = new JCFUserService(USER_REPOSITORY, new UserConverter(), new PasswordEncoder());

    public static UserService getUserService() {
        return USER_SERVICE;
    }

    public static UserRepository createUserRepository() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("application.yaml"));
        } catch (IOException exception) {
            System.out.println("설정 파일 로드 실패, 기본값(memory) 사용");
        }
        String repositoryType = props.getProperty("repository.type", "memory");

        return switch(repositoryType) {
            default -> new InMemoryUserRepository();
        };
    }
}
