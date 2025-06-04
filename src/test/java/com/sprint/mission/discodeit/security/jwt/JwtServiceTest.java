package com.sprint.mission.discodeit.security.jwt;

import java.time.Clock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

  private static final String TEST_SECRET = "test-secret-key-must-be-at-least-32-characters-long";
  private static final String TEST_ISSUER = "test-api";
  private static final long ACCESS_VALIDITY = 900; // 15분
  private static final long REFRESH_VALIDITY = 86400; // 1일

  private JwtService jwtService;
  private Clock fixedClock;

}