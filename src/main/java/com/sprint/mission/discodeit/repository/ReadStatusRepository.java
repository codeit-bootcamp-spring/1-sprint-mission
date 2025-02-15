package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public interface ReadStatusRepository {
    //채널마다 HashMap<멤버id, readStatus객체> 해시맵을 만들어 채널에 속한 멤버들의 readStatus를 저장한다.

    public HashMap<UUID, ReadStatus> getChannelReadStatusMap(UUID channelsMap) throws IOException;
    public boolean addChannelReadStatusMap(UUID channelId, HashMap<UUID, ReadStatus> readStatusMap) throws IOException;
    public boolean saveReadStatus(UUID channelId, UUID userId, ReadStatus readStatus) throws IOException;
    public boolean deleteReadStatus(UUID channelId, UUID userId) throws IOException;
    public boolean deleteChannelReadStatusMap(UUID channelId);
    //업데이트 메소드의 필요성을 느끼지 못하여 우선은 C, R, D만 구현
}
