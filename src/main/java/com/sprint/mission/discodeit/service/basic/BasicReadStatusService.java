package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public ReadStatus create(CreateReadStatusDto createReadStatusDto) throws CustomException {
        if (channelRepository.findById(createReadStatusDto.channelId()) == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        } else if (userRepository.findById(createReadStatusDto.userId()) == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        } else if (readStatusRepository.findByChannelIdWithUserId(createReadStatusDto.channelId(), createReadStatusDto.userId()) != null) {
            throw new CustomException(ErrorCode.READ_STATUS_ALREADY_EXIST);
        }

        ReadStatus readStatus = new ReadStatus(createReadStatusDto.channelId(), createReadStatusDto.userId());
        readStatusRepository.save(readStatus);

        return readStatus;
    }

    @Override
    public ReadStatus findById(String userStatusId) {
        return readStatusRepository.findById(userStatusId);
    }

    @Override
    public ReadStatus update(String id, UpdateReadStatusDto updateReadStatusDto) {
        ReadStatus readStatus = readStatusRepository.findById(id);

        if (readStatus == null) {
            throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
        }

        if (readStatus.isUpdated(updateReadStatusDto)) {
            return readStatusRepository.save(readStatus);
        }
        return readStatus;
    }

    public List<ReadStatus> findAllByUserId(String userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public List<ReadStatus> findAllByChannelId(String channelId) {
        return readStatusRepository.findAllByChannelId(channelId);
    }

    @Override
    public boolean delete(String userStatusId) {
        return readStatusRepository.delete(userStatusId);
    }
}
