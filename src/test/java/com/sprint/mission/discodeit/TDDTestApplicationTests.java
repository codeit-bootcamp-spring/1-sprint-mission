package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
public class TDDTestApplicationTests {
//
//  @Autowired
//  private TestRestTemplate restTemplate;
//  @Autowired
//  private UserService userService;
//
//  @MockBean
//  private InputHandler inputHandler; // Scanner 로 입력받는 게 있어서 Mocking
//
//  /**
//   * 사용자(User) 도메인 API
//   **/
//  @Test
//  @Transactional
//  // 데이터 상태 관리를 위해 @Transactional 애노테이션을 단다. --> 중간에 실패시 데이터 롤백
//  // TODO Service 로직 미구현
//  //  user_statuses 테이블을 구성할 때, user_id 는 NOTNULL 한 값인데, 해당 부분이 구현되어 있지 않다.
//  //  --> 때문에 상태 코드 201 대신 500 발생 중
//  void createUserTest()
//      throws Exception {
//    // JSON으로 보낼 데이터를 Map 형태로 생성
//    UserCreateRequest userCreateRequest = new UserCreateRequest(
//        "testUsername",
//        "test@example.com",
//        "testPassword"
//    );
//
//    BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
//        "Profile.jpg",
//        1024L,
//        "image/jpeg",
//        new byte[]{}
//    );
//
//    // JSON
//    ObjectMapper objectMapper = new ObjectMapper();
//    String userCreateRequestJson = objectMapper.writeValueAsString(userCreateRequest);
//    HttpHeaders userCreateRequestheaders = new HttpHeaders();
//    userCreateRequestheaders.setContentType(MediaType.APPLICATION_JSON);
//
//    // Multipart
//    MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
//    requestBody.add("userCreateRequest",
//        new HttpEntity<>(userCreateRequestJson, userCreateRequestheaders));
//    requestBody.add("binaryContent", binaryContentCreateRequest);
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody,
//        headers);
//
//    ResponseEntity<UserDto> response = restTemplate.exchange(
//        "/api/users",
//        HttpMethod.POST,
//        requestEntity,
//        UserDto.class
//    );
//
//    assertEquals(HttpStatus.CREATED, response.getStatusCode());
//    UserDto userDto = response.getBody();
//    assertNotNull(userDto);
//    assertEquals("testUsername", userDto.username());
//    assertEquals("test@example.com", userDto.email());
//  }
//
//  // TODO 수정
//  // TODO 삭제
//  // TODO 조회
//
//  /**
//   * 채널(Channel) 도메인 API
//   **/
//  @Test
//  @Transactional
//  @DisplayName("공개 채널 생성")
//  void createPublicChannelTest() throws Exception {
//    ChannelPublicRequest request = new ChannelPublicRequest("Test Channel", "channel Test");
//
//    ResponseEntity<ChannelDto> response = restTemplate.postForEntity(
//        "/api/channels/public",
//        request,
//        ChannelDto.class
//    );
//
//    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//    assertThat(response.getBody().name()).isEqualTo("Test Channel");
//    assertThat(response.getBody().description()).isEqualTo("channel Test");
//  }
//
//  // TODO 비공개 채널 생성
//
//  // TODO 채널 수정
//
//  @Test
//  @Transactional
//  @DisplayName("채널 삭제")
//  void deleteChannelTest() throws Exception {
//    given(inputHandler.getYesNOInput()).willReturn("y");
//
//    ChannelPublicRequest request = new ChannelPublicRequest("Test Channel", "channel Test");
//
//    ResponseEntity<ChannelDto> response = restTemplate.postForEntity(
//        "/api/channels/public",
//        request,
//        ChannelDto.class
//    );
//
//    UUID channelId = response.getBody().id();
//
//    ResponseEntity<Void> deleteResponse = restTemplate.exchange(
//        "/api/channels/" + channelId,
//        HttpMethod.DELETE,
//        null,
//        Void.class);
//
//    assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//  }
//
//  /**
//   * 메세지(Message) 도메인 API
//   **/
//  // TODO 생성
//  @Test
//  @Transactional
//  // TODO 유저 문제가 해결되어야 한다.
//  void updateMessageTest() throws Exception {
//    // 채널
//    ChannelPublicRequest channelRequest = new ChannelPublicRequest("Test Channel", "채널 설명");
//    ResponseEntity<ChannelDto> channelResponse = restTemplate.postForEntity(
//        "/api/channels/public",
//        channelRequest,
//        ChannelDto.class
//    );
//
//    assertThat(channelResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//    UUID channelId = channelResponse.getBody().id();
//
//    // 사용자
//    UserCreateRequest userRequest = new UserCreateRequest("testUser", "test@example.com",
//        "testPassword");
//
//    BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
//        "Profile.jpg",
//        1024L,
//        "image/jpeg",
//        new byte[]{}
//    );
//
//    // JSON
//    ObjectMapper objectMapper = new ObjectMapper();
//    String userCreateRequestJson = objectMapper.writeValueAsString(userRequest);
//    HttpHeaders userCreateRequestheaders = new HttpHeaders();
//    userCreateRequestheaders.setContentType(MediaType.APPLICATION_JSON);
//
//    // Multipart
//    MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
//    requestBody.add("userCreateRequest",
//        new HttpEntity<>(userCreateRequestJson, userCreateRequestheaders));
//    requestBody.add("binaryContent", binaryContentCreateRequest);
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody,
//        headers);
//
//    ResponseEntity<UserDto> userResponse = restTemplate.exchange(
//        "/api/users",
//        HttpMethod.POST,
//        requestEntity,
//        UserDto.class
//    );
//    assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//    UUID authorId = userResponse.getBody().id();
//
//    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(
//        channelId,
//        authorId,
//        "Original Message Content"
//    );
//
//    ResponseEntity<MessageDto> messageResponse = restTemplate.postForEntity("/api/messages",
//        messageCreateRequest,
//        MessageDto.class);
//    UUID messageId = messageResponse.getBody().id();
//
//    // 메세지 수정 요청
//    MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest("Test Update Message");
//
//    ResponseEntity<MessageDto> updateResponse = restTemplate.exchange("/api/messages/" + messageId,
//        HttpMethod.PATCH,
//        new HttpEntity<>(messageUpdateRequest),
//        MessageDto.class
//    );
//
//    assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//    assertThat(updateResponse.getBody().content()).isEqualTo("Test Update Message");
//  }
//
//  // TODO 삭제
//
//  // TODO 조회


}
