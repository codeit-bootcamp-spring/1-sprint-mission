package com.sprint.mission.controller;

import com.sprint.mission.common.CommonResponse;
import com.sprint.mission.common.exception.CustomErrorResponse;
import com.sprint.mission.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.service.jcf.addOn.ReadStatusService;

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
import jakarta.validation.Valid;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public class ReadStatusController {

    // 카피
    private final ReadStatusService readStatusService;

    @Operation(summary = "Message 읽음 상태 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "읽음 상태 생성 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "읽음 상태가 이미 존재함",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CommonResponse> create(@RequestBody @Valid ReadStatusCreateRequest request) {
        ReadStatus createdReadStatus = readStatusService.create(request);
        return CommonResponse.toResponseEntity
                (CREATED, "읽음 상태가 생성되었습니다.", createdReadStatus);
    }

    @Operation(summary = "Message 읽음 상태 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Message 읽음 상태가 성공적으로 수정됨",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = CustomErrorResponse.class))),
    })
    @PatchMapping("{id}")
    public ResponseEntity<CommonResponse> update(
            @Parameter(description = "수정할 읽음 상태 ID") @RequestParam("id") UUID readStatusId,
            @RequestBody @Valid ReadStatusUpdateRequest request) {
        ReadStatus updatedReadStatus = readStatusService.update(readStatusId, request);
        return CommonResponse.toResponseEntity
                (OK, "읽음 상태가 업데이트되었습니다.", updatedReadStatus);
    }

    @Operation(summary = "User의 Message 읽음 상태 목록 조회")
    @ApiResponse(responseCode = "200", description = "Message 읽음 상태 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReadStatus.class))))
    @GetMapping
    public ResponseEntity<CommonResponse> findAllByUserId(
            @Parameter(description = "조회할 User ID", required = true) @RequestParam("userId") UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        return CommonResponse.toResponseEntity
                (OK, "읽음 상태 목록이 조회되었습니다.", readStatuses);
    }
}
