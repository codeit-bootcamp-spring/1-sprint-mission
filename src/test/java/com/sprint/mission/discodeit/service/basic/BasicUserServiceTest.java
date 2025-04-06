package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "discodeit.storage.type=local"
})
@ExtendWith(SpringExtension.class)
@Transactional
class BasicUserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserStatusRepository userStatusRepository;

    @Test
    void create() {
        // given
        // 회원 생성
        UserCreateRequest request = UserCreateRequest.builder()
                .username("정연경")
                .email("imyg02@gmail.com")
                .password("123456")
                .build();

        // Mock 프로필 파일 생성
        MockMultipartFile profile = new MockMultipartFile(
                "profile1", "test1.png", "image/png", new byte[10]);


        // when
        UserDto createdUser = userService.create(request, Optional.of(profile));

        // then
        assertEquals("정연경", createdUser.getUsername());

    }

//    @Test
//    void update() {
//    }
//
//    @Test
//    void delete() {
//    }
}