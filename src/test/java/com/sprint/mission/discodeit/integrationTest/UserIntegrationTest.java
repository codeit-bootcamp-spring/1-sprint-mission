package com.sprint.mission.discodeit.integrationTest;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
		userRepository.save(testUser);
	}

	@Test
	@Transactional
	void 사용자_생성() {
		// given
		UserCreateRequest userCreateRequest = new UserCreateRequest("new_user", "newuser@example.com", "password123");

		// when
		ResponseEntity<UserDto> response = testRestTemplate.postForEntity("/api/users", userCreateRequest,
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
	@Transactional
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
