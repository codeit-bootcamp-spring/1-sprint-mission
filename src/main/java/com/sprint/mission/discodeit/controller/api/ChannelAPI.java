package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreate;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreate;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdate;
import com.sprint.mission.discodeit.entity.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel", description = "채널 API")
public interface ChannelAPI {

    @Operation(summary = "PublicChannel 생성")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Public Channel이 성공적으로 생성되었습니다.",
                    content = @Content(schema = @Schema(implementation = Channel.class))
            )
    })
    ResponseEntity<Channel> createPublicChannel(
            @Parameter(description = "Public Channel Info") PublicChannelCreate publicChannelCreate
    );

    @Operation(summary = "PrivateChannel 생성")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Private Channel이 성공적으로 생성되었습니다.",
                    content = @Content(schema = @Schema(implementation = Channel.class))
            )
    })
    ResponseEntity<Channel> createPrivateChannel(
            @Parameter(description = "Private Channel Info") PrivateChannelCreate privateChannelCreate
    );

    @Operation(summary = "Update Channel")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Channel 이 성공적으로 수정되었습니다.",
                    content = @Content(schema = @Schema(implementation = Channel.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 Channel을 찾을 수 없습니다.",
                    content = @Content(examples = @ExampleObject(value = "{channelId} not found"))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Private Channel은 수정할 수 없습니다.",
                    content = @Content(examples = @ExampleObject(value = "PrivateChannel은 수정할 수 없습니다."))
            )
    })
    ResponseEntity<Channel> updateChannel(
            @Parameter(description = "update ChannelId") UUID channelId,
            @Parameter(description = "update ChannelInfo") PublicChannelUpdate publicChannelUpdate
    );

    @Operation(summary = "Channel 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "Channel이 성공적으로 삭제되었습니다."
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 Channel을 찾을 수 없습니다.",
                    content = @Content(examples = @ExampleObject(value = "{channelId} not found"))
            )
    })
    ResponseEntity<Void> deleteChannel(
            @Parameter(description = "삭제할 ChannelID") UUID channelId
    );

    @Operation(summary = "사용자가 참여중인 Channel 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Channels 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class)))
            )
    })
    ResponseEntity<List<ChannelDto>> getChannelsByUserId(
            @Parameter(description = "userId") UUID userId
    );
}