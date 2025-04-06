package com.sprint.mission.mock;


import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.dto.BinaryContentMapper;
import com.sprint.mission.dto.BinaryContentMapperImpl;
import com.sprint.mission.dto.UserMapper;
import com.sprint.mission.dto.UserMapperImpl;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.service.jcf.addOn.BinaryService;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import com.sprint.mission.service.jcf.main.JCFUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    private static final Logger log = LoggerFactory.getLogger(UserTest.class);

    @Mock
    private BinaryService binaryService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapper userMapper = new UserMapperImpl();
    // 외부 API 호출이 아니라 내부 변환 로직만 담당한다면,
    // 이를 @Mock 대신 @Spy로 설정하면 실제 객체의 동작을 그대로 사용하면서 필요한 경우 특정 메서드에 대해 스텁(stub) 처리할 수 있다.

    @Mock
    private UserStatusService userStatusService;

    @Spy
    private BinaryContentMapper binaryContentMapper = new BinaryContentMapperImpl();

    @InjectMocks
    private JCFUserService userService;

    @Test
    @DisplayName("UserService.create() 시 관련된 의존성을 Mocking하여 실제 호출하는지 테스트")
    void createTest(){

        // given
        // 1. 필요한 의존성과 격리시키기 : binary 서비스 + user레포지토리 + userstatus 서비스
        stub_binaryService_userRepository_userStatusService_ForCreate();
        MockMultipartFile mockMultipartFile = getMockMultipartFile();

        // when
        UserDtoForCreate dto1 = new UserDtoForCreate("테스트 유저 2", "testPassword", "testEmail");
        User createdUser = userService.create(dto1, mockMultipartFile);
        log.info("user.getProfile() : {}", createdUser.getProfile());  // user.getProfile() : BinaryContent(fileName=testFile, size=123, contentType=image/png)

        // then1 : 연관된 메서드들이 호출됐는지
        verify(binaryContentMapper, times(1)).convertFileToBinaryContentDto(mockMultipartFile);
        verify(binaryService, times(1)).create(any());  // binaryService.create 호출 확인인데 stub에 설정해놨기에 호출됨
        verify(userRepository, atMost(1)).save(any(User.class));
        verify(userStatusService, atLeast(1)).create(any(User.class));

        // then2 : 생성된 user가 올바르게 세팅 됐는지
        assertThat(createdUser.getCreatedAt()).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo(dto1.username());
        assertThat(createdUser.getPassword()).isEqualTo(dto1.password());
        assertThat(createdUser.getEmail()).isEqualTo(dto1.email());
    }


    @Test
    @DisplayName("이름 중복 테스트")
    void duplicateNameTest() {
        // given
        // 1. 필요한 의존성과 격리시키기 : binary 서비스 + user레포지토리 + userstatus 서비스
        stub_binaryService_userRepository_userStatusService_ForCreate();
        String duplicatedName = "testUser";

        List<User> userList = new ArrayList<>();
        userList.add(new User(duplicatedName, "testPassword", "testEmail", null));
        when(userRepository.findAll()).thenReturn(userList);

        // when
        // 2. 이름 및 이메일 중복 테스트
        UserDtoForCreate userDto = new UserDtoForCreate(duplicatedName, "testPassword", "testEmail");

        assertThatThrownBy(() -> userService.create(userDto, getMockMultipartFile()))
                .isInstanceOf(CustomException.class);
    }


    private void stub_binaryService_userRepository_userStatusService_ForCreate() {

        //lenient() : 이 stub은 테스트 중 반드시 사용되지 않아도 된다”는 의미를 부여 (이렇게 하지 않으면 오류 발생)
        BinaryContent binaryContent = new BinaryContent("testFile", 123L, "image/png");
        lenient().when(binaryService.create(any())).thenReturn(binaryContent);
        lenient().when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
            ReflectionTestUtils.setField(user, "createdAt", Instant.now());
            log.info("리플렉션을 통한 user의 id : {}", user.getId()); //리플렉션을 통한 user의 id : 092997ef-591f-4eb2-ac6d-4ca3a8c76457
            log.info("리플렉션을 통한 user의 createdAt : {}", user.getCreatedAt());
            return user;
        });
        lenient().when(userStatusService.create(any())).thenAnswer(invocationOnMock -> {
            return new UserStatus(invocationOnMock.getArgument(0));
        });
    }

    private MockMultipartFile getMockMultipartFile() {
        return new MockMultipartFile(
                "file name", // name
                "originalFileName", // originalFilename
                "image/png", // contentType
                "fake byte".getBytes() // content
        );
    }
}
//public record UserDtoForCreate(
//        String username,
//        String password,
//        String email) {
