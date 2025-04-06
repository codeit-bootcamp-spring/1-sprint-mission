package com.sprint.mission.unit;

//create, update, delete 메소드
//핵심 메소드에 대해 각각 최소 2개 이상(성공, 실패)의 테스트 케이스를 작성


import com.sprint.mission.common.exception.CustomErrorResponse;
import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.dto.BinaryContentMapper;
import com.sprint.mission.dto.UserMapper;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.service.BinaryService;
import com.sprint.mission.service.UserService;
import com.sprint.mission.service.jcf.main.JCFUserService;
import com.sprint.mission.service.jcf.main.UserServiceSupporter;
import com.sprint.mission.service.jcf.main.UserValidator;
import org.assertj.core.api.Assertions;
import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.processing.SQL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


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
        MockMultipartFile mockFile = getMockFile();

        when(binaryService.create(any(BinaryContentDtoForCreate.class))).thenAnswer((invocation) -> {
            BinaryContentDtoForCreate binaryDto = invocation.getArgument(0);
            BinaryContent profile = binaryContentMapper.toEntity(binaryDto);
            ReflectionTestUtils.setField(profile, "id", UUID.randomUUID());
            ReflectionTestUtils.setField(profile, "createdAt", Instant.now());
            return profile;
        });

        //when
        User createdUser = userServiceSupporter.createUser(dto, mockFile);

        //then
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo(dto.username());
        assertThat(createdUser.getPassword()).isEqualTo(dto.password());
        assertThat(createdUser.getEmail()).isEqualTo(dto.email());
        assertThat(createdUser.getProfile()).isNotNull();
        assertThat(createdUser.getProfile().getFileName()).isEqualTo(mockFile.getName());
        assertThat(createdUser.getProfile().getContentType()).isEqualTo(mockFile.getContentType());
        assertThat(createdUser.getProfile().getSize()).isEqualTo(mockFile.getSize());
    }


    @Test
    @DisplayName("회원가입 실패 - 중복된 이름")
    void isDuplicateName() {
        // given
        User user1 = new User("중복 될 이름1", "테스트 패스워드", "테스트 이메일", null);
        User user2 = new User("중복 안 될 이름2", "테스트 패스워드", "테스트 이메일", null);
        List<User> userList = List.of(user1, user2);

        //when
        UserValidator userValidator = new UserValidator();

        //then
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

        //when
        UserValidator userValidator = new UserValidator();

        //then
        assertThatThrownBy(() ->
                        userValidator.isDuplicateNameEmail(userList, "유저 444", "중복 이메일"))
                .isInstanceOf(CustomException.class);
    }

    private MockMultipartFile getMockFile() {
        return new MockMultipartFile("파일 1", "thisIsMockFile.png", "image/png", "mockFile".getBytes());
    }
}