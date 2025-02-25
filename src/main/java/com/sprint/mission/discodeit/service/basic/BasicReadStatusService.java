package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Override
    public ReadStatusResponseDto create(CreateReadStatusDto createReadStatusDto) throws CustomException {

        if (createReadStatusDto == null || createReadStatusDto.channelId() == null || createReadStatusDto.userId() == null) {
            throw new CustomException(ErrorCode.EMPTY_DATA);
        }
        Channel channel = channelRepository.findById(createReadStatusDto.channelId());
        if (channel == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }

        User user = userRepository.findById(createReadStatusDto.userId());
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        if (readStatusRepository.findByChannelIdWithUserId(createReadStatusDto.channelId(), createReadStatusDto.userId()) != null) {
            throw new CustomException(ErrorCode.READ_STATUS_ALREADY_EXIST);
        }

        ReadStatus readStatus = new ReadStatus(channel.getId(), user.getId());
        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

        return ReadStatusResponseDto.from(savedReadStatus, isNewMessage(readStatus));
    }

    @Override
    public List<ReadStatusResponseDto> createByChannelId(String channelId) throws CustomException {
        Channel channel = channelRepository.findById(channelId);
        if (channel == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }

        //todo - 새로들어온 유저만 해당 채널의 수신 정보를 생성하는 일이 있을까?
        if ( !findAllByChannelId(channelId).isEmpty()) {
            throw new CustomException(ErrorCode.READ_STATUS_ALREADY_EXIST);
        }

        //todo - 리팩토링
        List<String> userIds = List.of();

        if (channel.getChannelType() == ChannelType.PUBLIC) {
            userIds = userRepository.findAll().stream().map(User::getId).toList();
        } else if (channel.getChannelType() == ChannelType.PRIVATE) {
            userIds = channel.getUserSet().stream().toList();
        }

        List<ReadStatusResponseDto> readStatusResponseDtos = new ArrayList<>();

        for (String userId : userIds) {
            ReadStatus readStatus = new ReadStatus(channelId, userId);
            ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

            readStatusResponseDtos.add(ReadStatusResponseDto.from(savedReadStatus, isNewMessage(readStatus)));
        }

        return readStatusResponseDtos;
    }

    @Override
    public ReadStatusResponseDto findById(String readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId);
        return ReadStatusResponseDto.from(readStatus, isNewMessage(readStatus));
    }

    @Override
    public ReadStatusResponseDto update(String readStatusId, UpdateReadStatusDto updateReadStatusDto) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId);

        if (readStatus == null) {
            throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
        }

        if (readStatus.isUpdated(updateReadStatusDto)) {
            return ReadStatusResponseDto.from(readStatusRepository.save(readStatus), isNewMessage(readStatus));
        }
        return ReadStatusResponseDto.from(readStatus, isNewMessage(readStatus));
    }

    @Override
    public List<ReadStatusResponseDto> updateByUserId(String userId, UpdateReadStatusDto updateReadStatusDto) {

        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);

        if (readStatuses == null || readStatuses.isEmpty()) {
            throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
        }

        List<ReadStatusResponseDto> readStatusResponseDtos = new ArrayList<>();

        for (ReadStatus readStatus : readStatuses) {
            if (readStatus.isUpdated(updateReadStatusDto)) {
                readStatusResponseDtos.add(ReadStatusResponseDto.from(readStatusRepository.save(readStatus), isNewMessage(readStatus)));
            }else {
                readStatusResponseDtos.add(ReadStatusResponseDto.from(readStatus,isNewMessage(readStatus)));
            }
        }
        return readStatusResponseDtos;
    }

    @Override
    public List<ReadStatusResponseDto> updateByChannelId(String channelId, UpdateReadStatusDto updateReadStatusDto) {

        Channel channel = channelRepository.findById(channelId);
        if (channel == null) {
            throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
        }

        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channelId);
        if (readStatuses == null || readStatuses.isEmpty()) {
            throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
        }

        List<ReadStatusResponseDto> readStatusResponseDtos = new ArrayList<>();
        for (ReadStatus readStatus : readStatuses) {
            if (readStatus.isUpdated(updateReadStatusDto)) {
                readStatusResponseDtos.add(ReadStatusResponseDto.from(readStatusRepository.save(readStatus), isNewMessage(readStatus)));
            }
            else {
                readStatusResponseDtos.add(ReadStatusResponseDto.from(readStatus,isNewMessage(readStatus)));
            }
        }
        return readStatusResponseDtos;
    }

    public List<ReadStatusResponseDto> findAllByUserId(String userId) {

        List<ReadStatus> allReadStatusByUserId = readStatusRepository.findAllByUserId(userId);

        if (allReadStatusByUserId == null) {
            throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
        }

        List<ReadStatusResponseDto> readStatusResponseDtos = new ArrayList<>();

        for (ReadStatus readStatus : allReadStatusByUserId) {
            readStatusResponseDtos.add(ReadStatusResponseDto.from(readStatus, isNewMessage(readStatus)));
        }
        return readStatusResponseDtos;
    }

    @Override
    public List<ReadStatusResponseDto> findAllByChannelId(String channelId) {
        return readStatusRepository.findAllByChannelId(channelId).stream().map(r -> ReadStatusResponseDto.from(r, isNewMessage(r))).toList();
    }

    @Override
    public boolean delete(String readStatusId) {
        return readStatusRepository.delete(readStatusId);
    }

    public boolean isNewMessage(ReadStatus readStatus) throws CustomException {
        Instant lastMessageTimestamp = messageRepository.findAllByChannelId(readStatus.getChannelId()).stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo).orElse(null);

        if (lastMessageTimestamp == null) {
            return false;
        }
        return lastMessageTimestamp.isAfter(readStatus.getLastReadAt());
    }

}
