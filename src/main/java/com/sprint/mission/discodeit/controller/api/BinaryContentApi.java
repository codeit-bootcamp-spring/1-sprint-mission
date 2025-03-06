package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.binarycontent.FindBinaryContentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentApi {

    @Operation(summary = "이진파일 단건 조회", description = "이진파일을 id를 통해 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "이진파일 단건 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FindBinaryContentResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "이진파일 단건 조회 실패 - 해당 파일이 존재하지 않음"
    )
    ResponseEntity<FindBinaryContentResponseDto> findByIdBinaryContent(@PathVariable UUID id);

    @Operation(summary = "이진파일 다건 조회", description = "모든 이진파일을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "이진파일 다건 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FindBinaryContentResponseDto.class)
            )
    )
    ResponseEntity<List<FindBinaryContentResponseDto>> findByIdBinaryContent();
}
