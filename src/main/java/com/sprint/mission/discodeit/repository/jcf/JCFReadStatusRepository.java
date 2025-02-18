package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import javax.management.InstanceNotFoundException;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> readStatusMap;
    JCFReadStatusRepository(){
        readStatusMap=new HashMap<>();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatusMap.put(readStatus.getId(), readStatus);
        return readStatus;
    }
    @Override
    public ReadStatus findById(UUID id) {
        ReadStatus readStatus = readStatusMap.get(id);
        if (readStatus == null) {
            throw new IllegalArgumentException("해당 객체가 존재하지 않습니다.");
        }
        return readStatus;
    }

    @Override
    public Map<UUID, ReadStatus> load() {
        return readStatusMap;
    }


    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatusListByUserId = readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
        if (readStatusListByUserId.isEmpty()) {
            throw new IllegalArgumentException("해당 유저에 대한 객체가 존재하지 않습니다.");
        }
        return readStatusListByUserId;
    }


    @Override
    public List<UUID> findAllUserIdByChannelId(UUID channelId) {
        List<UUID> readStatusListByChannelId = readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .map(ReadStatus::getUserId)
                .toList();
        if (readStatusListByChannelId.isEmpty()) {
            throw new IllegalArgumentException("해당 채널에 대한 객체가 존재하지 않습니다.");
        }
        return readStatusListByChannelId;
    }

    @Override
    public Instant findLatestTimeByChannelId(UUID channeId) {
            return
                    readStatusMap.values().stream()
                    .filter(readStatus -> readStatus.getChannelId().equals(channeId))
                    .map(ReadStatus::getChannelLastReadTimes)
                    .max(Comparator.naturalOrder()) //가장 최신 시간
                    .orElseThrow(()-> new IllegalArgumentException("해당 객체를 찾을 수 없습니다."));

    }

    //delete
    @Override
    public void delete(UUID id) {
        if (!readStatusMap.containsKey(id)) {
            throw new IllegalArgumentException("해당 객체가 존재하지 않아서 삭제할 수 없습니다.");
        }
        readStatusMap.remove(id);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        boolean removed = readStatusMap.values().removeIf(readStatus -> readStatus.getChannelId().equals(channelId));
        if (!removed) {
            throw new IllegalArgumentException("해당 채널 ID에 해당하는 객체를 삭제할 수 없습니다.");
        }
    }

}
