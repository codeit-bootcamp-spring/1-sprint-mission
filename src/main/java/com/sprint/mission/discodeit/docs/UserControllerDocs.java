package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.common.CustomApiResponse;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.exception.ExceptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "Users", description = "User 관련 API")
public interface UserControllerDocs {

    @RequestBody(content = @Content(
            encoding = @Encoding(name = "user", contentType = MediaType.APPLICATION_JSON_VALUE)))
    @Operation(summary = "유저 생성", description = "유저를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "유저 생성 및 저장 성공",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "유저 생성 실패(유저 중복)",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 값입니다.",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class))
            )
    })
    CustomApiResponse<UserResponseDto> createUser(
            @RequestPart("user") @Schema(format = "application/json") UserSignupRequestDto reqUserDto,
            @RequestPart(value = "file", required = false) MultipartFile profile
    ) throws IOException;

    @Operation(summary = "전체 유저 조회", description = "전체 유저 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "User 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class))))
    CustomApiResponse<List<UserResponseDto>> getAllUsers();

    @Operation(summary = "단일 유저 조회", description = "특정 유저 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 조회 성공",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "유저 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<UserResponseDto> getUser(@PathVariable UUID userId);

    @RequestBody(content = @Content(
            encoding = @Encoding(name = "update", contentType = MediaType.APPLICATION_JSON_VALUE)))
    @Operation(summary = "유저 수정", description = "특정 유저 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 수정 성공",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "유저 수정 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<UserResponseDto> updateUser(
            @PathVariable UUID userId,
            @RequestPart("update") @Schema(format = "application/json") UserUpdateDto userUpdateDto,
            @RequestPart(value = "file", required = false) MultipartFile updateProfile
    );

    @Operation(summary = "유저 삭제", description = "특정 유저 정보를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 삭제 성공",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "유저 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<UserResponseDto> deleteUser(@PathVariable UUID userId);

    @Operation(summary = "유저 상태 업데이트", description = "유저의 상태(접속 시간 등)를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 상태 업데이트 성공",
                    content = @Content(schema = @Schema(implementation = UserStatusResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "유저 상태 업데이트 실패",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<UserStatusResponseDto> updateUserStatus(
            @PathVariable UUID userId,
            @RequestParam("accessAt") UserStatusRequestDto userStatusRequestDto);

}
