package com.sprint.mission.discodeit.readStatus.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.readStatus.entity.ReadStatus;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusUpdateRequest;

public interface ReadStatusService {
	ReadStatus create(ReadStatusCreateRequest request);

	ReadStatus find(UUID readStatusId);

	List<ReadStatus> findAllByUserId(UUID userId);

	ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request);

	void delete(UUID readStatusId);
}
