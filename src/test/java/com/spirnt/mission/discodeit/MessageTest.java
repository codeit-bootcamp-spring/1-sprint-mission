package com.spirnt.mission.discodeit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.spirnt.mission.discodeit.dto.ErrorResponse;
import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageDto;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.dto.response.PageResponse;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  @Transactional
  @DisplayName("메시시 생성 테스트 - 성공")
  void testCreateMessageSuccess() {
    MessageCreateRequest messageCreateRequest = new MessageCreateRequest("content",
        UUID.fromString("00000000-0000-0000-0000-000000000101"),
        UUID.fromString("11111111-1111-1111-1111-111111111112"));
    MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<>();
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<MessageCreateRequest> jsonPart = new HttpEntity<>(messageCreateRequest, jsonHeaders);
    multipartMap.add("messageCreateRequest", jsonPart);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartMap,
        headers);

    // when
    ResponseEntity<MessageDto> response = restTemplate.postForEntity(
        "/api/messages",
        requestEntity,
        MessageDto.class
    );

    // then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("content", response.getBody().getContent());
  }

  @Test
  @Transactional
  @DisplayName("메시시 생성 테스트 - 실패 ")
  void testCreateMessageFail() {
    MessageCreateRequest messageCreateRequest = new MessageCreateRequest("content",
        UUID.fromString("11100000-0000-0000-0000-000000000101"),
        UUID.fromString("11111111-1111-1111-1111-111111111112"));
    MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<>();
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<MessageCreateRequest> jsonPart = new HttpEntity<>(messageCreateRequest, jsonHeaders);
    multipartMap.add("messageCreateRequest", jsonPart);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartMap,
        headers);

    // when
    ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
        "/api/messages",
        requestEntity,
        ErrorResponse.class
    );

    // then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("UserNotFoundException", response.getBody().getExceptionType());
  }

  @Test
  @Transactional
  @DisplayName("메시지 수정 테스트 - 성공")
  void testUpdateMessageSuccess() {
    MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest("new Content");
    UUID messageId = UUID.fromString("22222222-2222-2222-2222-222222222222");
    ResponseEntity<MessageDto> response = restTemplate.exchange(
        "/api/messages/" + messageId,
        HttpMethod.PATCH,
        new HttpEntity<>(messageUpdateRequest),
        MessageDto.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(messageUpdateRequest.newContent(), response.getBody().getContent());
  }

  @Test
  @Transactional
  @DisplayName("메시지 수정 테스트 - 실패")
  void testUpdateMessageFail() {
    MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest("new Content");
    UUID messageId = UUID.fromString("22222222-0000-2222-2222-222222222222");
    ResponseEntity<ErrorResponse> response = restTemplate.exchange(
        "/api/messages/" + messageId,
        HttpMethod.PATCH,
        new HttpEntity<>(messageUpdateRequest),
        ErrorResponse.class
    );

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("MessageNotFoundException", response.getBody().getExceptionType());
  }

  @Test
  @Transactional
  @DisplayName("메시지 삭제 테스트 - 성공")
  void testDeleteMessageSuccess() {
    UUID messageId = UUID.fromString("22222222-2222-2222-2222-222222222222");
    ResponseEntity<Void> response = restTemplate.exchange(
        "/api/messages/" + messageId,
        HttpMethod.DELETE,
        null,
        Void.class
    );
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  @Transactional
  @DisplayName("메시지 삭제 테스트 - 실패")
  void testDeleteMessageFail() {
    UUID messageId = UUID.randomUUID();
    ResponseEntity<ErrorResponse> response = restTemplate.exchange(
        "/api/messages/" + messageId,
        HttpMethod.DELETE,
        null,
        ErrorResponse.class
    );
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("MessageNotFoundException", response.getBody().getExceptionType());
  }

  @Test
  @Transactional
  @DisplayName("메시지 목록 조회 테스트 - 성공")
  void testFindMessagesSuccess() {
    // given
    UUID channelId = UUID.fromString("11111111-1111-1111-1111-111111111112");

    ResponseEntity<PageResponse<MessageDto>> response = restTemplate.exchange(
        "/api/messages?channelId=" + channelId + "&size=5",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<PageResponse<MessageDto>>() {
        }
    );
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().getContent().size() >= 0);
  }

  @Test
  @Transactional
  @DisplayName("메시지 목록 조회 테스트 - 실패")
  void testFindMessagesFail() {
    // given
    UUID channelId = UUID.randomUUID();
    ResponseEntity<ErrorResponse> response = restTemplate.exchange(
        "/api/messages?channelId=" + channelId,
        HttpMethod.GET,
        null,
        ErrorResponse.class
    );
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("ChannelNotFoundException", response.getBody().getExceptionType());
  }

}
