package com.sprint.mission.controller;

import com.sprint.mission.common.CommonResponse;
import com.sprint.mission.common.exception.CustomErrorResponse;
import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.request.BinaryContentDto;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.dto.request.UserDtoForUpdate;
import com.sprint.mission.dto.response.FindUserDto;
import com.sprint.mission.dto.response.SaveUserDto;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.service.UserService;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import com.sprint.mission.service.jcf.main.JCFUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "USER API")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @Operation(summary = "User 등록", description = "Create User")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "유저가 성공적으로 생성되었습니다.",
                    content = @Content(schema = @Schema(implementation = SaveUserDto.class))),
            @ApiResponse(responseCode = "409", description = "이메일 또는 이름 중복",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> create(
            @Parameter(description = "유저 생성을 위한 DTO") @RequestPart("createRequestDto") UserDtoForCreate requestDTO,
            @Parameter(description = "유저 프로필 ") @RequestPart(value = "profile", required = false) MultipartFile profile) {
        Optional<BinaryContentDto> binaryContentDto = BinaryContentDto.fileToBinaryContentDto(profile);
        User user = userService.create(requestDTO, binaryContentDto);
        return CommonResponse.toResponseEntity
                (CREATED, "유저가 성공적으로 생성되었습니다.", SaveUserDto.fromEntity(user));
    }


    @Operation(summary = "User 정보 수정", description = "Create User")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 업데이트되었습니다"),
            @ApiResponse(responseCode = "409", description = "이메일 또는 이름 중복",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PatchMapping(path = "{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> update(
            // @Parameter(description = "User ID")
           @PathVariable("id") UUID userId,
            @RequestPart("updateRequestDto") UserDtoForUpdate requestDTO) {

        userService.update(userId, requestDTO);
        return CommonResponse.toResponseEntity
                (OK, "성공적으로 업데이트되었습니다", requestDTO);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<CommonResponse> delete(@PathVariable("id") UUID userId) {
        userService.delete(userId);
        return CommonResponse.toResponseEntity
                (NO_CONTENT, "성공적으로 삭제되었습니다", null);
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<CommonResponse> updateStatusByUserId(@PathVariable("id") UUID userId) {
        UserStatus userStatus = userStatusService.updateByUserId(userId);
        return CommonResponse.toResponseEntity
                (OK, "status updated Successfully", userStatus);
    }


    @GetMapping
    public ResponseEntity<CommonResponse> findAll() {
        Map<User, Boolean> statusMapByUser = userStatusService.findStatusMapByUserList();
        log.info("statusMapByUser : {}", statusMapByUser);
        List<FindUserDto> findUserDtos = statusMapByUser.keySet().stream()
                .map(user -> {
                    return FindUserDto.fromEntityAndStatus(user, statusMapByUser.get(user));
                }).toList();

        return CommonResponse.toResponseEntity
                (OK, "유저 리스트 조회 성공", findUserDtos);
    }
}
