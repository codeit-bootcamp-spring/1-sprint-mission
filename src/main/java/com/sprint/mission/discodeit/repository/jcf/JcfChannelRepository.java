package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.entity.Channel;
import com.sprint.mission.discodeit.dto.entity.ChannelGroup;
import com.sprint.mission.discodeit.dto.form.ChannelUpdateDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
public class JcfChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data=new HashMap<>();

    @Override
    public Channel createChannel(UUID id, Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public void updateChannel(UUID id, ChannelUpdateDto channelUpdateDto) {
        Channel findChannel = data.get(id);
        if (findChannel.getChannelGroup()== ChannelGroup.PRIVATE){
            log.info("PRIVATE는 채널 수정 불가입니다.");
        }
        findChannel.setChannelName(channelUpdateDto.getChannelName());
        findChannel.setDescription(channelUpdateDto.getDescription());
        findChannel.setUpdatedAt();
        log.info("PUBLIC 채널 수정완료");
    }

    @Override
    public void deleteChannel(UUID id) {
        data.remove(id);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<Channel> findByChannelName(String channelName) {
        List<Channel> all=findAll();
        for (Channel channel : all) {
            if(channel.getChannelName().equals(channelName)) {
                return Optional.of(channel);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

}
