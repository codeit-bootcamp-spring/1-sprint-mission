package com.sprint.mission.cascade;

import com.sprint.mission.dto.UserMapper;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.BinaryContentRepository;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.repository.UserStatusRepository;
import com.sprint.mission.service.jcf.addOn.BinaryService;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import com.sprint.mission.service.jcf.main.JCFUserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BinaryContentRepository binaryContentRepository;

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
        assertThat(userStatus).isPresent();

        userService.delete(testUserId);

        Optional<UserStatus> deletedUserStatus = userStatusRepository.findByUser_Id(testUserId);
        assertThat(deletedUserStatus).isEmpty();
    }


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BinaryService binaryService;


    @DisplayName("BinaryContent 생성 테스트")
    @Test
    void binaryTest(){
        // Given
        UserDtoForCreate userDto = new UserDtoForCreate("testUser", "testPassword", "testEmail");
        BinaryContentDtoForCreate dto = new BinaryContentDtoForCreate("testFileName", "testContentType", 100L, new byte[100]);
        BinaryContent binaryContent = binaryService.create(dto);
        log.info("binaryContent = {}", binaryContent);
        assertThat(binaryContent).isNotNull();
    }


    @DisplayName("Profile이 사라지면 User의 profile 필드가 null로 세팅되는지 테스트")
    @Test
    void userAndProfile(){
        // Given
        UserDtoForCreate userDto = new UserDtoForCreate("testUser", "testPassword", "testEmail");
        userService.create(userDto, null);
        BinaryContentDtoForCreate dto = new BinaryContentDtoForCreate("testFileName", "testContentType", 100L, new byte[100]);

        // When
        User createdUser = createUser(dto, userDto);
        User savedUser = userRepository.save(createdUser);
        log.info("savedUser = {}", savedUser); //User(username=testUser, email=testEmail, password=testPassword, profile=BinaryContent(fileName=testFileName, size=100, contentType=testContentType))
        BinaryContent profile = savedUser.getProfile();
        em.flush();
        em.clear();

        // Then 1. 일단 잘 저장됐는지 확인
        Optional<User> user = userRepository.findById(savedUser.getId());
        assertThat(user).isPresent();
        Optional<BinaryContent> fProfile = binaryContentRepository.findById(profile.getId());
        assertThat(fProfile).isPresent();
        log.info("================초기화 전================");

        em.flush();
        em.clear();

        // WHen 2. BinaryContent 삭제 for cascade 확인
        BinaryContent deletedBinary = binaryService.findById(profile.getId());
        binaryService.deleteById(deletedBinary.getId());
        // EntityGraph로 연관된거 다 가져와서 flush 해야 함
        em.flush();
        em.clear();

        // Then 2. cascade 확인 : binaryContent 삭제되도 User의 profile필드는 null로 유지되어야 한다.
        User testUser = userService.findAll().get(0);
        log.info("testUser = {}", testUser.getProfile());
        assertThat(testUser.getProfile()).isNull();
    }

    private User createUser(BinaryContentDtoForCreate dto, UserDtoForCreate userDto) {
        Optional<BinaryContentDtoForCreate> profileDto = Optional.of(dto);
        User createdUser = profileDto.map((binaryDto) -> {
            BinaryContent createdBinaryContent = binaryService.create(binaryDto);
            return userMapper.toEntityWithProfile(userDto, createdBinaryContent);
        }).orElseGet(() -> userMapper.toEntityWithoutProfile(userDto));
        return createdUser;
    }


    //@Slf4j
    //public record BinaryContentDtoForCreate(
    //
    //        @Schema(example = "zessy")
    //        String fileName,
    //        String contentType,
    //        Long size,
    //        byte[] bytes) {
}
