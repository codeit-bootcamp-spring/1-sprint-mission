package com.sprint.mission.discodeit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	/*public ResponseEntity<CommonResponse<?>> login(@RequestBody Create loginRequestDto) {
		try {
			log.info("일반 로그인 시도 - userId: {}", loginRequestDto.getUserId());
			LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
			log.info("로그인 성공 - userId: {}", loginRequestDto.getUserId());
			CommonResponse<?> response = CommonResponse.success("로그인 성공", loginResponseDto);
			log.info("로그인 응답 생성: {}", response);
			return ResponseEntity.ok(response);
		} catch (AuthenticationException e) {
			log.error("인증 실패 - userId: {}, error: {}", loginRequestDto.getUserId(), e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(CommonResponse.fail(e.getMessage()));
		} catch (Exception e) {
			log.error("로그인 처리 중 예외 발생 - userId: {}", loginRequestDto.getUserId(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(CommonResponse.fail("로그인 중 오류가 발생했습니다."));
		}
	}*/
}
