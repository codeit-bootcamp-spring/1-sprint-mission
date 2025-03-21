package com.sprint.mission.service.jcf.addOn;


import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.repository.ReadStatusRepository;
import com.sprint.mission.repository.jcf.main.JCFChannelRepository;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final JCFUserRepository userRepository;
    private final JCFChannelRepository channelRepository;

    public ReadStatus create(ReadStatusCreateRequest request){
        if (!userRepository.existsById(request.userId())) {
            throw new CustomException(ErrorCode.NO_SUCH_USER);
        }

        if (!channelRepository.existsById(request.userId())) {
            throw new CustomException(ErrorCode.NO_SUCH_CHANNEL);
        }

        if (readStatusRepository.existsById(request.userId())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_READ_STATUS);
        }

        return readStatusRepository.save(request.toEntity());
    }

    public ReadStatus findById(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_READ_STATUS));
    }

    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusRepository.findAllByChannelId(channelId);
    }

    public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_READ_STATUS));

        readStatus.update(request.newLastReadAt());
        return readStatusRepository.save(readStatus);
    }

    public boolean existsById(UUID readStatusId) {
        return readStatusRepository.existsById(readStatusId);
    }

    public void delete(UUID readStatusId) {
        if (readStatusRepository.existsById(readStatusId)){
            readStatusRepository.deleteById(readStatusId);
        } else {
            throw new CustomException(ErrorCode.NO_SUCH_READ_STATUS);
        }
    }
}
