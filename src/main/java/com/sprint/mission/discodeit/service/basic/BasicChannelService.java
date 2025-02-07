package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.observer.manager.ObserverManager;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validation.ChannelValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ObserverManager observerManager;
    private final ChannelValidator channelValidator;
    private final ChannelRepository fileChannelRepository;

    @Override
    public Channel createChannel(ChannelType type, String name, String description){
        try {
            channelValidator.isUniqueName(name);
        }catch (CustomException e){
            System.out.println("Channel 생성 실패 -> "+ e.getMessage());
        }
        Channel ch1 = new Channel(type,name, description);
        fileChannelRepository.save(ch1);
        return ch1;
    }

    @Override
    public Channel getChannel(UUID uuid){
        return fileChannelRepository.findById(uuid);
    }

    @Override
    public Map<UUID, Channel> getAllChannels() {
        return fileChannelRepository.findAll();
    }

    @Override
    public void updateChannel(UUID uuId, String name,String description ){
        Channel channel = fileChannelRepository.findById(uuId);
        if (channel != null) {
            channel.update(name, description);
        }
    }

    @Override
    public void deleteChannel(UUID uuid){
        fileChannelRepository.delete(uuid);
        observerManager.channelDeletionEvent(uuid);  // 채널 삭제 시 이름을 전달
    }
}
