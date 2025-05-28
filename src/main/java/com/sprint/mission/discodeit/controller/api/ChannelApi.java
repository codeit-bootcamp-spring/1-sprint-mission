package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelPrivateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collection;
import java.util.UUID;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Channel", description = "ChannelAPI")
public interface ChannelApi {

  @Operation(summary = "Public Channel 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "Public Channel 이 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = ChannelDto.class))
      )
  })
  ResponseEntity<ChannelDto> createPublicChannel(
      @Parameter(description = "Public Channel 생성 정보") ChannelPublicRequest channelPublicRequest
  );

  @Operation(summary = "Private Channel 생성")
  @ApiResponses(
      @ApiResponse(
          responseCode = "201", description = "Private Channel 이 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = ChannelDto.class))
      )
  )
  ResponseEntity<ChannelDto> createPrivateChannel(
      @Parameter(description = "Private Channel 생성 정보") ChannelPrivateRequest channelPrivateRequest
  );

  @Operation(summary = "Channel 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Channel 이 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = ChannelDto.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Channel 을 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "채널을 찾을 수 없습니다."))
      ),
      @ApiResponse(
          responseCode = "400", description = "Private 채널은 수정할 수 없음",
          content = @Content(examples = @ExampleObject(value = "채널의 수정을 허용하지 않습니다."))
      )
  })
  ResponseEntity<ChannelDto> updateChannel(
      @Parameter(description = "수정할 Channel Id") UUID channelId,
      @Parameter(description = "수정할 Channel 정보") ChannelUpdateRequest channelUpdateRequest
  );


  @Operation(summary = "Channel 삭제")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "Channel 이 성공적으로 삭제됨"
      ),
      @ApiResponse(
          responseCode = "404", description = "Channel 을 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "채널을 찾을 수 없습니다."))
      )
  })
  ResponseEntity<Void> deleteChannel(
      @Parameter(description = "삭제할 Channel Id") UUID channelId
  );

  @Operation(summary = "User 가 참여 중인 Channel 목록 조회")
  @ApiResponses(
      @ApiResponse(
          responseCode = "200", description = "Channel 목록 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class)))
      )
  )
  ResponseEntity<Collection<ChannelDto>> getChannelListByUserId(
      @Parameter(description = "조회할 User Id") UUID userId
  );
}
