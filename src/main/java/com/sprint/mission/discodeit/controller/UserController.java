package com.sprint.mission.discodeit.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.global.dto.CommonResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;

@RestController
@RequestMapping("api/users")
public class UserController {

	private final UserService userService;
	private final UserStatusService userStatusService;

	public UserController(UserService userService, UserStatusService userStatusService) {
		this.userService = userService;
		this.userStatusService = userStatusService;
	}

	//사용자 등록
	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	public ResponseEntity<CommonResponse<UserResponse>> createUser(@RequestBody CreateUserRequest createUserRequest) {
		UserResponse newUser = userService.createUser(createUserRequest);
		return new ResponseEntity<>(CommonResponse.created(newUser), HttpStatus.CREATED);
	}

	//특정 사용자 조회
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<CommonResponse<UserResponse>> getUserById(@PathVariable("id") UUID userId) {
		UserResponse existUser = userService.findUser(userId);
		return new ResponseEntity<>(CommonResponse.created(existUser), HttpStatus.OK);
	}

	//전체 사용자 조회
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<CommonResponse<List<UserResponse>>> getAllUsers() {
		List<UserResponse> users = userService.findAllUsers();
		return new ResponseEntity<>(CommonResponse.success(users), HttpStatus.OK);
	}

	//사용자 수정
	@RequestMapping(value = "{id}", method = RequestMethod.PATCH)
	public ResponseEntity<CommonResponse<UserResponse>> updateUser(@PathVariable("id") UUID userId,
		UpdateUserRequest request) {
		UserResponse updatedUser = userService.updateUser(userId, request);
		return new ResponseEntity<>(CommonResponse.success(updatedUser), HttpStatus.OK);
	}

	//사용자 삭제
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<CommonResponse<Void>> deleteUser(@PathVariable("id") UUID userId) {
		userService.deleteUser(userId);
		return new ResponseEntity<>(CommonResponse.success("User deleted successfully", null), HttpStatus.OK);
	}

	//사용자 온라인 상태 업데이트
	@RequestMapping(value = "/update-status/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<CommonResponse<Void>> updateUserStatus(@PathVariable("id") UUID userId,
		@RequestBody UpdateUserStatusRequest request) {
		userStatusService.update(request);
		return new ResponseEntity<>(CommonResponse.success("User status updated", null), HttpStatus.OK);
	}
}
