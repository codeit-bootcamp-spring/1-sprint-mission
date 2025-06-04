package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;

public interface BinaryContentService {

	BinaryContentDto create(BinaryContentCreateRequest request);

	BinaryContentDto find(UUID binaryContentId);

	List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds);

	void delete(UUID binaryContentId);
}
