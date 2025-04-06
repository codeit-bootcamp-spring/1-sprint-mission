package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.dto.response.*;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository repository;

    @Override
    public ReadStatusResponse create(ReadStatusRequest request) {
        log.debug("읽음 상태 생성 시도");

        ReadStatus status = ReadStatusMapper.INSTANCE.toEntity(request);
        repository.save(status);

        log.info("읽음 상태 생성 완료 - request: {}", request);
        return ReadStatusMapper.INSTANCE.toDto(status);
    }

    @Override
    public ReadStatusResponse markMessageAsRead(UUID messageId) {
        log.debug("메시지 읽음 처리 요청 - messageId: {}", messageId);

        ReadStatus status = repository.findById(messageId)
            .orElseThrow(() -> {
                log.warn("메시지 읽음 상태 조회 실패 - 존재하지 않음: {}", messageId);
                return new MessageNotFoundException(messageId);
            });

        status.markAsRead();
        repository.save(status);

        log.info("메시지 읽음 처리 완료 - messageId: {}", messageId);
        return ReadStatusMapper.INSTANCE.toDto(status);
    }

    @Override
    public List<ReadStatusResponse> getUserMessageReadStatus(UUID userId, UUID channelId) {
        log.debug("유저 메시지 읽음 상태 조회 요청 - userId: {}, channelId: {}", userId, channelId);

        List<ReadStatus> statusList = repository.findByUserIdAndChannelId(userId, channelId);
        log.info("조회된 메시지 읽음 상태 수: {}", statusList.size());

        return statusList.stream()
            .map(ReadStatusMapper.INSTANCE::toDto)
            .toList();
    }
}
