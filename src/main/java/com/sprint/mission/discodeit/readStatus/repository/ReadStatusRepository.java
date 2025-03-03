package com.sprint.mission.discodeit.readStatus.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.readStatus.entity.ReadStatus;

public interface ReadStatusRepository {
	ReadStatus save(ReadStatus readStatus);

	Optional<ReadStatus> findById(UUID id);

	//사용자의 전체 채널 읽음 상태 조회(전체 채널의 마지막 읽은 시간을 확인할 때 사용)
	List<ReadStatus> findAllByUserId(UUID userId);

	//채널의 전체 사용자 읽음 상태 조회(채널 참여자들의 메시지 읽음 현황을 파악할때 사용)
	List<ReadStatus> findAllByChannelId(UUID channelId);

	boolean existsById(UUID id);

	void deleteById(UUID id);

	void deleteAllByChannelId(UUID channelId);
}
