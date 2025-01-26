package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.file.FileBasic;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileChannelRepository implements ChannelRepository {
  private Path fileDirectory;
  private FileBasic chnnelFileBasic;

  public FileChannelRepository(Path directory) {
    this.fileDirectory = directory;
    this.chnnelFileBasic = new FileBasic(fileDirectory);
  }

  @Override
  public void creat(Channel channel) {
    Path filePath = fileDirectory.resolve(channel.getId().toString().concat(".ser"));
    try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
        ObjectOutputStream oos = new ObjectOutputStream(fos);
    ) {
      oos.writeObject(channel);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void delete(UUID channelId) {
    try {
      Optional<Path> channelDelete = Files.list(fileDirectory)
          .filter(path -> path.getFileName().toString().equals(channelId + ".ser"))
          .findFirst();
      if(channelDelete.isPresent()) {
        Files.delete(channelDelete.get());
      }
      else {
        System.out.println("File not found for Id");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void update(UUID channelId, String title) {
    try {
      Optional<Path> updateChannel = Files.list(fileDirectory)
          .filter(path -> path.getFileName().toString().equals(channelId + ".ser"))
          .findFirst();
      if(updateChannel.isPresent()) {
        Channel channel = (Channel) chnnelFileBasic.deserialization(updateChannel.get());
        channel.updateTitle(title);
        creat(channel);
      }
      else {
        System.out.println("File not found for Id");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public UUID findByTitle(String title) {
    List<Channel> channelList = chnnelFileBasic.load();
    try {
      return channelList.stream()
          .filter(channel -> channel.getTitle().equals(title)).findFirst().get().getId();
    }
    catch (Exception e) {
      System.out.println("There is no user with that name.");
      return null;
    }
  }

  @Override
  public List<Channel> findByAll() {
    List<Channel> channelList = chnnelFileBasic.load();
    return channelList.stream()
        .map(Channel::new)
        .collect(Collectors.toList());
  }

}
