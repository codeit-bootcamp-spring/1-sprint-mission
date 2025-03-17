package com.sprint.mission.service.jcf.addOn;


import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.ReadStatusMapper;
import com.sprint.mission.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.dto.response.ReadStatusDto;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.ReadStatusRepository;
import com.sprint.mission.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ExecutorService ves;

    public ReadStatus create(ReadStatusCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));

        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_CHANNEL));

        if (readStatusRepository.existsById(user.getStatus().getId())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_READ_STATUS);
        }

        return readStatusRepository.save(new ReadStatus(user, channel, request.lastReadAt()));
    }

    public ReadStatus findById(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_READ_STATUS));
    }

    public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = this.findById(readStatusId);
        readStatus.update(request.newLastReadAt());
        return readStatus;
    }

    public boolean existsById(UUID readStatusId) {
        return readStatusRepository.existsById(readStatusId);
    }

    public void delete(UUID readStatusId) {
        if (readStatusRepository.existsById(readStatusId)) {
            readStatusRepository.deleteById(readStatusId);
        } else {
            throw new CustomException(ErrorCode.NO_SUCH_READ_STATUS);
        }
    }

    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUser_Id(userId);

    }

    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusRepository.findAllByChannel_Id(channelId);
    }
}
