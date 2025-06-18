package com.sprint.mission.discodeit.event;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.common.AsyncDebugService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AsyncContextPropagationTest {

  @Autowired
  AsyncDebugService asyncDebugService;

  @Test
  @DisplayName("MDC requestId, Security Context의 인증 정보가 유지되는지 확인")
  void contextIsPreservedInAsyncThread() throws ExecutionException, InterruptedException {
    // given
    String testReqId = UUID.randomUUID().toString();
    String testUsername = "testUser";

    MDC.put("requestId", testReqId);
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
        testUsername, "password", List.of());
    SecurityContextHolder.getContext().setAuthentication(auth);

    // when
    Map<String, String> context = asyncDebugService.checkContext().get();

    // then
    assertThat(context.get("requestId")).isEqualTo(testReqId);
    assertThat(context.get("userName")).isEqualTo(testUsername);
  }

}
