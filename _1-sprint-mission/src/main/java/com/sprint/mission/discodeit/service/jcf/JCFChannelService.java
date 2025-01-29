package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    //Repository 의존성 주입
    private final ChannelRepository channelRepository;

    public JCFChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }
    //생성
    @Override
    public Channel create(Channel.ChannelType channelType,String channelName,String description) {
        Channel channel = new Channel(channelType,channelName,description);
        return channelRepository.create(channel);
    }
    //단일조회
    @Override
    public Channel find(UUID channelId) {
        try{ //id값이 null이 아니면 그대로 리턴
            Channel channelNullable = channelRepository.findById(channelId);
            if(channelNullable == null){
                throw new NoSuchElementException("channelId not found");
            }
            return channelNullable;
        }catch (NoSuchElementException e){
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelRepository.findAll());
    }

    @Override
    public Channel update(UUID channelId, String newChannelDescription) {
        try{
            Channel channelNullable = channelRepository.findById(channelId);
            if(channelNullable == null){
                throw new NoSuchElementException("ChannelId not found");
            }
            channelNullable.updateDescription(newChannelDescription);
            return channelNullable;
        }catch (NoSuchElementException e){
            System.out.println("Error: "+e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(UUID channelId) {
        if(channelRepository.findById(channelId)==null){
            throw new NoSuchElementException("ChannelId not found");
        }
        channelRepository.delete(channelId);
    }
}
