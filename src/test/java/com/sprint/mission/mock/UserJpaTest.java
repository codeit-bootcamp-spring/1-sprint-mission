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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.*;
//@ActiveProfiles("test")  // 위 설정 적용

//@ExtendWith(MockitoExtension.class)
//@ComponentScan(basePackages = "com.sprint.mission.dto")
@DataJpaTest
//@TestPropertySource(properties = {"spring.sql.init.mode=never"}) // application-test.yml 설정 무시
@ActiveProfiles("test")
public class UserJpaTest {

    private static final Logger log = LoggerFactory.getLogger(UserTest.class);

    @Autowired
    private UserRepository userRepository;

    private BinaryContentMapper binaryContentMapper;

    @Test
    @DisplayName("UserRepository save 테스트")
    void saveTest(){

        // given
        MockMultipartFile mockMultipartFile = getMockMultipartFile();
        BinaryContent binaryContent = new BinaryContent(mockMultipartFile.getName(), mockMultipartFile.getSize(), mockMultipartFile.getContentType());
        User beforeSaveUser = new User("save 테스트 유저", "testPassword", "testEmail", binaryContent);

        // when
        User savedUser = userRepository.save(beforeSaveUser);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(beforeSaveUser.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo(beforeSaveUser.getPassword());
        assertThat(savedUser.getEmail()).isEqualTo(beforeSaveUser.getEmail());
    }


    @Test
    @DisplayName("이름 중복 테스트")
    void duplicateNameTest() {

    }


    private void stub_binaryService_userStatusService_ForCreate() {

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
