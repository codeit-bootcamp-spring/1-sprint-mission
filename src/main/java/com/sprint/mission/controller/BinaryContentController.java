package com.sprint.mission.controller;

import com.sprint.mission.common.CommonResponse;
import com.sprint.mission.common.exception.CustomErrorResponse;
import com.sprint.mission.dto.BinaryContentMapper;
import com.sprint.mission.dto.response.BinaryContentDto;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.repository.BinaryContentStorage;
import com.sprint.mission.service.jcf.addOn.BinaryService;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent", description = "첨부 파일 API")
public class BinaryContentController {

    private final BinaryService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;

    @Operation(summary = "첨부 파일 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "첨부 파일 조회 성공",
                    content = @Content(schema = @Schema(implementation = BinaryContent.class))),
            @ApiResponse(responseCode = "404", description = "첨부 파일을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
    })
    @GetMapping("{id}")
    public ResponseEntity<CommonResponse> find(
            @Parameter(description = "조회할 첨부 파일 ID") @PathVariable("id") UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.findById(binaryContentId);
        // 이 때는 byte 공백으로 처리하도록
        return CommonResponse.toResponseEntity
                (OK, "BinaryContent 조회 성공", binaryContentMapper.toDto(binaryContent));
    }


    @Operation(summary = "여러 첨부 파일 조회")
    @ApiResponse(responseCode = "200", description = "첨부 파일 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BinaryContent.class))))
    @GetMapping
    public ResponseEntity<CommonResponse> findAllByIdIn(
            @Parameter(description = "조회할 첨부 파일 ID 목록") @RequestParam("ids") List<UUID> binaryContentIds) {
        List<BinaryContentDto> binaryContentDtoList = binaryContentService.findAllByIdIn(binaryContentIds).stream()
                .map(binaryContentMapper::toDto).toList();

        return CommonResponse.toResponseEntity
                (OK, "BinaryContent 목록 조회 성공", binaryContentDtoList);
    }

    // 파일 다운로드 로직 넣기
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable("id") UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.findById(binaryContentId);
        return (ResponseEntity<Resource>) binaryContentStorage.download(binaryContentMapper.toDto(binaryContent));
    }
}
