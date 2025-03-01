package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.ReadStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "ReadStatus API", description = "ReadStatus 관련 API")
public interface ReadStatusApiDocs {

  @Operation(summary = "수신 상태 생성", description = "채널 메세지 수신 상태를 생성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수신 상태 생성 성공"),
      @ApiResponse(responseCode = "400", description = "수신 상태 생성 실패"),
      @ApiResponse(responseCode = "404", description = "해당 채널이나 유저가 존재하지 않습니다.")
  })
  ReadStatusResponse createReadStatus(
      @RequestBody ReadStatusRequest.Create readStatusRequest);

  @Operation(summary = "수신 상태 업데이트", description = "수신 상태를 업데이트 합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수신 상태 업데이트 성공"),
      @ApiResponse(responseCode = "400", description = "수신 상태 업데이트 실패"),
      @ApiResponse(responseCode = "404", description = "해당 수신 상태 정보가 존재하지 않습니다.")
  })
  ReadStatusResponse updateReadStatus(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusRequest.Update readStatusRequest);

  @Operation(summary = "유저의 수신 상태 정보", description = "유저의 모든 채널에 대한 수신 상태를 가져옵니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "수신 상태 가져오기 성공"),
      @ApiResponse(responseCode = "400", description = "수신 상태 가져오기 실패"),
  })
  List<ReadStatusResponse> getReadStatusByUser(UUID userId);
}
