package com.sprint.mission.BasicTest;

import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepoTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void userEqualsHashCodeTest(){
        System.out.println("============================userEqualsHashCodeTest===========================");
        //UserDtoForCreate createDto1 = new UserDtoForCreate("test 유저 1", "test 패스워드 1", "test 이메일 1");
        User createdUser1 = userRepository.save(new User("test 유저 1", "test 패스워드 1", "test 이메일 1"));
        User user = new User(createdUser1.getUsername(), createdUser1.getPassword(), createdUser1.getEmail());
        user.setId(createdUser1.getId());
        assertThat(user).isEqualTo(createdUser1);
        // Equals, HashCode를 정의하지 않으면 false
        System.out.println("=======================================================");
    }
}
