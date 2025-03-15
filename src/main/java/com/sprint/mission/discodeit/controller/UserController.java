package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "User", description = "User API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;


    private BinaryContentCreateRequest resolveProfileRequest(MultipartFile profile) {
        try {
            BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
                    profile.getOriginalFilename(),
                    profile.getContentType(),
                    profile.getBytes()
            );
            return binaryContentCreateRequest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "전체 User 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User 목록 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
            )
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> users = userService.readAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    @Operation(summary = "User 등록")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "User가 성공적으로 생성됨",
                    content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
                    content = @Content(examples = @ExampleObject(value = "User with email {email} already exists"))
            ),
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> create(@Parameter(
            description = "User 생성 정보",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    ) @RequestPart (value = "userCreateRequest") UserCreateRequest userCreateRequest, @Parameter(
            description = "User 프로필 이미지",
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
    ) @RequestPart(value = "profile", required = false) MultipartFile profile) {
        BinaryContentCreateRequest profileRequest = Optional.ofNullable(profile)
                .map(this::resolveProfileRequest).orElse(null);

        User createdUser = userService.createUser(userCreateRequest, profileRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    } //param으로 optional을 쓰면X -> null check(isPresent)가 필요해서? -> ofNullable + 함수형느낌(by .map)으로 작성

    @Operation(summary = "User 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User 정보가 성공적으로 수정됨",
                    content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "User를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject("User with id {userId} not found"))
            ),
            @ApiResponse(
                    responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
                    content = @Content(examples = @ExampleObject("user with email {newEmail} already exists"))
            )
    })
    @PatchMapping(path = "{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> update(@Parameter(description = "수정할 User ID") @PathVariable("userId") UUID userId, @Parameter(description = "수정할 User 정보") @RequestPart("userUpdateRequest")UserUpdateRequest userUpdateRequest, @Parameter(description = "수정할 User 프로필 이미지") @RequestPart (value = "profile", required = false) MultipartFile profile) {
//        Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();
//        if (profile != null) {
//            profileRequest = resolveProfileRequest(profile);
//        } 전형적인 optional 장점 못살리는 코드. (이렇게 하려면 resolveProfileRequest 반환형 Optional<T>로 변화 필요)
        BinaryContentCreateRequest profileRequest = Optional.ofNullable(profile)
                .map(this::resolveProfileRequest).orElse(null);

        User updatedUser = userService.updateUserField(userId, userUpdateRequest, profileRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @Operation(summary = "User 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User가 성공적으로 삭제됨"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "User with id {id} not found"))
            )
    })
    @DeleteMapping(path = "{userId}")
    public ResponseEntity<Void> delete(@Parameter(description = "삭제할 User ID") @PathVariable("userId") UUID userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "User 온라인 상태 업데이트")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨",
                    content = @Content(schema = @Schema(implementation = UserStatus.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "UserStatus with userId {userId} not found"))
            )
    })
    @PatchMapping(path = "{userId}/userStatus")
    public ResponseEntity<UserStatus> updateUserStatusByUserId(@Parameter(description = "상태를 변경할 User ID") @PathVariable("userId") UUID userId,
                                                               @Parameter(description = "변경할 User 온라인 상태 정보") @RequestBody UserStatusUpdateRequest request) {
        UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUserStatus);
    }




}


