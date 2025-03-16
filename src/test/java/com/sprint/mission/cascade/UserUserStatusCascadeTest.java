package com.sprint.mission.cascade;

import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserStatusRepository;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import com.sprint.mission.service.jcf.main.JCFUserService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class UserUserStatusCascadeTest {


    private static final Logger log = LoggerFactory.getLogger(UserUserStatusCascadeTest.class);
    @Autowired
    private EntityManager em;

    @Autowired
    private JCFUserService userService;

    @Autowired
    private UserStatusService userStatusService;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Test
    void userAndUserStatus(){
        UserDtoForCreate userDto = new UserDtoForCreate("testUser", "testPassword", "testEmail");
        User user = userService.create(userDto, null);
        em.flush();
        em.clear();

        Optional<UserStatus> userStatus = userStatusRepository.findByUser_Id(user.getId());
        Assertions.assertThat(userStatus).isPresent();

    }
}
