package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.entity.BinaryContent;
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
import org.springframework.http.ResponseEntity;

@Tag(name = "BinaryContent", description = "이미지 첨부파일 Api")
public interface BinaryContentApi {

  @Operation(summary = "첨부파일 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Attachment inquiry successful",
          content = @Content(schema = @Schema(implementation = BinaryContent.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "BinaryContent not found.",
          content = @Content(examples = @ExampleObject(value = "BinaryContent with id {binaryContentId} not found"))
      )
  })
  ResponseEntity<BinaryContent> find(
      @Parameter(description = "binaryContentId") UUID binaryContentId);


  @Operation(summary = "모든 첨부파일 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "All Attachment inquiry successful",
          content = @Content(schema = @Schema(implementation = BinaryContent.class))
      )
  })
  ResponseEntity<List<BinaryContent>> findAllByIdIn(
      @Parameter(description = "binaryContentIds") List<UUID> binaryContentIds);
}
