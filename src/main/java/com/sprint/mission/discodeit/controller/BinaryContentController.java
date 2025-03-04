package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
@Tag(name = "Binary Contents", description = "바이너리 파일 관련 정보")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping("/{binaryContentId}")
  @Operation(
      summary = "바이너리 콘텐츠 조회",
      description = "개별 ID로 바이너리 파일을 조회합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "바이너리 콘텐츠 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = BinaryContentResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "바이너리 콘텐츠를 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "BinaryContent with id {binaryContentId} not found"
                  )
              )
          )
      }
  )
  public ResponseEntity<BinaryContentResponse> getBinaryContent(
      @Parameter(description = "조회할 바이너리 콘텐츠의 ID")
      @PathVariable("binaryContentId") UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.findById(binaryContentId);
    if (binaryContent == null) {
      throw new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found");
    }
    BinaryContentResponse response = BinaryContentResponse.from(binaryContent);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(response);
  }

  @GetMapping
  @Operation(
      summary = "여러 바이너리 콘텐츠 조회",
      description = "여러 ID로 바이너리 콘텐츠를 조회합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "바이너리 콘텐츠 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = BinaryContentResponse.class)
              )
          )
      }
  )
  public ResponseEntity<List<BinaryContentResponse>> findAllByIdIn(
      @Parameter(description = "조회할 바이너리 콘텐츠 ID 목록")
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);

    List<BinaryContentResponse> responses = new ArrayList<>();
    for (BinaryContent content : binaryContents) {
      responses.add(BinaryContentResponse.from(content));
    }

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responses);
  }
}