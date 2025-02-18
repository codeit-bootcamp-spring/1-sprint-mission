package com.sprint.mission.discodeit.channel.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.channel.dto.request.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.channel.dto.request.readStatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.channel.entity.ReadStatus;
import com.sprint.mission.discodeit.channel.service.ReadStatusService;
import com.sprint.mission.discodeit.global.dto.CommonResponse;

@RestController
@RequestMapping("api/readStatus")
public class ReadStatusController {

	private final ReadStatusService readStatusService;

	public ReadStatusController(ReadStatusService readStatusService) {
		this.readStatusService = readStatusService;
	}

	//특정 채널의 메시지 수신 정보 생성(message,lastreadat = null로 처리)
	@RequestMapping(name = "/createReadStatus", method = RequestMethod.POST)
	public ResponseEntity<CommonResponse<ReadStatus>> createReadStatus(@RequestBody CreateReadStatusRequest request) {
		ReadStatus createdStatus = readStatusService.create(request);
		return new ResponseEntity<>(
			CommonResponse.success("UserId : " + createdStatus.getUserId() + "ReadStatus Chanel Created!",
				createdStatus), HttpStatus.OK);
	}

	@RequestMapping(name = "/updateReadStatus", method = RequestMethod.PUT)
	public ResponseEntity<CommonResponse<ReadStatus>> updateReadStatus(@RequestBody UpdateReadStatusRequest request) {
		ReadStatus updatedStatus = readStatusService.update(request);
		return ResponseEntity.ok(CommonResponse.success("ReadStatus updated successfully", updatedStatus));
	}

	@RequestMapping(name = "/user/{userId}", method = RequestMethod.GET)
	public ResponseEntity<CommonResponse<List<ReadStatus>>> getReadStatusesByUser(@PathVariable("userId") UUID userId) {
		List<ReadStatus> statuses = readStatusService.findAllByUserId(userId);
		return ResponseEntity.ok(CommonResponse.success("User's ReadStatus list retrieved", statuses));
	}

}
