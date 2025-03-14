package com.sprint.mission.BasicTest;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.dto.UserMapper;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.dto.request.UserDtoForUpdate;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.repository.UserStatusRepository;
import com.sprint.mission.service.UserService;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private UserMapper userMapper;

    @Autowired
    private UserStatusRepository userStatusRepository;

    //@BeforeEach
    void createTest() {
        for (int i = 0; i < 20; i++) {
            UserDtoForCreate createDto = new UserDtoForCreate("테스트 유저 " + i, "testPassword" + i, "테스트 이메일" + i);
            userService.create(createDto, null);
        }
    }

    @Test
    void setUpTest() {
        List<User> users = userRepository.findAll();
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(20);
        users.forEach(user -> {
            UserStatus findUserstatus = userStatusService.findById(user.getStatus().getId());
            assertThat(findUserstatus.getUser()).isEqualTo(user);
        });
    }

    @Test
    void cascadeUserAndUserStatusTest() {
        for (int i = 0; i < 20; i++) {
            UserDtoForCreate createDto = new UserDtoForCreate("테스트 유저 " + i, "testPassword" + i, "테스트 이메일" + i);
            userService.create(createDto, null);
        }

        List<User> userList = userService.findAll();
        List<UserStatus> userStatusList = userStatusService.findAll();

        assertThat(userList.size()).isEqualTo(20);
        assertThat(userList.size()).isEqualTo(userStatusList.size());
    }

    @Test
    void userEqualsHashCodeTest() {
        UserDtoForCreate createDto = new UserDtoForCreate("test 유저 1", "test 패스워드 1", "test 이메일 1");
        User createdUser1 = userService.create(createDto, null);

        User createdUser = userMapper.toEntityWithoutProfile(createDto);
        User savedUser = userRepository.save(createdUser);
        System.out.println("createdUser = " + createdUser + "ID = " + createdUser.getId());
        System.out.println("savedUser = " + savedUser + "ID = " + savedUser.getId());
        Assertions.assertThat(createdUser).isEqualTo(createdUser);

        User user = new User(createdUser1.getUsername(), createdUser1.getPassword(), createdUser1.getEmail());
        //user.setId(createdUser1.getId());

        em.flush();
        em.clear();

        //assertThat(user).isEqualTo(createdUser1);
        // Equals, HashCode를 정의하지 않으면 false
    }

    @Test
    void duplicateUserTest() {
        User user = new User("test 유저 1", "test 패스워드 1", "test 이메일 1");
        userRepository.save(user);

        User nameDuplicatedUser = new User("test 유저 1", "test 패스워드 1", "test 이메일1111");
        assertThatThrownBy(() -> userService.isDuplicateNameEmail(nameDuplicatedUser.getUsername(), nameDuplicatedUser.getEmail()))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void updateUser() {
        User beforeUpdateUser = new User("업데이트 전 이름", "업데이트 전 비밀번호", "업데이트 전 이메일");
        userRepository.save(beforeUpdateUser);
        em.flush();
        em.clear();
        UserDtoForUpdate userDtoForUpdate = new UserDtoForUpdate("업데이트 후 이름", "업데이트 후 비밀번호", "업데이트 후 이메일");
        User updatingUser = userRepository.findById(beforeUpdateUser.getId()).get();
        updatingUser.update(userDtoForUpdate.username(), userDtoForUpdate.password(), userDtoForUpdate.email());
        //updatingUser.update("업데이트 후 이름", "업데이트 후 비밀번호", "업데이트 후 이메일");
        em.flush();
        em.clear();
        User updatedUser = userRepository.findById(beforeUpdateUser.getId()).get();
        assertThat(updatedUser.getUsername()).isEqualTo("업데이트 후 이름");
        assertThat(updatedUser.getPassword()).isEqualTo("업데이트 후 비밀번호");
        assertThat(updatedUser.getEmail()).isEqualTo("업데이트 후 이메일");
    }

    @Test
    void find() {
        List<User> all = userRepository.findAll();
        assertThat(all).isNotEmpty();
        assertThat(all.size()).isEqualTo(20);
    }

    @Test
    void delete() {
        User beforeDeleteUser = new User("삭제 전 이름", "삭제 전 비밀번호", "삭제 전 이메일");
        userRepository.save(beforeDeleteUser);
        em.flush();
        em.clear();

        Optional<User> savedUser = userRepository.findById(beforeDeleteUser.getId());
        assertThat(savedUser).isPresent();

        userService.delete(beforeDeleteUser.getId());
        assertThatThrownBy(() -> userService.findById(beforeDeleteUser.getId()))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void findStatusMap() {
        for (int i = 0; i < 3; i++) {
            UserDtoForCreate userDtoForCreate = new UserDtoForCreate("테스트 유저 " + i, "testPassword" + i, "테스트 이메일" + i);
            userService.create(userDtoForCreate, null);
        }
        em.flush();
        em.clear();
//        Map<User, Boolean> statusMapByUserList = userStatusService.findStatusMapByUserList();
//        assertThat(statusMapByUserList.size()).isEqualTo(3);
//        for (Map.Entry<User, Boolean> entry : statusMapByUserList.entrySet()) {
//            User user = entry.getKey();
//            Boolean status = entry.getValue();
//            assertThat(user).isNotNull();
//            assertThat(status).isNotNull();
//        }
    }

    @Test
    void findAllWithStatusTest() {
        for (int i = 0; i < 3; i++) {
            UserDtoForCreate userDtoForCreate = new UserDtoForCreate("테스트 유저 " + i, "testPassword" + i, "테스트 이메일" + i);
            userService.create(userDtoForCreate, null);
        }
        em.flush();
        em.clear();

        List<User> all = userRepository.findAllWithRelations();
    }
}
