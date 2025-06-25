package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "Notification", description = "알림 API")
public interface NotificationApi {

    @Operation(summary = "알림 목록 조회")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "알림 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationDto.class)))
        ),
        @ApiResponse(
            responseCode = "401", description = "인증되지 않은 요청",
            content = @Content(schema = @Schema(hidden = true))
        )
    })
    ResponseEntity<List<NotificationDto>> findAllByReceiverId(
        @Parameter(hidden = true) DiscodeitUserDetails principal
    );

    @Operation(summary = "알림 삭제 (알림 확인)")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", description = "알림 삭제 성공",
            content = @Content(schema = @Schema(hidden = true))
        ),
        @ApiResponse(
            responseCode = "401", description = "인증되지 않은 요청",
            content = @Content(schema = @Schema(hidden = true))
        ),
        @ApiResponse(
            responseCode = "404", description = "알림을 찾을 수 없음",
            content = @Content(schema = @Schema(hidden = true))
        )
    })
    ResponseEntity<Void> delete(
        @Parameter(hidden = true) DiscodeitUserDetails principal,
        @Parameter(description = "알림 ID") UUID notificationId
    );
} 