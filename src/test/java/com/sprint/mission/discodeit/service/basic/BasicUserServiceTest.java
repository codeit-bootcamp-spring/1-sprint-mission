package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.role.Role;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Tag("db")
@ActiveProfiles("test")
@SpringBootTest
class BasicUserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("프로필을 포함하지 않은 사용자 생성 요청을 받아 사용자 생성 및 DB에 저장합니다.")
  @Transactional
    // 실제 구현체에도 적용되어 있는지 확인 필요 (유무에 따라 결과가 달라질 수 있기에)
  void createOnlyDtoTest() throws IOException {
    // given
    UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto(
        "cloudsoda", "구름소다", "cloudsoda@mail.com",
        "!@A445sndk", "010-1111-2222", Phone.RegionCode.KR,
        Role.USER, ""
    );

    // when
    UserResponseDto userCreateResult = userService.create(userSignupRequestDto, null);

    // then
    assertThat(userCreateResult).isNotNull()
        .extracting("nickname", "email")
        .contains("구름소다", "cloudsoda@mail.com");
  }

  @Test
  @DisplayName("사용자 삭제 요청을 받아 사용자 삭제를 수행합니다.")
  @Transactional
  void deleteUserTest() {
    // given
    User user = new User(
        "cloudsoda", "구름소다", "cloudsoda@mail.com",
        "!@A445sndk", new Phone("010-1111-2222", Phone.RegionCode.KR),
        Role.USER, "", null
    );
    userRepository.save(user); // 저장 후 ID를 얻기 위함

    // when
    UserResponseDto userDeleteResult = userService.delete(user.getId());

    // then
    assertThat(userDeleteResult).isNotNull()
        .extracting("nickname", "email")
        .contains("구름소다", "cloudsoda@mail.com");
  }
}