package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.BaseChannel;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.VoiceChannel;
import com.sprint.mission.discodeit.exception.ChannelValidationException;
import com.sprint.mission.discodeit.exception.FileException;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

import static com.sprint.mission.discodeit.constant.FileConstant.CHANNEL_FILE;

public class FileChannelService implements ChannelService {

  private static volatile FileChannelService fileRepository;

  private FileChannelService() {
  }

  public static FileChannelService getInstance() {
    if (fileRepository == null) {
      synchronized (FileChannelService.class) {
        if (fileRepository == null) {
          fileRepository = new FileChannelService();
          initFile();
        }
      }
    }
    return fileRepository;
  }

  private static void initFile() {
    File file = new File(CHANNEL_FILE);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        throw new RuntimeException("파일 생성중 오류." + e.getMessage());
      }
    }
  }

  @Override
  public BaseChannel createChannel(BaseChannel channel) {
    if (!checkIfChannelNameIsEmpty(channel.getChannelName())) {
      throw new ChannelValidationException();
    }
    try {
      saveChannelToFile(channel);
    } catch (FileException e) {
      e.printStackTrace();
    }

    return channel;
  }

  @Override
  public Optional<BaseChannel> getChannelById(String channelId){
    List<BaseChannel> channels = loadAllChannel();
    for(BaseChannel bc : channels){
      if(bc.getUUID().equals(channelId)) return Optional.of(bc);
    }
    return Optional.empty();
  }

  @Override
  public List<BaseChannel> getAllChannels() {
    return loadAllChannel();
  }

  @Override
  public List<BaseChannel> getChannelsByCategory(String categoryId) {
    return null;
  }

  @Override
  public void updateChannel(String channelId, ChannelUpdateDto updatedChannel) {
    List<BaseChannel> channels = loadAllChannel();

    BaseChannel channel = channels.stream().filter(c -> c.getUUID().equals(channelId)).findAny().orElseThrow(() -> new ChannelValidationException());

    synchronized (channel) {
      updatedChannel.getChannelName().ifPresent(channel::setChannelName);
      updatedChannel.getMaxNumberOfPeople().ifPresent(channel::setMaxNumberOfPeople);
      updatedChannel.getTag().ifPresent(channel::setTag);
      updatedChannel.getIsPrivate().ifPresent(channel::updatePrivate);
    }
    saveChannelToFile(channels);
  }

  @Override
  public void deleteChannel(String channelId) {
    List<BaseChannel> channels = loadAllChannel();
    BaseChannel channel = channels.stream().filter(c -> c.getUUID().equals(channelId)).findAny().orElseThrow(() -> new ChannelValidationException());

    channels.remove(channel);
    saveChannelToFile(channels);
  }

  @Override
  public String generateInviteCode(BaseChannel channel) {
    return null;
  }

  @Override
  public void setPrivate(BaseChannel channel) {

  }

  @Override
  public void setPublic(BaseChannel channel) {

  }

  private boolean checkIfChannelNameIsEmpty(String channelName) {
    return !channelName.isEmpty();
  }

  private boolean checkIfUserIsOwner(BaseChannel channel, User user) {
    return true;
  }

  private void saveChannelToFile(BaseChannel channel) throws FileException {
    List<BaseChannel> channels = loadAllChannel();
    channels.add(channel);
    saveChannelToFile(channels);
  }

  private void saveChannelToFile(List<BaseChannel> channels) {
    try (
        FileOutputStream fos = new FileOutputStream(CHANNEL_FILE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
    ) {
      for (BaseChannel channel : channels) {
        oos.writeObject(channel);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<BaseChannel> loadAllChannel(){
    List<BaseChannel> channelList = new ArrayList<>();
    try (
        FileInputStream fos = new FileInputStream(CHANNEL_FILE);
        ObjectInputStream ois = new ObjectInputStream(fos)
    ) {
      while (true) {
        try {
          BaseChannel baseChannel = (BaseChannel) ois.readObject();
          channelList.add(baseChannel);
        } catch (EOFException e) {
          break;
        } catch (ClassNotFoundException e) {
          throw new RuntimeException("직렬화된 객체 클래스가 존재하지 않습니다.");
        }
      }
    } catch (IOException e) {
      return channelList;
    }
    return channelList;
  }
}
