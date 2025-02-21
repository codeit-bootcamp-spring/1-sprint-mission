package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
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

@Tag(name = "Message", description = "메세지 API")
public interface MessageAPI {

    @Operation(summary = "Message Create")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Message가 성공적으로 생성되었습니다.",
                    content = @Content(schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Channel 혹은 User를 찾을 수 없습니다.",
                    content = @Content(examples = @ExampleObject(value = "{channelId | authorId} not found"))
            ),
    })
    ResponseEntity<Message> createMessage(
            @Parameter(
                    description = "Message Create INFO",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ) MessageCreateDto messageCreateDto,
            @Parameter(
                    description = "Message attachements",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ) List<MultipartFile> attachments
    );

    @Operation(summary = "Update Message Content")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Message가 성공적으로 업데이트 되었습니다.",
                    content = @Content(schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 Message를 찾을 수 없습니다.",
                    content = @Content(examples = @ExampleObject(value = "{messageId} not found"))
            ),
    })
    ResponseEntity<Message> updateMessage(
            @Parameter(description = "Update MessageID") UUID messageId,
            @Parameter(description = "Update MessageContent") MessageUpdateDto messageUpdateDto
    );

    @Operation(summary = "Delete Message")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "Message가 성공적으로 삭제되었습니다."
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 Message를 찾을 수 없습니다.",
                    content = @Content(examples = @ExampleObject(value = "{messageId} not found"))
            ),
    })
    ResponseEntity<Void> deleteMessage(
            @Parameter(description = "Delete MessageId") UUID messageId
    );

    @Operation(summary = "Channel에 있는 Message들 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Message 조회에 성공했습니다.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Message.class)))
            )
    })
    ResponseEntity<List<Message>> getMessagesByChannel(
            @Parameter(description = "ChannelId") UUID channelId
    );
}