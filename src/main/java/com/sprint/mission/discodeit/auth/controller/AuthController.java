package com.sprint.mission.discodeit.auth.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.auth.dto.LoginUserRequest;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.global.dto.CommonResponse;
import com.sprint.mission.discodeit.message.entity.BinaryContent;
import com.sprint.mission.discodeit.message.service.BinaryContentService;
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
	private final BinaryContentService binaryContentService;
	private final UserMapper userMapper;

	public AuthController(AuthService authService, UserStatusService userStatusService,
		BinaryContentService binaryContentService, UserMapper userMapper) {
		this.authService = authService;
		this.userStatusService = userStatusService;
		this.binaryContentService = binaryContentService;
		this.userMapper = userMapper;
	}

	@PostMapping(value = "/login")
	public ResponseEntity<CommonResponse<UserResponse>> login(@RequestBody LoginUserRequest request) {
		User loginUser = authService.login(request);
		UserStatus userStatus = userStatusService.find(loginUser.getId());
		Optional<BinaryContent> profile = binaryContentService.findProfileImageByAuthorId(loginUser.getId());

		UserResponse response = userMapper.userToUserResponse(loginUser, userStatus, profile);
		return new ResponseEntity<>(CommonResponse.success("Login successful", response), HttpStatus.OK);
	}
}
