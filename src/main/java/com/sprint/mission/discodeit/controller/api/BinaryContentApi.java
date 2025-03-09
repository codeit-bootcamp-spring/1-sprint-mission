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
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentApi {

    @Operation(
            summary = "BinaryContent 조회",
            description = "ID 값으로 해당 BinaryContent 조회합니다.",
            parameters = @Parameter(
                    name = "binaryContentId",
                    required = true,
                    example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = BinaryContent.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(examples = @ExampleObject(value = "BinaryContent with id {binaryContentId} not found"))
                    )
            }
    )
    ResponseEntity<BinaryContent> find(UUID binaryContentId);

    @Operation(
            summary = "BinaryContent 복수 조회",
            description = "쿼리 파라미터로 여러 BinaryContent 조회",
            parameters = @Parameter(
                    name = "binaryContentIds"
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BinaryContent.class)))
                    )
            }
    )
    ResponseEntity<List<BinaryContent>> findAllByIdIn(List<UUID> binaryContentIds);

}
