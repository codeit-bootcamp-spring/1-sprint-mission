package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ChannelUser;
import com.sprint.mission.discodeit.repository.ChannelUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChannelUserService {
    private final ChannelUserRepository channelUserRepository;

    public ChannelUser addUserToChannel(ChannelUser channelUser){
        return channelUserRepository.save(channelUser);
    }
    public List<UUID> findUserByChannelId(UUID channelId){
        return channelUserRepository.findUserByChannelId(channelId);
    }
    public List<UUID> findChannelByUserId(UUID userId){
        return channelUserRepository.findChannelByUserId(userId);
    }
    public void removeChannelUsers(UUID channelId){
        channelUserRepository.deleteByChannelId(channelId);
    }
    public void removeUserChannels(UUID userId){
        channelUserRepository.deleteByUserId(userId);
    }
}
