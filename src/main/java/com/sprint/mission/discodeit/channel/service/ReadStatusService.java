package com.sprint.mission.discodeit.channel.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.channel.dto.request.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.channel.dto.request.readStatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.channel.entity.ReadStatus;

public interface ReadStatusService {
	ReadStatus create(CreateReadStatusRequest request);

	ReadStatus find(UUID id);

	List<ReadStatus> findAllByUserId(UUID userId);

	ReadStatus update(UUID readStatusId, UpdateReadStatusRequest request);

	void delete(UUID id);

	void deleteAllByChannelId(UUID channelId);
}
