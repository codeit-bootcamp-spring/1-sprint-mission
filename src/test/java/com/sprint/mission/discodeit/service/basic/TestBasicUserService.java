package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import com.sprint.mission.discodeit.service.Interface.UserStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TestBasicUserService {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private UserStatusService userStatusService;

    @Mock
    private BinaryContentService binaryContentService;

    @InjectMocks
    private BasicUserService basicUserService;

    @Test
    void createUserTest() throws Exception {
        MockMultipartFile profileImage=new MockMultipartFile(
                "profileImage",
                "profile.jpg",
                "image/jpeg",
                "dummy image content".getBytes()
        );
        UserCreateRequestDto request = new UserCreateRequestDto("Alice", "alice@naver.com", "12345",profileImage, "ONLINE");
        User createdUser = basicUserService.createUser(request);

        assertEquals("Alice", createdUser.getName());
        assertEquals("alice@naver.com", createdUser.getEmail());
        assertNotNull(createdUser);
    }
}
