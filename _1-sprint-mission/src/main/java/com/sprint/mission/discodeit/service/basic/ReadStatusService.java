package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatus create(ReadStatusDto dto) {
        //유저와 채널 존재 여부 확인
        if(!userRepository.existsById(dto.user().getId())){
            throw new IllegalArgumentException("User not found");
        }
        if(channelRepository.existsById(dto.channel().getId())){
            throw new IllegalArgumentException("Channel not found");
        }
        //이미 존재하는 경우 예외 발생
        Optional<ReadStatus> existReadStatus = readStatusRepository.findByUserIdAndChannelId(dto.user().getId(), dto.channel().getId());
        if(existReadStatus.isPresent()){
            throw new IllegalArgumentException("Read status already exists");
        }
        ReadStatus readStatus = new ReadStatus(dto.user(),dto.channel(), dto.lastRead(),true);

        return readStatusRepository.save(readStatus);
    }

    public ReadStatus findById(UUID id) {
        return readStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Read status not found"));
    }

    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    public ReadStatus update(UpdateReadStatusDto dto){
        ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(dto.userId(),dto.channelId())
                .orElseThrow(() -> new NoSuchElementException("Read status not found"));
        readStatus.updateLastReadTime();
        return readStatusRepository.save(readStatus);
    }

    public void delete(UUID id){
        if(!readStatusRepository.existsById(id)){
            throw new NoSuchElementException("Read status not found");
        }
        readStatusRepository.deleteById(id);
    }
}
