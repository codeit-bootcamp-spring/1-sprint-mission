package com.sprint.mission.discodeit.integration;


import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.error.ErrorResponse;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

@SpringBootTest(classes = DiscodeitApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ChannelIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChannelRepository channelRepository;
  @PersistenceContext
  private EntityManager em;

  private String getBaseUrl() {
    return "http://localhost:" + port + "/api/channels";
  }


  @Test
  @DisplayName("공개 채널을 생성할 수 있다")
  void createPublicChannel_success() throws Exception {
    CreateChannelDto requestDto = new CreateChannelDto("channel", "channel description");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(requestDto),
        headers);

    ResponseEntity<ChannelResponseDto> response = restTemplate.postForEntity(
        getBaseUrl() + "/public",
        request,
        ChannelResponseDto.class
    );

    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody().name()).isEqualTo("channel");
    assertThat(response.getBody().participants()).isEmpty();
  }

  @Test
  @DisplayName("공개 채널 생성시 이름이 비어있으면 예외")
  void createPublicChannel_shouldFail_ifNameIsEmpty() throws Exception {
    CreateChannelDto requestDto = new CreateChannelDto("", "channel description");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(requestDto),
        headers);

    ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
        getBaseUrl() + "/public",
        request,
        ErrorResponse.class
    );

    assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    assertThat(response.getBody().getExceptionType()).isEqualTo(
        "MethodArgumentNotValidException");
  }

  @Test
  @Sql(scripts = "/insert_user.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
  @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("비공개 채널을 생성할 수 있다")
  void createPrivateChannel_success() throws Exception {
    String userId1 = "00000000-0000-0000-0000-000000000001";
    String userId2 = "00000000-0000-0000-0000-000000000002";

    // given
    List<String> participantIds = List.of(userId1, userId2);
    CreatePrivateChannelDto createDto = new CreatePrivateChannelDto(participantIds);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(
        objectMapper.writeValueAsString(createDto),
        headers
    );

    // when
    ResponseEntity<ChannelResponseDto> response = restTemplate.postForEntity(
        getBaseUrl() + "/private",
        request,
        ChannelResponseDto.class
    );

    // then
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody().participants()).hasSize(2);
    assertThat(response.getBody().name()).isEmpty();

    List<String> userIds = response.getBody().participants().stream().map(u -> u.id().toString())
        .collect(
            Collectors.toList());

    assertThat(userIds).containsExactlyInAnyOrder(userId1, userId2);
  }

  @Test
  @Sql(scripts = {
      "/insert_channel.sql"}, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
  @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("공개 채널을 수정할 수 있다")
  void updatePublicChannel_success() throws Exception {
    // given
    String channelId = "00000000-0000-0000-0000-000000000001";
    ChannelUpdateDto updateDto = new ChannelUpdateDto("updatedName", "updatedDescription");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(
        objectMapper.writeValueAsString(updateDto),
        headers
    );

    // when
    ResponseEntity<ChannelResponseDto> response = restTemplate.exchange(
        getBaseUrl() + "/" + channelId,
        HttpMethod.PATCH,
        request,
        ChannelResponseDto.class
    );

    // then
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    Optional<Channel> optional = channelRepository.findById(UUID.fromString(channelId));

    assertThat(optional).isPresent();
    assertThat(optional.get().getName()).isEqualTo("updatedName");
  }

  @Test
  @Sql(scripts = {
      "/insert_channel.sql"}, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
  @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("비공개 채널수정시 예외를 던진다")
  void updatePrivateChannel_throwsException() throws Exception {
    // given
    String channelId = "00000000-0000-0000-0000-000000000002";
    ChannelUpdateDto updateDto = new ChannelUpdateDto("updatedName", "updatedDescription");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(
        objectMapper.writeValueAsString(updateDto),
        headers
    );

    // when
    ResponseEntity<ErrorResponse> response = restTemplate.exchange(
        getBaseUrl() + "/" + channelId,
        HttpMethod.PATCH,
        request,
        ErrorResponse.class
    );

    // then
    assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    assertThat(response.getBody().getExceptionType()).isEqualTo("PrivateChannelUpdateException");
  }

  @Test
  @Sql(scripts = {
      "/insert_channel.sql"}, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
  @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("채널을 삭제할 수 있다")
  void deleteChannel_success() throws Exception {
    // when
    String channelId = "00000000-0000-0000-0000-000000000001";

    //when
    restTemplate.delete(getBaseUrl() + "/" + channelId);
    Optional<Channel> optional = channelRepository.findById(UUID.fromString(channelId));

    // then
    assertThat(optional).isEmpty();
  }

  @Test
  @DisplayName("존재하지 않는 채널 삭제 시도시 예외가 발생하지 않는다")
  void deleteChannelThatDoesNotExists_success() throws Exception {
    // when
    String channelId = "00000000-0000-0000-0000-000000000003";

    //when
    ResponseEntity<Void> response = restTemplate.exchange(
        getBaseUrl() + "/" + channelId,
        HttpMethod.DELETE,
        null,
        Void.class
    );

    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
  }
}
