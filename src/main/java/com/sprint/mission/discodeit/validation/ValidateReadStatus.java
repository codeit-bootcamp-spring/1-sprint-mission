package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidateReadStatus {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;

    public void validateReadStatus(UUID channelId, UUID userId){
        validateChannel(channelId);
        validateUser(userId);
        validateDuplicateReadStatus(channelId, userId);
    }

    public void validateChannel(UUID channelId){
        if (channelRepository.findById(channelId).isEmpty()){
            throw new ResourceNotFoundException("Channel not found.");
        }
    }

    public void validateUser(UUID userId){
        if (userRepository.findByUserId(userId).isEmpty()){
            throw new ResourceNotFoundException("User not found.");
        }
    }

    public void validateDuplicateReadStatus(UUID channelId, UUID userId){
        if (!readStatusRepository.existsByUserIdAndChannelId(userId, channelId)){
            throw new ResourceNotFoundException("ReadStatus already exists.");
        }
    }

    public void validateReadStatus(UUID readStatusId){
        if (readStatusRepository.findById(readStatusId).isEmpty()){
            throw new ResourceNotFoundException("ReadStatus not found.");
        }
    }
}
