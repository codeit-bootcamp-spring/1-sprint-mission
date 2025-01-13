package com.sprint.mission.discodeit.testdummy;

import com.sprint.mission.discodeit.db.user.UserRepository;
import com.sprint.mission.discodeit.db.user.UserRepositoryImpl;
import com.sprint.mission.discodeit.entity.user.entity.User;
import java.util.ArrayList;
import java.util.List;

public class TestDummyFactory {

    private static final List<User> users = new ArrayList<>(List.of(
            User.createFrom("홍길동"),
            User.createFrom("김길동"),
            User.createFrom("이길동"),
            User.createFrom("박깅동")
    ));


    public static UserRepository getUserRepository() {
        var userRepository = UserRepositoryImpl.getInstance();
        users.forEach(userRepository::save);
        return userRepository;
    }
}
