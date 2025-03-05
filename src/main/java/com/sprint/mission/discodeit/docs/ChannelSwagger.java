package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PrivateChannelCreateResponse;
import com.sprint.mission.discodeit.dto.response.PublicChannelCreateResponse;
import com.sprint.mission.discodeit.dto.response.PublicChannelUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "Channel", description = "Channel API")
public interface ChannelSwagger {

  @Operation(operationId = "create_3", summary = "Public Channel 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Public Channel이 성공적으로 생성됨")
  })
  ResponseEntity<PublicChannelCreateResponse> createPublic(PublicChannelCreateRequest request);

  @Operation(operationId = "create_4", summary = "Private Channel 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Private Channel이 성공적으로 생성됨")
  })
  ResponseEntity<PrivateChannelCreateResponse> createPrivate(PrivateChannelCreateRequest request);

  @Operation(operationId = "update_3", summary = "Channel 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Channel 정보가 성공적으로 수정됨"),
      @ApiResponse(responseCode = "400", description = "Private Channel은 수정할 수 없음", content = @Content(
          examples = @ExampleObject(value = "Private channel cannot be updated")
      )),
      @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음", content = @Content(
          examples = @ExampleObject(value = "Channel with id {channelId} not found")
      ))
  })
  ResponseEntity<PublicChannelUpdateResponse> update(UUID channelId,
      PublicChannelUpdateRequest request);

  @Operation(operationId = "delete_2", summary = "Channel 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Channel이 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "Channel을 찾을 수 없음", content = @Content(
          examples = @ExampleObject(value = "Channel with id {channelId} not found")
      ))
  })
  ResponseEntity<Void> delete(UUID channelId);

  @Operation(operationId = "findAll_1", summary = "User가 참여 중인 Channel 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Channel 목록 조회 성공")
  })
  ResponseEntity<List<ChannelDto>> findAll(UUID userId);
}