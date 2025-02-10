package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;

public interface BinaryContentService {
	BinaryContentResponse create(CreateBinaryContentRequest request);

	BinaryContentResponse find(UUID id);

	List<BinaryContentResponse> findAllByIdIn(List<UUID> ids);

	void delete(UUID id);
}
