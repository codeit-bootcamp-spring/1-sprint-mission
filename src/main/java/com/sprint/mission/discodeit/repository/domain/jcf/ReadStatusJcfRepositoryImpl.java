package com.sprint.mission.discodeit.repository.domain.jcf;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

//사용자가 언제 마지막으로 채널에 접속했는 지 기록
@Repository
@Profile("Jcf")
public class ReadStatusJcfRepositoryImpl implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> readStatusMap; //채널마다 각 유저의 ReadStatus 정보 가짐

    public ReadStatusJcfRepositoryImpl() {
        this.readStatusMap = new HashMap<>();
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
        List<ReadStatus> collect = readStatusMap.values().stream().filter(r -> r.getChannelId().equals(channelId)).collect(Collectors.toList());
        if(collect.isEmpty()) {
            return null;
        }
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
    public void update(ReadStatusDto before, ReadStatusDto after) {
        ReadStatus readStatus = findById(before.id());
        System.out.println("before = " + readStatus);
        readStatus = readStatus.update(after);
        System.out.println("after = " + readStatus);
        readStatusMap.replace(readStatus.getId(), readStatus);
    }
}
