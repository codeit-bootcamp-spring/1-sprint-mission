package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

    @Mock
    private UserStatusRepository userStatusRepository;
    @Mock private UserRepository userRepository;
    @Mock private BinaryContentRepository binaryContentRepository;
    @Mock private BinaryContentStorage binaryContentStorage;
    @Mock private com.sprint.mission.discodeit.mapper.BinaryContentMapper binaryContentMapper;

    @Spy
    @InjectMocks
    private UserMapper userMapper;

    @InjectMocks
    private BasicUserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(userMapper, "userStatusRepository", userStatusRepository);
        ReflectionTestUtils.setField(userMapper, "binaryContentMapper", binaryContentMapper);
    }

    @Test
    void createUserSuccess() {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("정연경")
                .email("imyg02@gmail.com")
                .password("123456")
                .build();

        MockMultipartFile profile = new MockMultipartFile(
                "profile1", "test1.png", "image/png", new byte[10]);

        UUID profileId = UUID.randomUUID();

        BinaryContent fakeProfile = BinaryContent.builder()
                .fileName(profile.getOriginalFilename())
                .size((int) profile.getSize())
                .contentType(profile.getContentType())
                .build();
        ReflectionTestUtils.setField(fakeProfile, "id", profileId);

        UUID userId = UUID.randomUUID();
        User fakeUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .profile(fakeProfile)
                .build();
        ReflectionTestUtils.setField(fakeUser, "id", userId);

        UserStatus fakeStatus = UserStatus.builder()
                .user(fakeUser)
                .lastActiveAt(Instant.now())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(fakeUser);
        when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(fakeProfile);
        when(binaryContentStorage.put(any(), any())).thenReturn(profileId);
        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(fakeStatus);
        when(userStatusRepository.findByUserId(userId)).thenReturn(Optional.of(fakeStatus));

        BinaryContentDto fakeProfileDto = BinaryContentDto.builder()
                .id(profileId)
                .fileName(fakeProfile.getFileName())
                .size(fakeProfile.getSize())
                .contentType(fakeProfile.getContentType())
                .build();

        when(binaryContentMapper.toDto(any(BinaryContent.class))).thenReturn(fakeProfileDto);

        // when
        UserDto createdUser = userService.create(request, Optional.of(profile));

        // then
        assertEquals("정연경", createdUser.getUsername());
        verify(binaryContentRepository).save(any(BinaryContent.class));
        verify(binaryContentStorage).put(any(), any());
        verify(userRepository).save(any(User.class));
        verify(userStatusRepository).save(any(UserStatus.class));
    }
}
