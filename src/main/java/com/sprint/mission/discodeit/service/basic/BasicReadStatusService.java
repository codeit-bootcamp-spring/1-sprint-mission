package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatusDto.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readStatusDto.FindReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserService userService;
    private final ChannelService channelService;

    @Override
    public UUID create(CreateReadStatusRequestDto createReadStatusRequestDto) {

        UUID userId = createReadStatusRequestDto.userId();
        UUID channelId = createReadStatusRequestDto.channelId();

        userService.userIsExist(userId);
        channelService.channelIsExist(channelId);

        ReadStatus readStatus = new ReadStatus(userId, channelId);

        readStatusRepository.save(readStatus);

        return readStatus.getId();
    }

    @Override
    public FindReadStatusResponseDto find(UUID id) {

        ReadStatus readStatus = readStatusRepository.load().get(id);

        return new FindReadStatusResponseDto(readStatus);
    }

    @Override
    public List<FindReadStatusResponseDto> findAllByUserId(UUID userId) {
        return readStatusRepository.load().values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .map(FindReadStatusResponseDto::new)
                .toList();
    }

    @Override
    public void update(UUID id) {
        ReadStatus readStatus = readStatusRepository.load().get(id);
        readStatus.updateLastReadTime();
        readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.delete(id);
    }
}
