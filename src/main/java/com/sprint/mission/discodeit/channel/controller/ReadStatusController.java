package com.sprint.mission.discodeit.channel.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.channel.dto.request.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.channel.dto.request.readStatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.channel.entity.ReadStatus;
import com.sprint.mission.discodeit.channel.service.ReadStatusService;
import com.sprint.mission.discodeit.global.dto.CommonResponse;

@RestController
@RequestMapping("api/readStatuses")
public class ReadStatusController {

	private final ReadStatusService readStatusService;

	public ReadStatusController(ReadStatusService readStatusService) {
		this.readStatusService = readStatusService;
	}

	//특정 채널의 메시지 수신 정보 생성(message,lastreadat = null로 처리)
	@PostMapping(value = "")
	public ResponseEntity<CommonResponse<ReadStatus>> createReadStatus(@RequestBody CreateReadStatusRequest request) {
		ReadStatus createdStatus = readStatusService.create(request);
		return new ResponseEntity<>(
			CommonResponse.success("UserId : " + createdStatus.getUserId() + "ReadStatus Chanel Created!",
				createdStatus), HttpStatus.OK);
	}

	@PutMapping(value = "/{readStatusId}")
	public ResponseEntity<CommonResponse<ReadStatus>> updateReadStatus(@PathVariable("readStatusId") UUID readStatusId,
		@RequestBody UpdateReadStatusRequest request) {
		ReadStatus updatedStatus = readStatusService.update(readStatusId, request);
		return ResponseEntity.ok(CommonResponse.success("ReadStatus updated successfully", updatedStatus));
	}

	@GetMapping(value = "/user/{userId}")
	public ResponseEntity<CommonResponse<List<ReadStatus>>> getReadStatusesByUser(@PathVariable("userId") UUID userId) {
		List<ReadStatus> statuses = readStatusService.findAllByUserId(userId);
		return ResponseEntity.ok(CommonResponse.success("User's ReadStatus list retrieved", statuses));
	}

}
