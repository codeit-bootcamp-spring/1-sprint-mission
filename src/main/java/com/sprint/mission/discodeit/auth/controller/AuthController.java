package com.sprint.mission.discodeit.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.auth.dto.LoginUserRequest;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.global.dto.CommonResponse;
import com.sprint.mission.discodeit.user.dto.response.UserResponse;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.entity.UserStatus;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.service.UserStatusService;

@RestController
@RequestMapping("api/auth")
public class AuthController {

	private final AuthService authService;
	private final UserStatusService userStatusService;
	private final UserMapper userMapper;

	public AuthController(AuthService authService, UserStatusService userStatusService, UserMapper userMapper) {
		this.authService = authService;
		this.userStatusService = userStatusService;
		this.userMapper = userMapper;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<CommonResponse<UserResponse>> login(@RequestBody LoginUserRequest request) {
		User loginUser = authService.login(request);
		UserStatus userStatus = userStatusService.find(loginUser.getId());

		UserResponse response = userMapper.userToUserResponse(loginUser, userStatus);
		return new ResponseEntity<>(CommonResponse.success("Login successful", response), HttpStatus.OK);
	}
}
