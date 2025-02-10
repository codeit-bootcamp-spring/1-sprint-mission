package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

public interface ReadStatusService {
	ReadStatus create(CreateReadStatusRequest request);

	ReadStatus find(UUID id);

	List<ReadStatus> findAllByUserId(UUID userId);

	ReadStatus update(UUID id, UpdateReadStatusRequest request);

	void delete(UUID id);
}
