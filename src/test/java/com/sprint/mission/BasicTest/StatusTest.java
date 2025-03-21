package com.sprint.mission.BasicTest;

import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.ReadStatusRepository;
import com.sprint.mission.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class StatusTest {

    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    private EntityManager em;
    @Autowired
    private UserService userService;

    @Test
    void testReadStatus() {

        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            UserDtoForCreate userDtoForCreate = new UserDtoForCreate("테스트 유저 " + i, "testPassword" + i, "테스트 이메일" + i);
            userList.add(userService.create(userDtoForCreate, null));
        }
        em.flush();
        em.clear();

        List<User> findedUser = userService.findAll();
        for (User user : findedUser) {
            System.out.println("user.getStatus() = " + user.getStatus());
        }

        List<ReadStatus> readStatusList = readStatusRepository.findAll();
    }
}
