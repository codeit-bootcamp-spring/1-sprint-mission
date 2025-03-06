package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.FindReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserService userService;
    private final ChannelService channelService;

    @Override
    public FindReadStatusResponseDto create(CreateReadStatusRequestDto createReadStatusRequestDto) {

        UUID userId = createReadStatusRequestDto.userId();
        UUID channelId = createReadStatusRequestDto.channelId();

        userService.userIsExist(userId);
        channelService.channelIsExist(channelId);

        ReadStatus readStatus = new ReadStatus(userId, channelId);

        readStatusRepository.save(readStatus);

        return FindReadStatusResponseDto.fromEntity(readStatus);
    }

    @Override
    public FindReadStatusResponseDto find(UUID id) {

        ReadStatusIsExist(id);

        ReadStatus readStatus = readStatusRepository.load().get(id);

        return FindReadStatusResponseDto.fromEntity(readStatus);
    }

    @Override
    public List<FindReadStatusResponseDto> findAllByUserId(UUID userId) {

        userService.userIsExist(userId);

        return readStatusRepository.load().values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .map(FindReadStatusResponseDto::fromEntity)
                .toList();
    }

    @Override
    public FindReadStatusResponseDto update(UUID id) {

        ReadStatusIsExist(id);

        ReadStatus readStatus = readStatusRepository.load().get(id);
        readStatus.updateLastReadTime();
        readStatusRepository.save(readStatus);

        return FindReadStatusResponseDto.fromEntity(readStatus);
    }

    @Override
    public void delete(UUID id) {

        ReadStatusIsExist(id);

        readStatusRepository.delete(id);
    }

    private void ReadStatusIsExist(UUID id) {
        Map<UUID, ReadStatus> map = readStatusRepository.load();

        if (!map.containsKey(id)) {
            throw new NoSuchElementException("존재하지 않는 read status입니다.");
        }
    }
}
