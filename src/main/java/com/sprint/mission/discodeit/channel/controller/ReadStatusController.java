package com.sprint.mission.discodeit.channel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.channel.service.ReadStatusService;

@RestController
@RequestMapping("api/readStatus")
public class ReadStatusController {

	private final ReadStatusService readStatusService;

	public ReadStatusController(ReadStatusService readStatusService) {
		this.readStatusService = readStatusService;
	}

	//특정 채널의 메시지 수신 정보 생성

}
