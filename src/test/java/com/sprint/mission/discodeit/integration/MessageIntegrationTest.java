package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.error.ErrorResponse;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(classes = DiscodeitApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MessageIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private ChannelRepository channelRepository;
  @PersistenceContext
  private EntityManager em;

  private String getBaseUrl() {
    return "http://localhost:" + port + "/api/messages";
  }


  @Test
  @Sql(scripts = {
      "/insert_channel.sql", "/insert_user.sql"},
      config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
  @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("첨부파일 있는 메세지를 생성할 수 있다")
  void sendMessage_success() throws Exception {

    //given
    String channelId = "00000000-0000-0000-0000-000000000001";
    String authorId = "00000000-0000-0000-0000-000000000002";
    CreateMessageDto request = new CreateMessageDto(
        "content",
        channelId,
        authorId
    );

    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> messagePart = new HttpEntity<>(objectMapper.writeValueAsString(request),
        jsonHeaders);
    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    parts.add("messageCreateRequest", messagePart);

    byte[] fileContent = "dummy image content".getBytes(StandardCharsets.UTF_8);
    ByteArrayResource fileResource = new ByteArrayResource(fileContent) {
      @Override
      public String getFilename() {
        return "attachment.png";
      }
    };
    parts.add("attachments", fileResource);

    ResponseEntity<MessageResponseDto> response = restTemplate.postForEntity(getBaseUrl(), parts,
        MessageResponseDto.class);

    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody().content()).isEqualTo("content");
    assertThat(response.getBody().attachments()).hasSize(1);
  }

  @Test
  @Sql(scripts = {
      "/insert_channel.sql", "/insert_user.sql"},
      config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
  @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("channelId 없는 메시지 생성 요청시 예외를 던진다")
  void sendMessage_fail_whenChannelIdIsNull() throws Exception {
    String channelId = "";
    String authorId = "00000000-0000-0000-0000-000000000002";
    CreateMessageDto request = new CreateMessageDto(
        "content",
        channelId,
        authorId
    );
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> messagePart = new HttpEntity<>(objectMapper.writeValueAsString(request),
        jsonHeaders);
    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    parts.add("messageCreateRequest", messagePart);

    ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(getBaseUrl(), parts,
        ErrorResponse.class);

    assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    assertThat(response.getBody().getExceptionType()).isEqualTo("MethodArgumentNotValidException");
  }

  @Test
  @Sql(scripts = {
      "/insert_channel.sql", "/insert_user.sql"},
      config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
  @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("존재하지 않는 채널의 메시지 생성 요청시 예외를 던진다")
  void sendMessage_fail_whenChannelDoesNotExist() throws Exception {
    String channelId = "00000000-0000-0000-0000-000000000007";
    String authorId = "00000000-0000-0000-0000-000000000002";
    CreateMessageDto request = new CreateMessageDto(
        "content",
        channelId,
        authorId
    );
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> messagePart = new HttpEntity<>(objectMapper.writeValueAsString(request),
        jsonHeaders);
    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    parts.add("messageCreateRequest", messagePart);

    ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(getBaseUrl(), parts,
        ErrorResponse.class);

    assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    assertThat(response.getBody().getExceptionType()).isEqualTo("ChannelNotFoundException");
  }

  @Test
  @Sql(scripts = {"/insert_message.sql"},
      config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
  @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("메시지를 업데이트 할 수 있다")
  void updateMessage_success() throws Exception {
    // given
    String messageId = "00000000-0000-0000-0000-000000000001";
    MessageUpdateDto updateDto = new MessageUpdateDto("updated content");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(
        objectMapper.writeValueAsString(updateDto),
        headers
    );

    // when
    ResponseEntity<MessageResponseDto> response = restTemplate.exchange(
        getBaseUrl() + "/" + messageId,
        HttpMethod.PATCH,
        request,
        MessageResponseDto.class
    );

    // then
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody().content()).isEqualTo("updated content");
  }

  @Test
  @Sql(scripts = {"/insert_message.sql"},
      config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
  @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("빈 content 로 업데이트시 예외를 던진다")
  void updateMessage_fail() throws Exception {
    // given
    String messageId = "00000000-0000-0000-0000-000000000001";
    MessageUpdateDto updateDto = new MessageUpdateDto("");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(
        objectMapper.writeValueAsString(updateDto),
        headers
    );

    // when
    ResponseEntity<ErrorResponse> response = restTemplate.exchange(
        getBaseUrl() + "/" + messageId,
        HttpMethod.PATCH,
        request,
        ErrorResponse.class
    );

    // then
    assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    assertThat(response.getBody().getExceptionType()).isEqualTo("MethodArgumentNotValidException");
  }

  @Test
  @Sql(scripts = {"/insert_message.sql"},
      config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
  @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("메시지를 삭제할 수 있다")
  void deleteMessage_success() {
    // given
    String messageId = "00000000-0000-0000-0000-000000000001";

    // when
    restTemplate.delete(getBaseUrl() + "/" + messageId);

    // then
    Optional<Message> optional = messageRepository.findById(UUID.fromString(messageId));
    assertThat(optional).isEmpty();
  }

  @Test
  @DisplayName("UUID 가 잘못된 형식이면 예외를 던진다")
  void deleteMessage_fail() {
    // given
    String messageId = "invalidId";

    // when
    ResponseEntity<ErrorResponse> response =
        restTemplate.exchange(getBaseUrl() + "/" + messageId, HttpMethod.DELETE, null,
            ErrorResponse.class);

    // given
    assertThat(response.getStatusCode().is5xxServerError()).isTrue();
  }

  @Test
  @Sql(scripts = {"/data.sql"},
      config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
  @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("채널 ID 로 메시지를 페이지네이션 하여 조회할 수 있다")
  void getChannelMessages_success() {
    // given
    String channelId = "10000000-0000-0000-0000-000000000001";

    // when
    ResponseEntity<PageResponse> response = restTemplate.exchange(
        getBaseUrl() + "?channelId=" + channelId + "&size=10",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<PageResponse>() {
        }
    );

    System.out.println("TOTAL:" + response.getBody().totalElements());

    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().content()).hasSizeGreaterThan(0);
    assertThat(response.getBody().hasNext()).isTrue();
  }

  @Test
  @DisplayName("채널 ID가 비어 있으면 조회에 실패한다")
  void getMessagesByChannel_invalidRequest_fail() {
    String invalidChannelId = "";

    ResponseEntity<String> response = restTemplate.exchange(
        getBaseUrl() + "?channelId=" + invalidChannelId,
        HttpMethod.GET,
        null,
        String.class
    );

    assertThat(response.getStatusCode().is4xxClientError()).isTrue();
  }
}


