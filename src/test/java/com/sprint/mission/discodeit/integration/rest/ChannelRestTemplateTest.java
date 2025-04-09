package com.sprint.mission.discodeit.integration.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.AuthRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import java.util.List;
import java.util.UUID;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChannelRestTemplateTest {

  @LocalServerPort
  private int port;

  @Autowired
  private ObjectMapper objectMapper;

  private final RestTemplate restTemplate = new RestTemplate(
      new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault())
  );

  private String getBaseUrl(String path) {
    return "http://localhost:" + port + path;
  }

  private HttpEntity<Resource> toJsonResource(String name, Object dto) throws Exception {
    byte[] json = objectMapper.writeValueAsBytes(dto);
    Resource resource = new ByteArrayResource(json) {
      @Override
      public String getFilename() {
        return name;
      }
    };
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(resource, headers);
  }

  @Test
  @Transactional
  void 사용자_채널_통합테스트() throws Exception {
    // 회원가입
    UserCreateRequestDto signupRequest = new UserCreateRequestDto("홍길동", "hong@test.com", "1234");
    MultiValueMap<String, Object> signupBody = new LinkedMultiValueMap<>();
    signupBody.add("userCreateRequest", toJsonResource("request.json", signupRequest));
    signupBody.add("profile", new ByteArrayResource("dummy".getBytes()) {
      @Override
      public String getFilename() {
        return "profile.png";
      }
    });
    HttpHeaders signupHeaders = new HttpHeaders();
    signupHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
    HttpEntity<MultiValueMap<String, Object>> signupEntity = new HttpEntity<>(signupBody,
        signupHeaders);
    ResponseEntity<UserDto> createdUserResp = restTemplate.postForEntity(getBaseUrl("/api/users"),
        signupEntity, UserDto.class);
    UUID userId = createdUserResp.getBody().getId();

    // 로그인
    AuthRequestDto loginRequest = new AuthRequestDto("홍길동", "1234");
    ResponseEntity<UserDto> loginResp = restTemplate.postForEntity(getBaseUrl("/api/auth/login"),
        loginRequest, UserDto.class);
    assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);

    // 비공개 채널 생성
    PrivateChannelCreateRequestDto privateChannelRequest = new PrivateChannelCreateRequestDto(
        List.of(userId));
    ResponseEntity<ChannelDto> privateChannelResp = restTemplate.postForEntity(
        getBaseUrl("/api/channels/private"),
        new HttpEntity<>(privateChannelRequest, getJsonHeader()),
        ChannelDto.class);
    assertThat(privateChannelResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    // 공개 채널 2개 생성 후 전체 조회
    ChannelDto ch1 = restTemplate.postForEntity(
        getBaseUrl("/api/channels/public"),
        new HttpEntity<>(new PublicChannelCreateRequestDto("channel-1", "desc-1"), getJsonHeader()),
        ChannelDto.class).getBody();

    ChannelDto ch2 = restTemplate.postForEntity(
        getBaseUrl("/api/channels/public"),
        new HttpEntity<>(new PublicChannelCreateRequestDto("channel-2", "desc-2"), getJsonHeader()),
        ChannelDto.class).getBody();

    ChannelDto[] allChannels = restTemplate.getForEntity(
        getBaseUrl("/api/channels?userId=" + userId), ChannelDto[].class).getBody();
    assertThat(allChannels.length).isGreaterThanOrEqualTo(2);

    // 채널 1 수정
    ChannelUpdateRequestDto update = new ChannelUpdateRequestDto("channel-1-edited",
        "desc-1-edited");
    ResponseEntity<ChannelDto> updatedResp = restTemplate.exchange(
        getBaseUrl("/api/channels/" + ch1.getId()),
        HttpMethod.PATCH,
        new HttpEntity<>(update, getJsonHeader()),
        ChannelDto.class);
    assertThat(updatedResp.getBody().getName()).isEqualTo("channel-1-edited");

    // 채널 2 삭제
    restTemplate.delete(getBaseUrl("/api/channels/" + ch2.getId()));
    ChannelDto[] remaining = restTemplate.getForEntity(getBaseUrl("/api/channels?userId=" + userId),
        ChannelDto[].class).getBody();
    assertThat(remaining.length).isEqualTo(2); // public 1 + private 1
  }

  private HttpHeaders getJsonHeader() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
