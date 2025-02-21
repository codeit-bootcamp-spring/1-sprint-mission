package com.sprint.mission.discodeit.controller.api;


import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.ReadStatus;
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

@Tag(name = "ReadStatus", description = "Message ReadStatus API - 읽음 상태")
public interface ReadStatusAPI {

    @Operation(summary = "Create ReadStatus")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Message ReadStatus가 성공적으로 생성되었습니다.",
                    content = @Content(schema = @Schema(implementation = ReadStatus.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 Channel 또는 User를 찾을 수 없습니다.",
                    content = @Content(examples = @ExampleObject(value = "{channelId | userId} not found"))
            ),
            @ApiResponse(
                    responseCode = "400", description = "이미 읽은 메세지입니다.",
                    content = @Content(examples = @ExampleObject(value = "{userId} and {channelId} already exists"))
            )
    })
    ResponseEntity<ReadStatus> createReadStatus(
            @Parameter(description = "ReadStatus Info") ReadStatusCreate readStatusCreate
    );

    @Operation(summary = "Update ReadStatus")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "ReadStatus가 성공적으로 수정되었습니다.",
                    content = @Content(schema = @Schema(implementation = ReadStatus.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 ReadStatus를 찾을 수 없습니다.",
                    content = @Content(examples = @ExampleObject(value = "{readStatusId} not found"))
            )
    })
    ResponseEntity<ReadStatus> updateReadStatus(
            @Parameter(description = "ReadStatusID - update") UUID readStatusId,
            @Parameter(description = "UpdateReadStatus Info") ReadStatusUpdate request
    );

    @Operation(summary = "User의 ReadStatus 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "ReadStatus 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReadStatus.class)))
            )
    })
    ResponseEntity<List<ReadStatus>> getReadStatusByUser(
            @Parameter(description = "UserID") UUID userId
    );
}