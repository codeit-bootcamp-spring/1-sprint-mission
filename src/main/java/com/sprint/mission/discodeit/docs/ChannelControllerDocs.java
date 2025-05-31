package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.request.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Channel API", description = "채널 관리 API")
public interface ChannelControllerDocs {

  @Operation(summary = "퍼블릭 채널 생성")
  @ApiResponse(responseCode = "201", description = "퍼블릭 채널 생성")
  @PostMapping("/public")
  ResponseEntity<ChannelDto> createPublicChannel(
      @RequestBody PublicChannelRequest publicChannelRequest
  );

  @Operation(summary = "프라이빗 채널 생성")
  @ApiResponse(responseCode = "201", description = "프라이빗 채널 생성")
  @PostMapping("/private")
  ResponseEntity<ChannelDto> createPrivateChannel(
      @RequestBody PrivateChannelRequest privateChannelRequest
  );

  @Operation(summary = "채널 조회")
  @ApiResponse(responseCode = "200", description = "채널 목록 조회 성공")
  @GetMapping
  ResponseEntity<List<ChannelDto>> getChannels(
      @RequestParam UUID userId
  );

  @Operation(summary = "채널 수정")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "채널 수정 성공"),
      @ApiResponse(responseCode = "400", description = "프라이빗 채널은 수정할 수 없음"),
      @ApiResponse(responseCode = "404", description = "채널을 찾을 수 없음")
  })
  @PatchMapping("/{id}")
  ResponseEntity<ChannelDto> updatePublicChannel(
      @PathVariable UUID id,
      @RequestBody PublicChannelUpdateRequest updateRequest
  );

  @Operation(summary = "채널 삭제")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "채널 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "채널을 찾을 수 없음")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteChannel(
      @PathVariable UUID id
  );
}
