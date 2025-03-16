package com.sprint.mission.cascade;

import com.sprint.mission.dto.UserMapper;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserStatusRepository;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import com.sprint.mission.service.jcf.main.JCFUserService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
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

    @DisplayName("User의 userstauts 필드가 cascade Remove설정된거 테스트")
    @Test
    void userAndUserStatus(){
        // Given
        UserDtoForCreate userDto = new UserDtoForCreate("testUser", "testPassword", "testEmail");
        User user = userService.create(userDto, null);
        UUID testUserId = user.getId();
        em.flush();
        em.clear();

        Optional<UserStatus> userStatus = userStatusRepository.findByUser_Id(testUserId);
        Assertions.assertThat(userStatus).isPresent();

        userService.delete(testUserId);

        Optional<UserStatus> deletedUserStatus = userStatusRepository.findByUser_Id(testUserId);
        Assertions.assertThat(deletedUserStatus).isEmpty();
    }


    @Autowired
    private UserMapper userMapper;

    @Test
    void userAndProfile(){
        // Given
        UserDtoForCreate userDto = new UserDtoForCreate("testUser", "testPassword", "testEmail");
        new BinaryContent()
        userMapper.toEntityWithProfile(userDto, )

    }
}
