package com.sprint.mission.discodeit.message.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.global.dto.CommonResponse;
import com.sprint.mission.discodeit.message.dto.response.binaryContent.BinaryContentResponse;
import com.sprint.mission.discodeit.message.entity.BinaryContent;
import com.sprint.mission.discodeit.message.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.message.service.BinaryContentService;

@RestController
@RequestMapping("api/binary-content")
public class BinaryContentController {

	private final BinaryContentService binaryContentService;
	private final BinaryContentMapper binaryContentMapper;

	public BinaryContentController(BinaryContentService binaryContentService, BinaryContentMapper binaryContentMapper) {
		this.binaryContentService = binaryContentService;
		this.binaryContentMapper = binaryContentMapper;
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<CommonResponse<BinaryContentResponse>> getBinaryContentById(@PathVariable("id") UUID id) {
		BinaryContent content = binaryContentService.find(id);
		return ResponseEntity.ok(
			CommonResponse.success("Binary content retrieved successfully", binaryContentMapper.toResponse(content)));
	}

	@GetMapping("/get")
	public ResponseEntity<CommonResponse<List<BinaryContentResponse>>> getBinaryContentsByIds(
		@RequestParam("ids") List<UUID> ids) {
		List<BinaryContent> contents = binaryContentService.findAllByIdIn(ids);
		return ResponseEntity.ok(CommonResponse.success("Binary contents retrieved successfully",
			binaryContentMapper.toResponseList(contents)));
	}
}

