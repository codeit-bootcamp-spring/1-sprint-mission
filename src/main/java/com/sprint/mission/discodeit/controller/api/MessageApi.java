package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.reponse.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message", description = "Meesage API")
public interface MessageApi {

  @Operation(summary = "Message 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "Message가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = MessageDto.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "유저를 찾을 수 없습니다. | 채널을 찾을 수 없습니다."))
      )
  })
  ResponseEntity<MessageDto> createMessage(
      @Parameter(
          description = "Message 생성 정보",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
      ) MessageCreateRequest messageCreateRequest,
      @Parameter(
          description = "Message 첨부 파일",
          content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
      ) List<MultipartFile> attachments
  );

  @Operation(summary = "Message 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Message 가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = MessageDto.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Message 를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "메세지를 찾을 수 없습니다."))
      )
  })
  ResponseEntity<MessageDto> updateMessage(
      @Parameter(description = "수정할 Message Id") UUID messageId,
      @Parameter(description = "수정할 Message 정보") MessageUpdateRequest messageUpdateRequest
  );

  @Operation(summary = "Message 삭제")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "Message 가 성공적으로 삭제됨"
      ),
      @ApiResponse(
          responseCode = "404", description = "Message 를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "메세지를 찾을 수 없습니다."))
      )
  })
  ResponseEntity<Void> deleteMessage(
      @Parameter(description = "삭제할 Message Id") UUID messageId
  );


  @Operation(summary = "Channel 의 Message 목록 조회")
  @ApiResponses(
      @ApiResponse(
          responseCode = "200", description = "Message 목록 조회 성공",
          content = @Content(schema = @Schema(implementation = PageResponse.class))
      )
  )
  ResponseEntity<PageResponse<MessageDto>> getMessageByChannelId(
      @Parameter(description = "조회할 Channel Id") UUID channelId,
      @Parameter(description = "Pageable 의 시작 페이지", example = "0", schema = @Schema(defaultValue = "0")) int page,
      @Parameter(description = "Pageable 의 각 페이지 별 개수", example = "50", schema = @Schema(defaultValue = "50")) int size,
      @Parameter(description = "Pageable 의 정렬 기준", example = "createDate", schema = @Schema(defaultValue = "createDate")) String sortBy,
      @Parameter(description = "Sort 의 정렬 순서", example = "desc", schema = @Schema(defaultValue = "desc")) String direction
  );
}
