package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentApiDocs {

  @Operation(summary = "첨부 파일 조회", description = "단일 첨부 파일을 ID로 조회합니다.")
  ResponseEntity<BinaryContent> getById(
      @Parameter(description = "조회할 첨부 파일 ID", required = true) UUID binaryContentId
  );

  @Operation(summary = "여러 첨부 파일 조회", description = "여러 첨부 파일을 ID 목록으로 조회합니다.")
  ResponseEntity<List<BinaryContent>> getAllByIds(
      @Parameter(description = "조회할 첨부 파일 ID 목록", required = true) List<UUID> binaryContentIds
  );
}