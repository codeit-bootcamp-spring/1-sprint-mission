package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.AbstractFileRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "file",  matchIfMissing = true)
public class FileChannelRepository extends AbstractFileRepository<Channel> implements ChannelRepository{

  public FileChannelRepository(@Value("${app.file.channel-file}") String filePath) {
    super(filePath);
  }

  @Override
  public Channel save(Channel channel) {
    List<Channel> channels = loadAll(Channel.class);
    channels.add(channel);
    saveAll(channels);
    return channel;
  }

  @Override
  public Optional<Channel> findById(String id) {
    List<Channel> channels = loadAll(Channel.class);
    return channels.stream().filter(c -> c.getId().equals(id)).findAny();
  }

  @Override
  public List<Channel> findAll() {
    return loadAll(Channel.class);
  }

  @Override
  public Channel update(Channel channel) {
    List<Channel> channels = loadAll(Channel.class);
    Channel targetChannel = channels.stream()
        .filter(c -> c.getId().equals(channel.getId()))
        .findAny()
        .orElseThrow(() -> new ChannelNotFoundException());
    channels.remove(targetChannel);
    channels.add(channel);
    saveAll(channels);
    return channel;
  }

  @Override
  public void delete(String id) {
    List<Channel> channels = loadAll(Channel.class);
    Channel targetChannel = channels.stream()
        .filter(c -> c.getId().equals(id))
        .findAny()
        .orElseThrow(() -> new ChannelNotFoundException());
    channels.remove(targetChannel);
    saveAll(channels);
  }

  @Override
  public void clear() {
    File file = new File(getFilePath());
    if(file.exists()) file.delete();
  }
}
