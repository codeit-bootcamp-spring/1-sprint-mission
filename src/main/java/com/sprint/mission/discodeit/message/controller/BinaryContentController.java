package com.sprint.mission.discodeit.message.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.binaryContent.entity.BinaryContent;
import com.sprint.mission.discodeit.binaryContent.service.BinaryContentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent", description = "첨부파일 관련 API")
public class BinaryContentController {

	private final BinaryContentService binaryContentService;

	@Operation(summary = "단일 첨부파일 조회")
	@GetMapping(value = "")
	public ResponseEntity<BinaryContent> find(@RequestParam("binaryContentId") UUID binaryContentId) {
		BinaryContent binaryContent = binaryContentService.find(binaryContentId);
		return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
	}

	@Operation(summary = "여러개의 첨부파일 조회")
	@GetMapping(value = "/findAllByIdIn")
	public ResponseEntity<List<BinaryContent>> findAllByIdIn(
		@RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
		List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
		return ResponseEntity.status(HttpStatus.OK).body(binaryContents);
	}
}

