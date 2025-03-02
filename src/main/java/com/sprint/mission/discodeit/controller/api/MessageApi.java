package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
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
        name = "Message",
        description = "메시지 API"
)
public interface MessageApi {

    @Operation(summary = "메시지 요청과 첨부 파일 생성")
    @ApiResponse(responseCode = "201", content = @Content(
            schema = @Schema(implementation = Message.class)
    ))
    ResponseEntity<Message> create(
            MessageCreateRequest request,
            List<MultipartFile> attachments
    );

    @Operation(
            summary = "메시지 수정",
            parameters = @Parameter(
                    name = "messageId", required = true, description = "수정하려는 메시지 ID",
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            ),
            requestBody = @RequestBody(
                    required = true,
                    description = "수정 메시지",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageUpdateRequest.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = Message.class)
            )),
            @ApiResponse(responseCode = "404", content = @Content(
                    examples = @ExampleObject(value = "Message with id {messageId} not found")
            ))
    })
    ResponseEntity<Message> update(UUID messageId, MessageUpdateRequest request);

    @Operation(
            summary = "메시지 삭제",
            parameters = @Parameter(
                    name = "messageId",
                    description = "삭제하려는 메시지 ID",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = Message.class)
            )),
            @ApiResponse(responseCode = "404", content = @Content(
                    examples = @ExampleObject(value = "Message with id {messageId} not found")
            ))
    })
    ResponseEntity<Void> delete(UUID messageId);

    @Operation(
            summary = "채널의 모든 메시지 조회",
            parameters = @Parameter(
                    name = "channelId",
                    description = "조회하려는 채널 ID",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = Message.class))
            )
    )
    ResponseEntity<List<Message>> findAllByChannelId(UUID channelId);
}
