package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(
        name = "User API",
        description = "유저 API"
)
public interface UserApi {

    @Operation(
            summary = "유저 등록"
    )
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = User.class)))
    ResponseEntity<User> create(UserCreateRequest userCreateRequest, MultipartFile profile);

    @Operation(summary = "유저 정보 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", content = @Content(
                            schema = @Schema(implementation = User.class)
                    )),
                    @ApiResponse(responseCode = "404", content = @Content(
                            examples = @ExampleObject(value = "UserStatus with userId {userId} not found")
                    ))
            }
    )
    ResponseEntity<User> update(UUID userId, UserUpdateRequest request, MultipartFile profile);

    @Operation(
            summary = "유저 정보 삭제",
            parameters = @Parameter(name = "userId", required = true, description = "삭제할 user ID",
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "404", content = @Content(
                            examples = @ExampleObject(value = "User with userId {userId} not found")
                    ))
            }
    )
    ResponseEntity<Void> delete(UUID userId);

    @Operation(summary = "모든 유저 조회")
    @ApiResponse(responseCode = "200", content = @Content(
            array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
    ))
    ResponseEntity<List<UserDto>> findAll();

    @Operation(
            summary = "유저 상태 변경",
            parameters = @Parameter(name = "userId", required = true, description = "수정할 User ID",
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            ),
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserStatusUpdateRequest.class)
                    )
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", content = @Content(
                            schema = @Schema(implementation = UserStatus.class)
                    )),
                    @ApiResponse(responseCode = "404", content = @Content(
                            examples = @ExampleObject(value = "UserStatus with userId {userId} not found")
                    ))
            }
    )
    ResponseEntity<UserStatus> updateUserStatusByUserId(UUID userId, UserStatusUpdateRequest request);
}
