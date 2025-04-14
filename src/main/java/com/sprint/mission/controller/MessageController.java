package com.sprint.mission.controller;

import com.sprint.mission.common.CommonResponse;
import com.sprint.mission.common.exception.CustomErrorResponse;
import com.sprint.mission.dto.BinaryContentMapper;
import com.sprint.mission.dto.MessageMapper;
import com.sprint.mission.dto.response.MessageDto;
import com.sprint.mission.dto.response.PageResponse;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.dto.response.ScrollPageResponse;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Message API")
public class MessageController {

    private final MessageService messageService;
    private final BinaryContentMapper binaryContentMapper;
    private final MessageMapper messageMapper;

    @Operation(summary = "Message 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Message가 성공적으로 생성됨",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> create(
            @RequestPart("messageCreateDto") @Valid MessageDtoForCreate requestDTO,
            @Parameter(description = "Message 첨부 파일들")
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
        List<BinaryContentDtoForCreate> binaryContentDtoForCreateList = ((attachments == null) || attachments.isEmpty())
                ? Collections.emptyList()
                : attachments.stream().map(binaryContentMapper::convertFileToBinaryContentDto)
                .flatMap(Optional::stream)
                .toList();

        Message createdMessage = messageService.create(requestDTO, binaryContentDtoForCreateList);
        return CommonResponse.toResponseEntity
                (CREATED, "메시지가 성공적으로 생성되었습니다.", messageMapper.toDto(createdMessage));
    }


    @Operation(summary = "Channel의 Message 목록 조회")
    @ApiResponse(responseCode = "200", description = "Message 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MessageDto.class))))
    @GetMapping
    public ResponseEntity<CommonResponse> findInChannel(
            @Parameter(description = "조회할 Channel ID") @RequestParam("channelId") UUID channelId) {

        // Page 인터페이스 버전
        // Pageable pageable = PageRequest.of(0, 50, Sort.by("createdAt").descending());
        // List<PageResponse<MessageDto>> pagedMessageList = messageService.findAllByChannelId(channelId, pageable);
        List<ScrollPageResponse<MessageDto>> scrollPageResponseList = messageService.findAllByChannelId(channelId);
        return CommonResponse.toResponseEntity
                (OK, "메시지 목록을 성공적으로 조회했습니다.", scrollPageResponseList);
    }


    @Operation(summary = "Message 내용 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PatchMapping("{id}")
    public ResponseEntity<CommonResponse> update(
            @Parameter(description = "수정할 Message ID")
            @PathVariable("id") UUID messageId,
            @RequestBody @Valid MessageDtoForUpdate requestDTO) {
        Message updatedMessage = messageService.update(messageId, requestDTO);
        return CommonResponse.toResponseEntity
                (OK, "메시지가 성공적으로 업데이트되었습니다.", messageMapper.toDto(updatedMessage));
    }


    @Operation(summary = "Message 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<CommonResponse> delete(@RequestParam("id") UUID messageId) {
        messageService.delete(messageId);
        return CommonResponse.toResponseEntityWithoutData
                (NO_CONTENT, "메시지가 성공적으로 삭제되었습니다.");
    }
}
