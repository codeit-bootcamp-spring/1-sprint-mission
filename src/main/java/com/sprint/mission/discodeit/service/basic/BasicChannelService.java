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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ObserverManager observerManager;
    private final ChannelValidator channelValidator;
    private final ChannelRepository channelRepository;

    @Override
    public Channel createPublicChannel(String name, String description){
        try {
            channelValidator.isUniqueName(name);
        }catch (CustomException e){
            System.out.println("Channel 생성 실패 -> "+ e.getMessage());
        }
        Channel ch = new Channel(ChannelType.PUBLIC,name, description);
        channelRepository.save(ch);
        return ch;
    }

    // PRIVATE 채널 생성
    @Override
    public Channel createPrivateChannel(String name, String description){
        try {
            channelValidator.isUniqueName(name);
        }catch (CustomException e){
            System.out.println("Channel 생성 실패 -> "+ e.getMessage());
        }
        Channel ch = new Channel(ChannelType.PRIVATE, name, description);
        channelRepository.save(ch);
        return ch;
    }

    @Override
    public Channel find(UUID channelId){
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String newName,String newDescription ){
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        channel.update(newName, newDescription);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId){
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        observerManager.channelDeletionEvent(channelId);  // 채널 삭제 시 이름을 전달
    }
}
