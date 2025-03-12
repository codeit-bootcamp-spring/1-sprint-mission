package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.BadRequestException;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Transactional
    @Override
    public ReadStatusDto create(ReadStatusCreateRequest request) {
        UUID userId = request.userId();
        UUID channelId = request.channelId();

        if (readStatusRepository.findAllByUserId(userId).stream()
                .anyMatch(rs -> rs.getChannel().getId().equals(channelId))) {
            throw new BadRequestException("이미 존재하는 읽음 상태입니다: 사용자 ID " + userId + ", 채널 ID " + channelId);
        }

        // 요청으로 받은 Instant를 Timestamp로 변환 (null 체크 포함)
        Timestamp lastReadAtTimestamp = request.lastReadAt() != null ? Timestamp.from(request.lastReadAt()) : null;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자", "id", userId));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("채널", "id", channelId));

        ReadStatus readStatus = ReadStatus.builder()
                .user(user)
                .channel(channel)
                .lastReadAt(lastReadAtTimestamp)
                .build();

        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);
        log.info("읽음 상태 생성 완료: {}", savedReadStatus.getId());
        
        return readStatusMapper.toDto(savedReadStatus);
    }

    @Override
    public ReadStatusDto find(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new ResourceNotFoundException("읽음 상태", "id", readStatusId));
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("사용자", "id", userId);
        }
        
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatusMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new ResourceNotFoundException("읽음 상태", "id", readStatusId));
        
        Timestamp newLastReadAtTimestamp = request.newLastReadAt() != null 
                ? Timestamp.from(request.newLastReadAt()) 
                : null;
        
        readStatus.setLastReadAt(newLastReadAtTimestamp);
        ReadStatus updatedReadStatus = readStatusRepository.save(readStatus);
        log.info("읽음 상태 업데이트 완료: {}", updatedReadStatus.getId());
        
        return readStatusMapper.toDto(updatedReadStatus);
    }

    @Transactional
    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw new ResourceNotFoundException("읽음 상태", "id", readStatusId);
        }
        readStatusRepository.deleteById(readStatusId);
        log.info("읽음 상태 삭제 완료: {}", readStatusId);
    }
}