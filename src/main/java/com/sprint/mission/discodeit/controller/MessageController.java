package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Tag(name = "Message", description = "Message API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public List<BinaryContentCreateRequest> resolveAttachmentFile(List<MultipartFile> attachmentFile) {
        return attachmentFile.stream()
                .map(file -> {
                    try {
                    return new BinaryContentCreateRequest(
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getBytes()
                    );
                } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
        })
                .toList();
    }

    @Operation(summary = "Message 생성")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Message가 성공적으로 생성됨",
                    content = @Content(schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "Channel | Author with id {channelId | authorId} not found"))
            ),
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> create(@Parameter(description = "Message 생성 정보", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) @RequestPart("messageCreateRequest")MessageCreateRequest messageCreateRequest, @Parameter(description = "Message 첨부 파일들", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "attachmentFiles", required = false)List<MultipartFile> attachmentFiles) {
        List<BinaryContentCreateRequest> attachments = Optional.ofNullable(attachmentFiles)
                .map(this::resolveAttachmentFile)
                .orElse(List.of());

        Message createdMessage = messageService.createMessage(messageCreateRequest, attachments);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdMessage);
    }


    @Operation(summary = "Message 내용 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Message가 성공적으로 수정됨",
                    content = @Content(schema = @Schema(implementation = Message.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Message를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "Message with id {messageId} not found"))
            ),
    })
    @PatchMapping(path = "{messageId}")
    public ResponseEntity<Message> update(@Parameter(description = "수정할 Message ID") @PathVariable("messageId") UUID messageId, @Parameter(description = "수정할 Message 내용") @RequestBody MessageUpdateRequest request) {
        Message updatedMessage = messageService.updateMessageField(messageId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedMessage);
    }

    @Operation(summary = "Message 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "Message가 성공적으로 삭제됨"
            ),
            @ApiResponse(
                    responseCode = "404", description = "Message를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject(value = "Message with id {messageId} not found"))
            ),
    })
    @DeleteMapping(path = "{messageId}")
    public ResponseEntity<Void> delete(@Parameter(description = "삭제할 Message ID") @PathVariable("messageId") UUID messageId) {
        messageService.deleteMessageById(messageId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
    @Operation(summary = "Channel의 Message 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Message 목록 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Message.class)))
            )
    })
    @GetMapping
    public ResponseEntity<List<Message>> findAllByChannelId(@Parameter(description = "조회할 Channel ID") @RequestParam("channelId") UUID channelId) {
        List<Message> messages = messageService.readAllByChannelId(channelId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(messages);
    }
}

