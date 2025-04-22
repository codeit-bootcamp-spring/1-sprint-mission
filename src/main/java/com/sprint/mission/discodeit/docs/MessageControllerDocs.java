package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.common.CustomApiResponse;
import com.sprint.mission.discodeit.dto.message.MessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.exception.ExceptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "Messages", description = "Message 관련 API")
public interface MessageControllerDocs {

    @RequestBody(content = @Content(
            encoding = @Encoding(name = "message", contentType = MediaType.APPLICATION_JSON_VALUE)))
    @Operation(summary = "메시지 생성", description = "메시지를 생성하고 첨부파일을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "메시지 생성 및 저장 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<MessageResponseDto> createMessage(
            @Parameter(description = "메시지 정보", required = true) @RequestPart("message") @Schema(format = "application/json") MessageRequestDto messageReqDto,
            @Parameter(description = "첨부파일 리스트", required = false) @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) throws IOException;

    @Operation(summary = "메시지 수정", description = "주어진 메시지 ID의 메시지를 수정합니다. 메시지 내용과 첨부파일을 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<MessageResponseDto> updateMessage(
            @Parameter(description = "메시지 ID", required = true) @PathVariable UUID messageId,
            @Parameter(description = "수정할 메시지 내용", required = true) @RequestPart("content") String content,
            @Parameter(description = "첨부파일 리스트", required = false) @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) throws IOException;

    @Operation(summary = "메시지 삭제", description = "주어진 메시지 ID의 메시지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<MessageResponseDto> deleteMessage(
            @Parameter(description = "메시지 ID", required = true) @PathVariable UUID messageId);

    @Operation(summary = "채널별 메시지 조회", description = "주어진 채널 ID에 해당하는 메시지 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    CustomApiResponse<PageResponse<MessageResponseDto>> getMessagesByChannel(
            @Parameter(description = "채널 ID", required = true) @RequestParam UUID channelId,
            @Parameter(description = "페이지 커서", required = false) @RequestParam UUID cursor);
}
