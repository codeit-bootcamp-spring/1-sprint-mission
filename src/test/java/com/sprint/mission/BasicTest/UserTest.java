package com.sprint.mission.BasicTest;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.repository.UserStatusRepository;
import com.sprint.mission.service.UserService;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserStatusService userStatusService;

    @Autowired
    private EntityManager em;
    @Autowired
    private UserStatusRepository userStatusRepository;

    //@BeforeEach
    void createTest(){
        for (int i = 0; i < 20; i++) {
            UserDtoForCreate createDto = new UserDtoForCreate("테스트 유저 " + i, "testPassword" + i, "테스트 이메일" + i);
            userService.create(createDto, null);
        }
    }

    @Test
    void setUpTest(){
        List<User> users = userRepository.findAll();
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(20);
        users.forEach(user -> {
            UserStatus findUserstatus = userStatusService.findById(user.getStatus().getId());
            assertThat(findUserstatus.getUser()).isEqualTo(user);
        });
    }

    @Test
    void cascadeUserAndUserStatusTest(){
        List<User> userList = userService.findAll();
        List<UserStatus> userStatusList = userStatusService.findAll();
        assertThat(userList.size()).isEqualTo(20);
        assertThat(userList.size()).isEqualTo(userStatusList.size());
    }

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



    @Test
    void find(){
        List<User> all = userRepository.findAll();
        assertThat(all).isNotEmpty();
        assertThat(all.size()).isEqualTo(20);
    }

    @Test
    void update(){
        User beforeUpdateUser = new User("업데이트 전 이름", "업데이트 전 비밀번호", "업데이트 전 이메일");
        userRepository.save(beforeUpdateUser);
        em.flush();
        em.clear();

        User updatingUser = userRepository.findById(beforeUpdateUser.getId()).orElseThrow();
        

    }
}
