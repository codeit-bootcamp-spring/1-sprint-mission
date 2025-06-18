package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Notification", description = "알림 API")
public interface NotificationApi {

    @Operation(summary = "사용자 알림 목록 조회")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "알림 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotificationDto.class)))
        ),
        @ApiResponse(
            responseCode = "401", description = "인증되지 않은 사용자",
            content = @Content(examples = @ExampleObject(value = "Unauthorized"))
        )
    })
    @GetMapping
    ResponseEntity<List<NotificationDto>> findAllByReceiverId(Authentication authentication);

    @Operation(summary = "알림 삭제 (알림 확인)")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", description = "알림 삭제 성공"
        ),
        @ApiResponse(
            responseCode = "404", description = "알림을 찾을 수 없음",
            content = @Content(examples = @ExampleObject(value = "Notification with id {notificationId} not found"))
        ),
        @ApiResponse(
            responseCode = "400", description = "다른 사용자의 알림 삭제 시도",
            content = @Content(examples = @ExampleObject(value = "Cannot delete other user's notification"))
        )
    })
    @DeleteMapping("{notificationId}")
    ResponseEntity<Void> delete(
        @Parameter(description = "삭제할 알림 ID") UUID notificationId,
        Authentication authentication
    );
}