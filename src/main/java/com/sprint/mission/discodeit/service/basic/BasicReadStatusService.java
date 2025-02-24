package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import com.sprint.mission.discodeit.validator.ReadStatusValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final ReadStatusValidator readStatusValidator;
    private final UserValidator userValidator;
    private final ChannelValidator channelValidator;

    @Override
    public ReadStatus createReadStatus(CreateReadStatusRequest createReadStatusRequest) {
        User user = userValidator.validateUserExistsByUserId(createReadStatusRequest.userId());
        Channel channel = channelValidator.validateChannelExistsByChannelId(createReadStatusRequest.channelId());

        return readStatusRepository.saveReadStatus(ReadStatus.of(user, channel));
    }

    @Override
    public ReadStatus findReadStatusById(UUID readStatusId) {
        return readStatusValidator.validateReadStatusExistsById(readStatusId);
    }

    @Override
    public ReadStatus findAllReadStatusesByUserId(UUID userId) {
        return readStatusValidator.validateReadStatusExistsByUserId(userId);
    }

    @Override
    public ReadStatus updateReadStatus(UUID readStatusId) {
        ReadStatus readStatus= readStatusValidator.validateReadStatusExistsById(readStatusId);
        readStatus.updateUpdatedAt();

        return readStatus;
    }

    @Override
    public void deleteReadStatus(UUID readStatusId) {
        readStatusValidator.validateReadStatusExistsById(readStatusId);

        readStatusRepository.removeReadStatus(readStatusId);
    }
}
