package com.sprint.mission.discodeit.user.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sprint.mission.discodeit.binaryContent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.user.dto.response.UserResponse;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.entity.UserStatus;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.user.service.UserStatusService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Slf4j
@Tag(name = "User", description = "회원 관련 API")
public class UserController {
	private final UserService userService;
	private final UserStatusService userStatusService;
	private final UserMapper userMapper;

	//사용자 등록
	@Operation(summary = "사용자 등록", description = "회원가입 API")
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<User> create(
		@RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
		@RequestPart(value = "profile", required = false) MultipartFile profile) {
		Optional<BinaryContentCreateRequest> profileRequest;
		if (profile != null) {
			profileRequest = resolveProfileRequest(profile);
		} else {
			profileRequest = Optional.empty();
		}
		User createdUser = userService.createUser(userCreateRequest, profileRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}

	//전체 사용자 조회
	@Operation(summary = "전체 사용자 조회", description = "전체 사용자 조회 API")
	@GetMapping(value = "")
	public ResponseEntity<List<UserResponse>> findAll() {
		List<UserResponse> users = userService.findAllUsers();
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}

	//사용자 수정
	@Operation(summary = "사용자 수정", description = "특정 사용자 수정 API")
	@PutMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<User> update(@RequestParam("userId") UUID userId,
		@RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
		@RequestPart(value = "profile", required = false) MultipartFile profile) {
		Optional<BinaryContentCreateRequest> profileRequest;
		if (profile != null) {
			profileRequest = resolveProfileRequest(profile);
		} else {
			profileRequest = Optional.empty();
		}
		User updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
		return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
	}

	//사용자 삭제
	@Operation(summary = "사용자 삭제", description = "특정 사용자 삭제 API")
	@DeleteMapping(value = "/{userId}")
	public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
		userService.delete(userId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	//사용자 온라인 상태 업데이트
	@Operation(summary = "사용자 상태 업데이트", description = "특정 사용자의 온라인 상태 업데이트 API")
	@PutMapping(value = "")
	public ResponseEntity<UserStatus> updateUserStatusByUserId(@RequestParam("userId") UUID userId,
		@RequestBody UserStatusUpdateRequest request) {
		UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, request);
		return ResponseEntity.status(HttpStatus.OK).body(updatedUserStatus);
	}

	private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
		if (profileFile.isEmpty()) {
			return Optional.empty();
		} else {
			try {
				BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
					profileFile.getOriginalFilename(), profileFile.getContentType(), profileFile.getBytes());
				return Optional.of(binaryContentCreateRequest);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}