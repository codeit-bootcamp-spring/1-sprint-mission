package com.sprint.mission.discodeit.testdummy;

import com.sprint.mission.discodeit.db.user.UserRepositoryImpl;
import com.sprint.mission.discodeit.db.user.ifs.UserRepository;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserName;
import java.util.ArrayList;
import java.util.List;

public class TestDummyFactory {

    private static final List<User> users = new ArrayList<>(List.of(
            new User(new UserName("홍길동")),
            new User(new UserName("김길동")),
            new User(new UserName("이길동")),
            new User(new UserName("박길동"))
    ));


    public static UserRepository getUserRepository() {
        var userRepository = new UserRepositoryImpl();
        users.forEach(userRepository::save);
        return userRepository;
    }
}
