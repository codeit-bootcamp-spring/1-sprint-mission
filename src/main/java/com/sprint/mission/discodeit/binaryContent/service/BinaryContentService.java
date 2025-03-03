package com.sprint.mission.discodeit.binaryContent.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.binaryContent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binaryContent.entity.BinaryContent;

public interface BinaryContentService {

	BinaryContent create(BinaryContentCreateRequest request);

	BinaryContent find(UUID binaryContentId);

	List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);

	void delete(UUID binaryContentId);
}

