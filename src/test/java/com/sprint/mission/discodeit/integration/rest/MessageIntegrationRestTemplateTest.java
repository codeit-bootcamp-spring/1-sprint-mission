package com.sprint.mission.discodeit.integration.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.user.AuthRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MessageIntegrationRestTemplateTest {

  @LocalServerPort
  int port;

  @Autowired
  ObjectMapper objectMapper;

  RestTemplate restTemplate = new RestTemplate();

  @Test
  @Transactional
  void 회원가입_채널생성_메시지전송_삭제_조회() throws Exception {
    String baseUrl = "http://localhost:" + port;

    // 1. 회원가입
    var signUpRequest = new UserCreateRequestDto("홍길동", "hong@test.com", "1234");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    Resource profileImage = new ByteArrayResource("dummy".getBytes(StandardCharsets.UTF_8)) {
      @Override
      public String getFilename() {
        return "profile.png";
      }
    };

    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> userJson = new HttpEntity<>(objectMapper.writeValueAsString(signUpRequest),
        jsonHeaders);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("userCreateRequest", userJson);
    body.add("profile", profileImage);

    HttpEntity<MultiValueMap<String, Object>> signupRequest = new HttpEntity<>(body, headers);
    ResponseEntity<UserDto> signupResponse = restTemplate.postForEntity(baseUrl + "/api/users",
        signupRequest, UserDto.class);

    assertEquals(HttpStatus.CREATED, signupResponse.getStatusCode());
    UUID userId = signupResponse.getBody().getId();

    // 2. 로그인
    var loginRequest = new AuthRequestDto("홍길동", "1234");
    HttpHeaders loginHeaders = new HttpHeaders();
    loginHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> loginEntity = new HttpEntity<>(objectMapper.writeValueAsString(loginRequest),
        loginHeaders);
    ResponseEntity<UserDto> loginResponse = restTemplate.postForEntity(baseUrl + "/api/auth/login",
        loginEntity, UserDto.class);
    assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

    // 3. 채널 생성
    var createChannelRequest = new PublicChannelCreateRequestDto("테스트채널", "테스트 설명");
    HttpEntity<String> channelEntity = new HttpEntity<>(
        objectMapper.writeValueAsString(createChannelRequest), loginHeaders);
    ResponseEntity<Object> channelResponse = restTemplate.postForEntity(
        baseUrl + "/api/channels/public", channelEntity, Object.class);
    assertEquals(HttpStatus.CREATED, channelResponse.getStatusCode());

    // 채널 ID 추출
    String channelId = objectMapper.convertValue(channelResponse.getBody(), java.util.Map.class)
        .get("id").toString();

    // 4. 메시지 전송
    var messageRequest = new CreateMessageRequestDto("안녕하세요", UUID.fromString(channelId), userId);
    HttpEntity<String> messageJson = new HttpEntity<>(
        objectMapper.writeValueAsString(messageRequest), jsonHeaders);

    MultiValueMap<String, Object> messageBody = new LinkedMultiValueMap<>();
    messageBody.add("messageCreateRequest", messageJson);

    HttpEntity<MultiValueMap<String, Object>> messageEntity = new HttpEntity<>(messageBody,
        headers);
    ResponseEntity<MessageDto> messageResponse = restTemplate.postForEntity(
        baseUrl + "/api/messages", messageEntity, MessageDto.class);
    assertEquals(HttpStatus.CREATED, messageResponse.getStatusCode());

    UUID messageId = messageResponse.getBody().getId();

    // 5. 메시지 삭제
    restTemplate.delete(baseUrl + "/api/messages/" + messageId);

    // 6. 메시지 조회 → PageResponse<MessageDto>
    ResponseEntity<String> rawResponse = restTemplate.getForEntity(
        baseUrl + "/api/messages?channelId=" + channelId,
        String.class
    );

    PageResponse<MessageDto> pageResponse = objectMapper.readValue(
        rawResponse.getBody(),
        new TypeReference<PageResponse<MessageDto>>() {
        }
    );

    assertNotNull(pageResponse);
    assertTrue(pageResponse.getContent().isEmpty(), "삭제 후 content가 비어있어야 합니다.");
  }
}
