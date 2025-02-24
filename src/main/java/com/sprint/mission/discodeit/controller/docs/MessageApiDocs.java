package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message API", description = "Message 관련 API")
public interface MessageApiDocs {

  @Operation(summary = "메세지 생성", description = "메세지 생성하기")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "메세지 생성 성공"),
      @ApiResponse(responseCode = "400", description = "메세지 생성 실패"),
      @ApiResponse(responseCode = "404", description = "유저나 채널 정보를 찾을 수 없습니다."),
  })
  MessageResponse createMessage(
      MessageRequest.Create messageRequest,
      List<MultipartFile> files
  );

  @Operation(summary = "메세지 수정", description = "메세지 수정하기")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "메세지 수정 성공"),
      @ApiResponse(responseCode = "400", description = "메세지 수정 실패"),
      @ApiResponse(responseCode = "404", description = "해당 메세지를 찾을 수 없습니다.")
  })
  MessageResponse updateMessage(
      UUID messageId,
      MessageRequest.Update messageRequest,
      List<MultipartFile> files
  );

  @Operation(summary = "메세지 삭제", description = "메세지 삭제하기")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "메세지 삭제 성공"),
      @ApiResponse(responseCode = "400", description = "메세지 삭제 실패")
  })
  String deleteMessage(UUID messageId);

  @Operation(summary = "채널 메세지 조회", description = "채널 id로 채널 전체 메세지 조회하기")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "메세지 조회 성공"),
      @ApiResponse(responseCode = "400", description = "메세지 조회 실패"),
      @ApiResponse(responseCode = "404", description = "해당 채널을 찾을 수 없습니다.")
  })
  List<MessageResponse> getMessageListByChannel(UUID channelId);
}
