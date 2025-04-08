package com.sprint.mission.discodeit.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
class UserControllerIntegrationTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void 클라이언트가_유저_생성을_요청하면_응답으로_유저를_반환한다() throws Exception {
		// given
		UserCreateRequest userCreateRequest = new UserCreateRequest("유저", "email@email.com", "1234");
		String json = objectMapper.writeValueAsString(userCreateRequest);

		HttpHeaders jsonHeaders = new HttpHeaders();
		jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> jsonEntity = new HttpEntity<>(json, jsonHeaders);

		MultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
		request.add("userCreateRequest", jsonEntity);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(request, headers);

		// when
		ResponseEntity<UserDto> response = testRestTemplate.postForEntity("/api/users", requestEntity, UserDto.class);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody())
			.isNotNull()
			.extracting("username")
			.isEqualTo("유저");
	}

	@Test
	@Sql(scripts = {"/controller/integration_already_exist_email.sql"},
		config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
	@Sql(statements = "DELETE FROM users WHERE email = 'email@email.com';", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
		config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
	public void 클라이언트가_이미_존재하는_이메일로_유저_생성을_요청하면_400_에러를_내려준다() throws Exception {
		// given
		String existingEmail = "email@email.com";
		UserCreateRequest userCreateRequest = new UserCreateRequest("유저1", existingEmail, "1234");
		String json = objectMapper.writeValueAsString(userCreateRequest);

		HttpHeaders jsonHeaders = new HttpHeaders();
		jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> jsonEntity = new HttpEntity<>(json, jsonHeaders);

		MultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
		request.add("userCreateRequest", jsonEntity);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(request, headers);

		// when
		ResponseEntity<UserDto> response = testRestTemplate.postForEntity("/api/users", requestEntity,
			UserDto.class);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

}