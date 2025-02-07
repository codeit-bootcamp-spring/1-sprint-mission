package com.sprint.mission.discodeit.repository.domain.file;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

//사용자가 언제 마지막으로 채널에 접속했는 지 기록
@Repository
@RequiredArgsConstructor
@Profile("File")
public class ReadStatusFileRepositoryImpl implements ReadStatusRepository {

    @Override
    public void save(ReadStatusDto readStatusDto) {

    }

    @Override
    public ReadStatus findById(UUID id) {
        return null;
    }

    @Override
    public List<ReadStatus> findAll() {
        return List.of();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return List.of();
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public void deleteByUserId(UUID userId) {

    }

    @Override
    public void deleteByChannelId(UUID id) {

    }

    @Override
    public void update(ReadStatusDto readStatusDto) {

    }
}
