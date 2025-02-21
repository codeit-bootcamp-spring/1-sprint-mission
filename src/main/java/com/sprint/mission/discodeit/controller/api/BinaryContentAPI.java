package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.entity.BinaryContent;
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

@Tag(name = "BinaryContent", description = "프로필 파일 API")
public interface BinaryContentAPI {

    @Operation(summary = "하나의 파일 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "파일 조회 Success",
                    content = @Content(schema = @Schema(implementation = BinaryContent.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 파일을 찾을 수 없습니다.",
                    content = @Content(examples = @ExampleObject(value = "{binaryContentId} not found"))
            )
    })
    ResponseEntity<BinaryContent> findFile(
            @Parameter(description = "조회할 BinaryContentId") UUID binaryContentId
    );

    @Operation(summary = "여러 파일 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "첨부 파일 목록 조회 Success",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BinaryContent.class)))
            )
    })
    ResponseEntity<List<BinaryContent>> getFilesByIds(
            @Parameter(description = "조회할 BinaryContentIDs 목록") List<UUID> binaryContentIds
    );
}