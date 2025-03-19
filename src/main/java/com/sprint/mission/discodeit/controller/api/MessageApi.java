package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "Message", description = "메시지 API")
public interface MessageApi {

    @Operation(summary = "메시지 생성", description = "메시지를 생성합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "메시지 생성 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MessageDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "메시지 생성 실패 - 존재하지 않는 유저나 채널"
    )
    ResponseEntity<MessageDto> createMessage(CreateMessageRequestDto createMessageRequestDto) throws IOException;

    @Operation(summary = "메시지 수정", description = "메시지를 수정합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "메시지 수정 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MessageDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "메시지 수정 실패 - 존재하지 않는 메시지"
    )
    ResponseEntity<MessageDto> updateMessage(UUID id, String context);

    @Operation(summary = "메시지 삭제", description = "메시지를 삭제합니다.")
    @ApiResponse(
            responseCode = "204",
            description = "메시지 삭제 성공"
    )
    @ApiResponse(
            responseCode = "404",
            description = "메시지 삭제 실패 - 존재하지 않는 메시지"
    )
    ResponseEntity<Void> deleteMessage(UUID id);

    @Operation(summary = "메시지 다건 조회", description = "해당 유저가 작성한 모든 메시지를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "메시지 다건 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MessageDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "메시지 삭제 실패 - 존재하지 않는 유저"
    )
    ResponseEntity<List<MessageDto>> findMessage(UUID userId);
}
