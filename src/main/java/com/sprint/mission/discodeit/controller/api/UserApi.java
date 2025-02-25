package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
    ResponseEntity<User> create(UserCreateRequest userCreateRequest, MultipartFile profile);

    @Operation(summary = "유저 정보 수정")
    ResponseEntity<User> update(UUID userId, UserUpdateRequest request, MultipartFile profile);

    @Operation(
            summary = "유저 정보 삭제",
            parameters = @Parameter(name = "userId", required = true, description = "삭제할 user ID",
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
    )
    ResponseEntity<Void> delete(UUID userId);

    @Operation(summary = "모든 유저 조회")
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
    ResponseEntity<UserStatus> updateUserStatusByUserId(UUID userId, UserStatusUpdateRequest request);
}
