package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.notfound.NotfoundIdException;
import com.sprint.mission.discodeit.exception.validation.channel.InvalidChannelNameException;
import com.sprint.mission.discodeit.exception.validation.channel.InvalidChannelTypeException;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        data = new HashMap<>();
    }

    @Override
    public Channel create(String name, String description, ChannelType channelType) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidChannelNameException("유효하지 않은 채널명입니다.");
        }
        if (channelType == null || !isValidChannelType(channelType)) {
            throw new InvalidChannelTypeException("유효하지 않은 채널 타입입니다.");
        }
        Channel channel = new Channel(name, description, channelType);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        return data.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID channelId, String name, String description, ChannelType channelType) {
        if (!data.containsKey(channelId)) {
            throw new NotfoundIdException("유효하지 않은 Id입니다.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidChannelNameException("유효하지 않은 채널명입니다.");
        }
        if (channelType == null || !isValidChannelType(channelType)) {
            throw new InvalidChannelTypeException("유효하지 않은 채널 타입입니다.");
        }

        Channel checkChannel = data.get(channelId);
        checkChannel.update(name, description, channelType);
        return checkChannel;
    }

    @Override
    public void delete(UUID channelId) {
        if (!data.containsKey(channelId)) {
            throw new NotfoundIdException("유효하지 않은 Id입니다.");
        }
        data.remove(channelId);
    }

    private boolean isValidChannelType(ChannelType channelType) {
        for (ChannelType type : ChannelType.values()) {
            if (type == channelType) {
                return true;
            }
        }
        return false;
    }
}
