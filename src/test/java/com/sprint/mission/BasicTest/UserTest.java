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

    @BeforeEach
    void createTest(){
        for (int i = 0; i < 20; i++) {
            UserDtoForCreate createDto = new UserDtoForCreate("테스트 유저 " + i, "testPassword" + i, "테스트 이메일" + i);
            userService.create(createDto, null);
        }
    }

    @Test
    void setUpTest(){
        List<User> users = userRepository.findAll();
        Assertions.assertThat(users).isNotEmpty();
        Assertions.assertThat(users.size()).isEqualTo(20);
        users.forEach(user -> {
            UserStatus findUserstatus = userStatusService.findById(user.getStatus().getId());
            Assertions.assertThat(findUserstatus.getUser()).isEqualTo(user);
        });
    }

    @Test
    void cascadeUserAndUserStatusTest(){
        List<User> userList = userService.findAll();
        List<UserStatus> userStatusList = userStatusService.findAll();
        Assertions.assertThat(userList.size()).isEqualTo(20);
        Assertions.assertThat(userList.size()).isEqualTo(userStatusList.size());
    }

    @Test
    void find(){
        List<User> all = userRepository.findAll();
        Assertions.assertThat(all).isNotEmpty();
        Assertions.assertThat(all.size()).isEqualTo(20);
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
