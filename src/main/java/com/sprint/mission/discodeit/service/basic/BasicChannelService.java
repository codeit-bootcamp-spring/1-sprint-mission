package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.observer.manager.ObserverManager;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validation.ChannelValidtor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ObserverManager observerManager;
    private final ChannelValidtor channelValidtor;
    private final ChannelRepository fileChannelRepository;

//    public BasicChannelService (ObserverManager observerManager,ChannelValidtor channelValidtor,ChannelRepository fileChannelRepository){
//        this.observerManager = observerManager;
//        this.channelValidtor = channelValidtor;
//        this.fileChannelRepository = fileChannelRepository;
//    }

    @Override
    public Channel createChannel(String chName){
        if(channelValidtor.isUniqueName(chName, fileChannelRepository.findAll())){
            Channel ch1 = new Channel(chName);
            fileChannelRepository.save(ch1);
            return ch1;
        }
        throw new CustomException(ExceptionText.CHANNEL_CREATION_FAILED);
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
    public void updateChannel(UUID uuId, String name ){
        Channel channel = fileChannelRepository.findById(uuId);
        if (channel != null) {
            channel.update(name);
        }
    }

    @Override
    public void deleteChannel(UUID uuid){
        fileChannelRepository.delete(uuid);
        observerManager.channelDeletionEvent(uuid);  // 채널 삭제 시 이름을 전달
    }
}
