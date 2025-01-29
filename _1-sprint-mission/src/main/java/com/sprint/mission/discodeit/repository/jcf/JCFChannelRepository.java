package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.nio.channels.FileChannel;
import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    //데이터 저장
    private final Map<UUID,Channel> channelData;

    //의존성 주입
    public JCFChannelRepository() {
        this.channelData = new HashMap<>();
    }
    //생성
    @Override
    public Channel create(Channel channel) {
        channelData.put(channel.getId(), channel);
        return channel;
    }
    //단일조회
    @Override
    public Channel findById(UUID channelId) {
        return channelData.get(channelId);
    }
    //다중조회
    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelData.values());
    }
    //삭제
    @Override
    public void delete(UUID channelId) {
        if(!channelData.containsKey(channelId)){
            throw new NoSuchElementException("UserId not found");
        }
        channelData.remove(channelId);
    }
}
