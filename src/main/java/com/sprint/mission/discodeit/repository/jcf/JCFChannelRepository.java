package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name="repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data;

    public JCFChannelRepository() { this.data = new HashMap<>(); }


    @Override
    public boolean save(Channel channel) {
        data.put(channel.getId(), channel);
        System.out.println(channel.getChannelType() + " 채널이 오픈되었습니다.");
        return true;
    }

    @Override
    public Channel findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel modify(UUID id, Channel modifiedChannel) {
        Channel existingChannel = data.get(id);

        if(existingChannel == null){
            throw new IllegalArgumentException("Channel not found with id: " + id);
        }

        existingChannel.setName(modifiedChannel.getName());
        existingChannel.setDescription(modifiedChannel.getDescription());

        data.put(id, existingChannel);

        return existingChannel;
    }

    @Override
    public boolean deleteById(UUID id) {
        try {
            String removeChannelName = data.get(id).getName();
            data.remove(id);
            System.out.println(removeChannelName + "채널이 삭제되었습니다.");
            return true;
        } catch (NullPointerException e){
            System.out.println("유효하지 않은 채널입니다.\n" + e);
        }
        return false;
    }

}
