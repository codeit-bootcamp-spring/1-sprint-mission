package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class TestBasicUserService {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @InjectMocks
    private BasicUserService basicUserService;

    @Test
    void createUserTest() {
        UserCreateRequestDTO request = new UserCreateRequestDTO("Alice", "alice@naver.com", "12345",null, "ONLINE");
        User createdUser = basicUserService.createUser(request);
        // when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        assertEquals("Bob", createdUser.getName());
        assertEquals("alice@naver.com", createdUser.getEmail());
    }
}
