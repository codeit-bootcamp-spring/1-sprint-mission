package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.form.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.form.PrivateChannelDto;
import com.sprint.mission.discodeit.entity.form.PublicChannelDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class JcfChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data=new HashMap<>();

    @Override
    public void createChannel(UUID id, Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public void updateChannel(UUID id, ChannelUpdateDto channelUpdateDto) {
        Channel findChannel = data.get(id);
        if (findChannel.getChannelGroup().equals("PUBLIC")) {
            findChannel.setChannelName(channelUpdateDto.getChannelName());
            findChannel.setDescription(channelUpdateDto.getDescription());
            log.info("PUBLIC 채널 수정완료");
        }
        log.info("PRIVATE는 채널 수정 불가입니다.");
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
