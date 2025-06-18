package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.execption.readStatus.NotReadStatusOwner;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import com.sprint.mission.discodeit.validator.ReadStatusValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final ReadStatusValidator readStatusValidator;
    private final UserValidator userValidator;
    private final ChannelValidator channelValidator;
    private final ReadStatusMapper readStatusMapper;

    @Override
    @Transactional
    public ReadStatusDto createReadStatus(CreateReadStatusRequest createReadStatusRequest) {
        User user = userValidator.validateUserExistsByUserId(createReadStatusRequest.userId());
        Channel channel = channelValidator.validateChannelExistsByChannelId(
            createReadStatusRequest.channelId());

        return readStatusMapper.toReadStatusDto(
            readStatusRepository.saveReadStatus(
                ReadStatus.of(user, channel, createReadStatusRequest.lastReadAt(), false)));
    }

    @Override
    public ReadStatus findReadStatusById(UUID readStatusId) {
        return readStatusValidator.validateReadStatusExistsById(readStatusId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusDto> findAllReadStatusesByUserId(UUID userId) {
        User user = userValidator.validateUserExistsByUserId(userId);
        return readStatusRepository.findAllReadStatusByUser(user).stream()
            .map(readStatus -> readStatusMapper.toReadStatusDto(readStatus))
            .toList();
    }

    @Override
    @Transactional
    public ReadStatusDto updateReadStatus(UUID readStatusId,
        UpdateReadStatusRequest updateReadStatusRequest, UserDetails userDetails) {
        ReadStatus readStatus = readStatusValidator.validateReadStatusExistsById(readStatusId);

        if (!readStatus.getUser().getName().equals(userDetails.getUsername())) {
            throw new NotReadStatusOwner(
                Map.of("readStatusId", readStatus, "username", userDetails.getUsername()));
        }

        readStatus.updateLastReadAt(updateReadStatusRequest.newLastReadAt());
        readStatus.updateNotificationEnabled(updateReadStatusRequest.newNotificationEnabled());

        return readStatusMapper.toReadStatusDto(readStatus);
    }

    @Override
    public void deleteReadStatus(UUID readStatusId) {
        readStatusValidator.validateReadStatusExistsById(readStatusId);

        readStatusRepository.removeReadStatus(readStatusId);
    }
}
