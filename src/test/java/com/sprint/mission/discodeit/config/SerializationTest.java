package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.security.role.Role;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class SerializationTest {

  @Test
  @DisplayName("직렬화 라이브러리가 제대로 적용되었는지 확인하기 위함")
  void testSerializeChannelResponseDto() throws Exception {
    // given
    ChannelResponseDto dto = new ChannelResponseDto(UUID.randomUUID(), ChannelType.PUBLIC,
        "test-channel", "desc", List.of(
        new UserResponseDto(UUID.randomUUID(), "username", "nickname", "email", Role.USER, null,
            false)),
        Instant.now()
    );

    // when
    ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    String json = mapper.writeValueAsString(dto);

    // then
    System.out.println("Serialized JSON:\n" + json);
  }

}
