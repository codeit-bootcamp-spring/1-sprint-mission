package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.util.FileIOUtil.saveToFile;

@Profile("jcf")
@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> repository;

    public JCFReadStatusRepository() {
        this.repository = new HashMap<>();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        repository.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public List<ReadStatus> saveAll(List<ReadStatus> readStatuses) {
        // 각 ReadStatus를 repository에 추가
        readStatuses.forEach(readStatus -> repository.put(readStatus.getId(), readStatus));

        // 파일에 저장하는 부분은 이 클래스에서 처리하지 않음
        // 모든 ReadStatus를 repository에 추가한 후, 리스트 그대로 반환
        return readStatuses;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return repository.values().stream()
                .filter(readStatus -> readStatus.getId().equals(id))
                .findFirst();
    }

    @Override
    public void deleteAll(List<ReadStatus> readStatuses) {
        // 각 ReadStatus를 삭제
        readStatuses.forEach(readStatus -> repository.remove(readStatus.getId()));
    }


    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelID) {
        return repository.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelID))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userID) {
        return repository.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userID))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(ReadStatus readStatus) {
        repository.remove(readStatus.getId());
    }
}
