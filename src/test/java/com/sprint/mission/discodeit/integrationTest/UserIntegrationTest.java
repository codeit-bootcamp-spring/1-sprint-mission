package com.sprint.mission.discodeit.integrationTest;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UserRepository userRepository;

	private User testUser;

	@BeforeEach
	void setUp() {
		// 테스트용 사용자 초기화
		testUser = new User("john_doe", "john.doe@example.com", "password123", null);
		userRepository.saveAndFlush(testUser);
	}

	@Test
	@Transactional
	void 사용자_생성() {
		// given
		UserCreateRequest userCreateRequest = new UserCreateRequest("new_user", "newuser@example.com", "password123");

		// multipart/form-data 요청 구성을 위한 MultiValueMap 생성
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

		// JSON 데이터를 multipart 요청의 한 파트로 추가 (직렬화 필요)
		// Spring에서는 일반 객체를 자동 직렬화하지 않으므로, JSON 문자열로 변환하거나, HttpEntity를 사용해서 명시적으로 Content-Type을 선언
		HttpHeaders jsonHeaders = new HttpHeaders();
		jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserCreateRequest> jsonPart = new HttpEntity<>(userCreateRequest, jsonHeaders);

		body.add("userCreateRequest", jsonPart);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		// when
		ResponseEntity<UserDto> response = testRestTemplate.exchange("/api/users", HttpMethod.POST, requestEntity,
			UserDto.class);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().username()).isEqualTo("new_user");
	}

/*	@Test
	@Transactional
	void 사용자_수정() {
		// given
		String newUsername = "updated_user";
		String newEmail = "updateduser@example.com";
		UserUpdateRequest userUpdateRequest = new UserUpdateRequest(newUsername, newEmail, "newpassword123");

		HttpEntity<UserUpdateRequest> requestEntity = new HttpEntity<>(userUpdateRequest);

		// when
		ResponseEntity<UserDto> response = testRestTemplate.exchange(
			"http://localhost:" + port + "/api/users/{userId}",
			HttpMethod.PATCH,
			requestEntity,
			UserDto.class,
			testUser.getId()
		);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().username()).isEqualTo(newUsername);
	}*/

	@Test
	void 사용자_삭제() {
		// given
		UUID userId = testUser.getId();

		// when
		ResponseEntity<Void> response = testRestTemplate.exchange(
			"/api/users/{userId}", HttpMethod.DELETE, null, Void.class, userId
		);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(userRepository.findById(userId)).isEmpty();
	}

}
