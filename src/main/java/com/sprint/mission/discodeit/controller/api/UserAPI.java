package com.sprint.mission.discodeit.controller.api;
import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.dto.user.userStatus.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "User", description = "사용자 API")
public interface UserAPI {

    @Operation(summary = "User Create")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "User가 성공적으로 등록되었습니다.",
                    content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "해당 userEmail 또는 username를 사용하는 User가 이미 존재합니다.",
                    content = @Content(examples = @ExampleObject(value = "{userEmail} already exists"))
            ),
    })
    ResponseEntity<User> createUser(
            @Parameter(
                    description = "User Create Info",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ) UserCreate userCreateRequest,
            @Parameter(
                    description = "User Profile Image",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ) MultipartFile profile
    );

    @Operation(summary = "User 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User 정보가 성공적으로 수정되었습니다.",
                    content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 User를 찾을 수 없습니다.",
                    content = @Content(examples = @ExampleObject("{userId} not found"))
            ),
            @ApiResponse(
                    responseCode = "400", description = "해당 userEmail 또는 username를 사용하는 User가 이미 존재합니다.",
                    content = @Content(examples = @ExampleObject("{newUserEmail} already exists"))
            )
    })
    ResponseEntity<User> updateUser(
            @Parameter(description = "수정할 UserId") UUID userId,
            @Parameter(description = "수정할 User Info") UserUpdate userUpdateRequest,
            @Parameter(description = "수정할 User 프로필 이미지") MultipartFile profile
    );

    @Operation(summary = "User 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User가 성공적으로 삭제됨"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "{userId} not found"))
            )
    })
    ResponseEntity<Void> deleteUser(
            @Parameter(description = "삭제할 userID") UUID userId
    );

    @Operation(summary = "전체 User 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User 목록 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
            )
    })
    ResponseEntity<List<UserDto>> getAllUsers();

    @Operation(summary = "User Online Status Update")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트되었습니다.",
                    content = @Content(schema = @Schema(implementation = UserStatus.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없습니다.",
                    content = @Content(examples = @ExampleObject(value = "{userId} not found"))
            )
    })
    ResponseEntity<UserStatus> updateUserStatus(
            @Parameter(description = "상태를 변경할 userID") UUID userId,
            @Parameter(description = "변경할 User 온라인 상태 정보") UserStatusUpdate request
    );
}
