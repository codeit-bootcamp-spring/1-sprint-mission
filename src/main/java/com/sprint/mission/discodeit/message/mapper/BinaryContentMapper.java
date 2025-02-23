package com.sprint.mission.discodeit.message.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sprint.mission.discodeit.message.dto.response.binaryContent.BinaryContentResponse;
import com.sprint.mission.discodeit.message.entity.BinaryContent;

@Component
public class BinaryContentMapper {

	public BinaryContentResponse toResponse(BinaryContent binaryContent) {
		return new BinaryContentResponse(
			binaryContent.getId(),
			binaryContent.getCreatedAt(),
			binaryContent.getAuthorId(),
			binaryContent.getMessageId(),
			binaryContent.getContent(),
			binaryContent.getContentType(),
			binaryContent.getFileName(),
			binaryContent.getFileSize(),
			binaryContent.getBinaryContentType()
		);
	}

	public List<BinaryContentResponse> toResponseList(List<BinaryContent> binaryContents) {
		List<BinaryContentResponse> responses = new ArrayList<>();
		for (BinaryContent content : binaryContents) {
			responses.add(toResponse(content));
		}
		return responses;
	}
}
