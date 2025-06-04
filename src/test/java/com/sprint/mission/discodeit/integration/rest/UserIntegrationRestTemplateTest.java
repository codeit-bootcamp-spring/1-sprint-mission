package com.sprint.mission.discodeit.integration.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.AuthRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import java.util.UUID;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserIntegrationRestTemplateTest {

    private final RestTemplate restTemplate = new RestTemplate(
            new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault())
    );
    @LocalServerPort
    private int port;
    @Autowired
    private ObjectMapper objectMapper;

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
    void 사용자_시나리오_통합테스트() throws Exception {
        // 1. 회원가입
        UserCreateRequestDto request = new UserCreateRequestDto("홍길동", "hong@test.com", "1234");
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("userCreateRequest", toJsonResource("request.json", request));
        body.add("profile", new ByteArrayResource("dummy".getBytes()) {
            @Override
            public String getFilename() {
                return "profile.png";
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<UserDto> response = restTemplate.postForEntity(getBaseUrl("/api/users"),
                requestEntity, UserDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID userId = response.getBody().getId();

        // 2. 로그인 (홍길동)
        AuthRequestDto loginRequest = new AuthRequestDto("홍길동", "1234");
        ResponseEntity<UserDto> loginResponse = restTemplate.postForEntity(
                getBaseUrl("/api/auth/login"), loginRequest, UserDto.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 3. 유저 정보 수정 (김철수)
        UserUpdateRequestDto updateRequest = new UserUpdateRequestDto("김철수", "kim@test.com",
                "1234");
        MultiValueMap<String, Object> updateBody = new LinkedMultiValueMap<>();
        updateBody.add("userUpdateRequest", toJsonResource("request.json", updateRequest));
        updateBody.add("profile", new ByteArrayResource("dummy2".getBytes()) {
            @Override
            public String getFilename() {
                return "updated.png";
            }
        });

        HttpHeaders updateHeaders = new HttpHeaders();
        updateHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> updateEntity = new HttpEntity<>(updateBody,
                updateHeaders);

        ResponseEntity<UserDto> updateResponse = restTemplate.exchange(
                getBaseUrl("/api/users/" + userId), HttpMethod.PATCH, updateEntity, UserDto.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getUsername()).isEqualTo("김철수");
        assertThat(updateResponse.getBody().getEmail()).isEqualTo("kim@test.com");

        // 4. 수정된 정보로 로그인
        AuthRequestDto updatedLoginRequest = new AuthRequestDto("김철수", "1234");
        ResponseEntity<UserDto> updatedLoginResponse = restTemplate.postForEntity(
                getBaseUrl("/api/auth/login"), updatedLoginRequest, UserDto.class);
        assertThat(updatedLoginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 5. 회원 탈퇴
        restTemplate.delete(getBaseUrl("/api/users/" + userId));

        // 6. 삭제된 사용자로 로그인 시도 → 실패
        AuthRequestDto failedLoginRequest = new AuthRequestDto("김철수", "1234");
        try {
            restTemplate.postForEntity(getBaseUrl("/api/auth/login"), failedLoginRequest,
                    String.class);
            fail("예외가 발생해야 합니다."); // 예외가 발생하지 않으면 테스트 실패
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(e.getResponseBodyAsString()).contains("존재하지 않는 사용자입니다.");
        }
    }
}
