package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.CHANNEL_FILE;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "file")
public class FileChannelRepository implements ChannelRepository {

  private static FileChannelRepository instance;

  private FileChannelRepository() {
  }

  public static FileChannelRepository getInstance() {
    if (instance == null) {
      synchronized (FileChannelRepository.class) {
        if (instance == null) {
          instance = new FileChannelRepository();
        }
      }
    }
    return instance;
  }

  @Override
  public Channel save(Channel channel) {
    List<Channel> channels = FileUtil.loadAllFromFile(CHANNEL_FILE, Channel.class);
    channels.add(channel);
    FileUtil.saveAllToFile(CHANNEL_FILE, channels);
    return channel;
  }

  @Override
  public Optional<Channel> findById(String id) {
    List<Channel> channels = FileUtil.loadAllFromFile(CHANNEL_FILE, Channel.class);
    return channels.stream().filter(c -> c.getUUID().equals(id)).findAny();
  }

  @Override
  public List<Channel> findAll() {
    return FileUtil.loadAllFromFile(CHANNEL_FILE, Channel.class);
  }

  @Override
  public Channel update(Channel channel) {
    List<Channel> channels = FileUtil.loadAllFromFile(CHANNEL_FILE, Channel.class);
    Channel targetChannel = channels.stream()
        .filter(c -> c.getUUID().equals(channel.getUUID()))
        .findAny()
        .orElseThrow(() -> new ChannelNotFoundException());
    channels.remove(targetChannel);
    channels.add(channel);
    FileUtil.saveAllToFile(CHANNEL_FILE, channels);
    return channel;
  }

  @Override
  public void delete(String id) {
    List<Channel> channels = FileUtil.loadAllFromFile(CHANNEL_FILE, Channel.class);
    Channel targetChannel = channels.stream()
        .filter(c -> c.getUUID().equals(id))
        .findAny()
        .orElseThrow(() -> new ChannelNotFoundException());
    channels.remove(targetChannel);
    FileUtil.saveAllToFile(CHANNEL_FILE, channels);
  }

  @Override
  public void clear() {
    File file = new File(CHANNEL_FILE);
    if (file.exists()) {
      file.delete();
    }
  }
}
