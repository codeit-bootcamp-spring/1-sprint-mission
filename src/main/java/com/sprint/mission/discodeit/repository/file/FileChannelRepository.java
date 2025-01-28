package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BaseChannel;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.util.FileUtil;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.CHANNEL_FILE;

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
  public BaseChannel save(BaseChannel baseChannel) {
    List<BaseChannel> channels = FileUtil.loadAllFromFile(CHANNEL_FILE, BaseChannel.class);
    channels.add(baseChannel);
    FileUtil.saveAllToFile(CHANNEL_FILE, channels);
    return baseChannel;
  }

  @Override
  public Optional<BaseChannel> findById(String id) {
    List<BaseChannel> channels = FileUtil.loadAllFromFile(CHANNEL_FILE, BaseChannel.class);
    return channels.stream().filter(c -> c.getUUID().equals(id)).findAny();
  }

  @Override
  public List<BaseChannel> findAll() {
    return FileUtil.loadAllFromFile(CHANNEL_FILE, BaseChannel.class);
  }

  @Override
  public BaseChannel update(BaseChannel channel) {
    List<BaseChannel> channels = FileUtil.loadAllFromFile(CHANNEL_FILE, BaseChannel.class);
    BaseChannel targetChannel = channels.stream()
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
    List<BaseChannel> channels = FileUtil.loadAllFromFile(CHANNEL_FILE, BaseChannel.class);
    BaseChannel targetChannel = channels.stream()
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
