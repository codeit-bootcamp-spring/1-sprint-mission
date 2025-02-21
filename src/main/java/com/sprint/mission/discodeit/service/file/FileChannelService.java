/*
package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.FindChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
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
  public Channel createChannel(Channel channel) {
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
  public FindChannelResponseDto getChannelById(String channelId){
    List<Channel> channels = loadAllChannel();
    for(Channel bc : channels){
      if(bc.getId().equals(channelId)) return Optional.of(bc);
    }
    return Optional.empty();
  }

  @Override
  public List<FindChannelResponseDto> findAllChannelsByUserId(String userId) {
    return loadAllChannel();
  }

  @Override
  public List<Channel> getChannelsByCategory(String categoryId) {
    return null;
  }

  @Override
  public void updateChannel(ChannelUpdateDto updatedChannel) {
    List<Channel> channels = loadAllChannel();

    Channel channel = channels.stream().filter(c -> c.getId().equals(channelId)).findAny().orElseThrow(() -> new ChannelValidationException());

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
    List<Channel> channels = loadAllChannel();
    Channel channel = channels.stream().filter(c -> c.getId().equals(channelId)).findAny().orElseThrow(() -> new ChannelValidationException());

    channels.remove(channel);
    saveChannelToFile(channels);
  }

  @Override
  public String generateInviteCode(Channel channel) {
    return null;
  }

  @Override
  public void setPrivate(Channel channel) {

  }

  @Override
  public void setPublic(Channel channel) {

  }

  private boolean checkIfChannelNameIsEmpty(String channelName) {
    return !channelName.isEmpty();
  }

  private boolean checkIfUserIsOwner(Channel channel, User user) {
    return true;
  }

  private void saveChannelToFile(Channel channel) throws FileException {
    List<Channel> channels = loadAllChannel();
    channels.add(channel);
    saveChannelToFile(channels);
  }

  private void saveChannelToFile(List<Channel> channels) {
    try (
        FileOutputStream fos = new FileOutputStream(CHANNEL_FILE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
    ) {
      for (Channel channel : channels) {
        oos.writeObject(channel);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<Channel> loadAllChannel(){
    List<Channel> channelList = new ArrayList<>();
    try (
        FileInputStream fos = new FileInputStream(CHANNEL_FILE);
        ObjectInputStream ois = new ObjectInputStream(fos)
    ) {
      while (true) {
        try {
          Channel channel = (Channel) ois.readObject();
          channelList.add(channel);
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
*/
