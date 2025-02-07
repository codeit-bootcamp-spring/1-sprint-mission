package com.sprint.mission.discodeit.repository.domain.jcf;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

//사용자가 언제 마지막으로 채널에 접속했는 지 기록
@Repository
@RequiredArgsConstructor
@Profile("Jcf")
public class ReadStatusJcfRepositoryImpl implements ReadStatusRepository {
    private Map<UUID, ReadStatus> readStatusMap;

    public ReadStatusJcfRepositoryImpl(Map<UUID, ReadStatus> readStatusMap) {
        this.readStatusMap = readStatusMap;
    }

    @Override
    public void save(ReadStatusDto readStatusDto) {
        //유저가 채널을 언제 마지막으로 읽었는 지 > 생성
        ReadStatus readStatus = new ReadStatus(readStatusDto.userId(), readStatusDto.channelId());
        readStatusMap.put(readStatus.getId(), readStatus);
    }

    @Override
    public ReadStatus findById(UUID id) {
        return readStatusMap.get(id);
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(readStatusMap.values());
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusMap.values().stream().filter(r -> r.getUserId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusMap.values().stream().filter(r -> r.getChannelId().equals(channelId)).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        readStatusMap.remove(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        List<ReadStatus> readStatuses = findAllByUserId(userId);
        for (ReadStatus readStatus : readStatuses) {
            delete(readStatus.getId());
        }
    }

    @Override
    public void deleteByChannelId(UUID id) {
        List<ReadStatus> readStatuses = findAllByChannelId(id);
        for (ReadStatus readStatus : readStatuses) {
            delete(readStatus.getId());
        }
    }

    @Override
    public void update(ReadStatusDto readStatusDto) {
        ReadStatus readStatus = findById(readStatusDto.id());
        readStatus = readStatus.update(readStatusDto);
        readStatusMap.replace(readStatus.getId(), readStatus);
    }
}
