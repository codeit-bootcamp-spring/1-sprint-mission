package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.role.Role;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "JWT_SECRET_KEY=asdwqnkjcwqdhjcjkbxcqkuwdhulczxkcqbuwdbkuczxbjkcqwidwqlhixczbjqwdwq")
class JwtServiceTest {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private UserRepository userRepository;

  private String jwtAccessToken = "";
  private String jwtRefreshToken = "";

  @BeforeEach
  void init() {
    UserDto dto = new UserDto(UUID.randomUUID(), "username", "user@mail.com", null, true,
        Role.ROLE_ADMIN);

    jwtAccessToken = jwtService.generateAccessToken(dto);
    jwtRefreshToken = jwtService.generateRefreshToken(dto);

    User user = new User("username", "user@mail.com", "{noop}pasdqwddqwdw", null, Role.ROLE_ADMIN);
    userRepository.save(user);
  }

  @Test
  @DisplayName("Jwt 토큰 생성")
  void create() {

    UserDto dto = new UserDto(UUID.randomUUID(), "username", "user@mail.com", null, true,
        Role.ROLE_ADMIN);

    String accessToken = jwtService.generateAccessToken(dto);

    System.out.println("accessToken = " + accessToken);

    Assertions.assertThat(accessToken).isNotNull();
  }

  @Test
  @DisplayName("refresh 토큰 생성")
  void createRefreshToken() {

    UserDto dto = new UserDto(UUID.randomUUID(), "username", "user@mail.com", null, true,
        Role.ROLE_ADMIN);

    String refreshToken = jwtService.generateRefreshToken(dto);

    System.out.println("refreshToken = " + refreshToken);

    Assertions.assertThat(refreshToken).isNotNull();
  }

  @Test
  @DisplayName("토큰 검증")
  void validation() {

    boolean validateToken = jwtService.validate(jwtAccessToken);
    Assertions.assertThat(validateToken).isTrue();

    boolean validateRefreshToken = jwtService.validate(jwtRefreshToken);
    Assertions.assertThat(validateRefreshToken).isTrue();
  }
}