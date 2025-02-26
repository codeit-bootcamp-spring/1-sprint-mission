package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatus create(ReadStatusCreateDto readStatusCreateDto) {
        //유저와 채널 존재 여부 확인
        if(!userRepository.existsById(readStatusCreateDto.userId())){
            throw new NoSuchElementException("User with id " +readStatusCreateDto.userId() + " does not exist");
        }
        if(channelRepository.existsById(readStatusCreateDto.channelId())){
            throw new NoSuchElementException("Channel with id " + readStatusCreateDto.channelId() + " does not exist");
        }
        //이미 존재하는 경우 예외 발생
        Optional<ReadStatus> existReadStatus = readStatusRepository.findByUserIdAndChannelId(
                readStatusCreateDto.userId(), readStatusCreateDto.channelId()
        );
        if(existReadStatus.isPresent()){
            throw new IllegalArgumentException("ReadStatus with userId " + readStatusCreateDto.userId() + " and channelId " + readStatusCreateDto.channelId() + " already exists");
        }

        ReadStatus readStatus = new ReadStatus(readStatusCreateDto.userId(),readStatusCreateDto.channelId(),readStatusCreateDto.lastReadAt());

        return readStatusRepository.save(readStatus);
    }

    public ReadStatus findById(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
    }

    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream().toList();
    }

    public ReadStatus update(UUID readStatusId , ReadStatusUpdateDto readStatusUpdateDto){
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                        .orElseThrow(()-> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
        readStatus.updateLastReadTime();
        return readStatusRepository.save(readStatus);
    }

    public void delete(UUID readStatusId) {
        if(!readStatusRepository.existsById(readStatusId)){
            throw new NoSuchElementException("Read status not found");
        }
        readStatusRepository.deleteById(readStatusId);
    }
}
