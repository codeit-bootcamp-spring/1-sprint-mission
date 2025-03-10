package com.sprint.mission.BasicTest;

import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.repository.ReadStatusRepository;
import com.sprint.mission.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class StatusTest {

    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    private UserService userService;

    @Test
    void testReadStatus() {

        for (int i = 0; i < 3; i++) {
            UserDtoForCreate userDtoForCreate = new UserDtoForCreate("테스트 유저 " + i, "testPassword" + i, "테스트 이메일" + i);
            userService.create(userDtoForCreate, null);
        }
        List<ReadStatus> readStatusList = readStatusRepository.findAll();
    }
}
