package com.sprint.mission.controller;

import com.sprint.mission.common.CommonResponse;
import com.sprint.mission.common.exception.CustomErrorResponse;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.dto.request.UserDtoForUpdate;
import com.sprint.mission.dto.response.FindUserDto;
import com.sprint.mission.dto.response.SaveUserDto;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.service.UserService;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @Operation(summary = "User 등록", description = "Create User")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "409", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> create(
            @Parameter(description = "유저 생성을 위한 DTO") @RequestPart("createRequestDto") @Valid UserDtoForCreate requestDTO,
            @Parameter(description = "User 프로필 이미지") @RequestPart(value = "profile", required = false) MultipartFile profile) {
        User user = userService.create(requestDTO, profile);

        return CommonResponse.toResponseEntity
                (CREATED, "유저가 성공적으로 생성되었습니다.", new SaveUserDto(user));
    }

    @Operation(summary = "User 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User 정보가 성공적으로 수정됨",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CommonResponse> update(
            @Parameter(description = "수정할 User ID") @PathVariable("id") UUID userId,
            @RequestBody @Valid UserDtoForUpdate requestDTO) {

        userService.update(userId, requestDTO);
        return CommonResponse.toResponseEntity
                (OK, "성공적으로 업데이트되었습니다", requestDTO);
    }


    @Operation(summary = "User 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨", content = @Content()),
            @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<CommonResponse> delete(
            @Parameter(description = "삭제할 User ID")
            @PathVariable("id") UUID userId) {
        userService.delete(userId);
        return CommonResponse.toResponseEntityWithoutData
                (NO_CONTENT, "성공적으로 삭제되었습니다");
    }


    @Operation(summary = "User 온라인 상태 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨",
                    content = @Content(schema = @Schema(implementation = UserStatus.class))),
            @ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })

    @PatchMapping("{id}/status")
    public ResponseEntity<CommonResponse> updateStatusByUserId(
            @Parameter(description = "상태를 변경할 User ID")
            @PathVariable("id") UUID userId) {
        UserStatus userStatus = userStatusService.updateByUserId(userId);
        return CommonResponse.toResponseEntity
                (OK, "status updated Successfully", userStatus);
    }


    @Operation(summary = "전체 User 목록 조회")
    @ApiResponse(responseCode = "200", description = "User 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FindUserDto.class))))
    @GetMapping
    public ResponseEntity<CommonResponse> findAll() {
        Map<User, Boolean> statusMapByUser = userStatusService.findStatusMapByUserList();
        log.info("statusMapByUser : {}", statusMapByUser);

        // osiv 끌 시 LazyInitializationException 발생
        List<FindUserDto> findUserDtoList = statusMapByUser.keySet().stream()
                .map(user -> new FindUserDto(user, statusMapByUser.get(user)))
                .toList();

        return CommonResponse.toResponseEntity
                (OK, "유저 리스트 조회 성공", findUserDtoList);
    }
}
