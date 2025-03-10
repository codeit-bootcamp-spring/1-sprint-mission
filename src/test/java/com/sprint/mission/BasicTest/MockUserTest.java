package com.sprint.mission.BasicTest;


import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MockUserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private EntityManager em;

    @Test
    void userEqualsHashCodeTest(){
        System.out.println("============================userEqualsHashCodeTest===========================");
        UserDtoForCreate createDto1 = new UserDtoForCreate("test 유저 1", "test 패스워드 1", "test 이메일 1");
        User createdUser1 = userService.create(createDto1, null);
        User user = new User(createdUser1.getUsername(), createdUser1.getPassword(), createdUser1.getEmail());
        user.setId(createdUser1.getId());
        em.flush();
        em.clear();
        assertThat(user).isEqualTo(createdUser1);
        // Equals, HashCode를 정의하지 않으면 false
        System.out.println("=======================================================");
    }

}
