package com.sprint.mission.discodeit.readStatus.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.readStatus.entity.ReadStatus;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.readStatus.service.ReadStatusService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
@Tag(name = "ReadStatus", description = "메시지 수신 정보 API")
public class ReadStatusController {

	private final ReadStatusService readStatusService;

	//특정 채널의 메시지 수신 정보 생성(message,lastreadat = null로 처리)
	@Operation(summary = "메시지 수신 정보 생성", description = "특정 채널의 메시지 수신 정보 생성 API")
	@PostMapping(value = "")
	public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreateRequest request) {
		ReadStatus createdReadStatus = readStatusService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdReadStatus);
	}

	@Operation(summary = "메시지 수신 정보 수정", description = "특정 채널의 메시지 수신 정보 수정 API")
	@PutMapping(value = "/{readStatusId}")
	public ResponseEntity<ReadStatus> update(@PathVariable("readStatusId") UUID readStatusId,
		@RequestBody ReadStatusUpdateRequest request) {
		ReadStatus updatedReadStatus = readStatusService.update(readStatusId, request);
		return ResponseEntity.status(HttpStatus.OK).body(updatedReadStatus);
	}

	@Operation(summary = "사용자의 메시지 수신 정보 조회", description = "특정 채널의 사용자 메시지 수신 정보 생성 API")
	@GetMapping(value = "")
	public ResponseEntity<List<ReadStatus>> findAllByUserId(@RequestParam("userId") UUID userId) {
		List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
		return ResponseEntity.status(HttpStatus.OK).body(readStatuses);
	}

}
