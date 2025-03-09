package com.sprint.mission.BasicTest;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
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
    private EntityManager em;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            userList.add(new User("테스트 유저 " + i, "testPassword" + i, "테스트 이메일" + i));
        }
        userRepository.saveAll(userList);
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
