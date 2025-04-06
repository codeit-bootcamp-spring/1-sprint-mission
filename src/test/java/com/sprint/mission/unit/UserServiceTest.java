package com.sprint.mission.unit;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.dto.BinaryContentMapper;
import com.sprint.mission.dto.UserMapper;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.service.BinaryService;
import com.sprint.mission.service.jcf.serviceImpl.JCFUserService;
import com.sprint.mission.service.jcf.supporter.UserServiceSupporter;
import com.sprint.mission.unit.util.MockFileFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final MockFileFactory mockFileFactory = new MockFileFactory();

    @Spy
    private BinaryContentMapper binaryContentMapper = Mappers.getMapper(BinaryContentMapper.class);
    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    @Mock
    private BinaryService binaryService;

    @InjectMocks
    private UserServiceSupporter userServiceSupporter;


    @Test
    @DisplayName("회원가입 성공")
    void duplicateTest() {
        UserDtoForCreate dto = new UserDtoForCreate("test1", "비밀번호486", "icb6999@naver.com");
        MockMultipartFile mockFile = mockFileFactory.getMockFileList(1).getFirst();

        when(binaryService.create(any(BinaryContentDtoForCreate.class))).thenAnswer((invocation) -> {
            BinaryContentDtoForCreate binaryDto = invocation.getArgument(0);
            BinaryContent profile = binaryContentMapper.toEntity(binaryDto);
            setField(profile, "id", UUID.randomUUID());
            setField(profile, "createdAt", Instant.now());
            return profile;
        });

        //when
        User createdUser = userServiceSupporter.createUser(dto, mockFile);

        //then
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo(dto.username());
        assertThat(createdUser.getPassword()).isEqualTo(dto.password());
        assertThat(createdUser.getEmail()).isEqualTo(dto.email());

        BinaryContent profile = createdUser.getProfile();
        assertThat(profile).isNotNull();
        assertThat(profile.getFileName()).isEqualTo(mockFile.getName());
        assertThat(profile.getContentType()).isEqualTo(mockFile.getContentType());
        assertThat(profile.getSize()).isEqualTo(mockFile.getSize());
    }


    @Test
    @DisplayName("회원가입 실패 - 중복된 이름")
    void isDuplicateName() {
        // given
        User user1 = new User("중복 될 이름1", "테스트 패스워드", "테스트 이메일", null);
        User user2 = new User("중복 안 될 이름2", "테스트 패스워드", "테스트 이메일", null);
        List<User> userList = List.of(user1, user2);

        //when //then
        assertThatThrownBy(() ->
                userServiceSupporter.isDuplicateNameEmail(userList, "중복 될 이름1", "icb444@naver.com"))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void isDuplicateEmail() {
        // given
        List<User> userList = List.of(
                new User("유저 1", "테스트 패스워드", "중복 이메일", null),
                new User("유저 2", "테스트 패스워드", "테스트 이메일", null));

        //when //then
        assertThatThrownBy(() ->
                userServiceSupporter.isDuplicateNameEmail(userList, "유저 444", "중복 이메일"))
                .isInstanceOf(CustomException.class);
    }

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JCFUserService userService;

    @Test
    @DisplayName("Delete 실패 - userId에 맞는 user가 존재하지 않음")
    void deleteFail() {
        // when
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(userId))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("Delete 성공")
    void deleteSuccess() {
        // given
        UUID userId = UUID.randomUUID();
        User user = new User("지울 이름1", "비밀번호1", "이메일1", null);
        setField(user, "id", userId);
        setField(user, "createdAt", Instant.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        userService.delete(userId);

        // then
        verify(userRepository, times(1)).delete(user);
    }
}
