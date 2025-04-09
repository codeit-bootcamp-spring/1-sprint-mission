package com.sprint.mission.discodeit.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController implements UserApi {

	private final UserService userService;
	private final UserStatusService userStatusService;

	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@Override
	public ResponseEntity<UserDto> create(
		@Valid @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
		@RequestPart(value = "profile", required = false) MultipartFile profile
	) {
		log.info("Received request to create user: username={}", userCreateRequest.username());
		Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
			.flatMap(this::resolveProfileRequest);
		UserDto createdUser = userService.create(userCreateRequest, profileRequest);
		log.info("User creation request successful: id={}", createdUser.id());
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(createdUser);
	}

	@PatchMapping(path = "{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@Override
	public ResponseEntity<UserDto> update(
		@PathVariable("userId") UUID userId,
		@Valid @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
		@RequestPart(value = "profile", required = false) MultipartFile profile
	) {
		log.info("Received request to update user: id={}", userId);
		Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
			.flatMap(this::resolveProfileRequest);
		UserDto updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
		log.info("User update request successful: id={}", updatedUser.id());
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(updatedUser);
	}

	@DeleteMapping(path = "{userId}")
	@Override
	public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
		log.info("Received request to delete user: id={}", userId);
		userService.delete(userId);
		log.info("User deletion request successful: id={}", userId);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

	@GetMapping
	@Override
	public ResponseEntity<List<UserDto>> findAll() {
		List<UserDto> users = userService.findAll();
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(users);
	}

	@PatchMapping(path = "{userId}/userStatus")
	@Override
	public ResponseEntity<UserStatusDto> updateUserStatusByUserId(@PathVariable("userId") UUID userId,
		@RequestBody UserStatusUpdateRequest request) {
		UserStatusDto updatedUserStatus = userStatusService.updateByUserId(userId, request);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(updatedUserStatus);
	}

	private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
		if (profileFile.isEmpty()) {
			return Optional.empty();
		} else {
			try {
				BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
					profileFile.getOriginalFilename(),
					profileFile.getContentType(),
					profileFile.getBytes()
				);
				return Optional.of(binaryContentCreateRequest);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
