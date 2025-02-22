package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data;
    public FileChannelRepository(SerializationUtil<UUID, Channel> util) {
        this.data = util.loadData(); // 이 부분, filePath 매개변수 이해 잘 안됨
    }


    @Override
    public Channel save(Channel channel){
        return data.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> findById(UUID channelId){
        return Optional.ofNullable(data.get(channelId));
    }

    @Override
    public List<Channel> findAll(){
        return data.values().stream().toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    @Override
    public void deleteById(UUID channelId){
        data.remove(channelId);
    }
}
