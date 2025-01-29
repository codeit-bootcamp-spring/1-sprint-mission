package com.sprint.mission.discodeit.service.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBasic {
  private Path fileDirectory;

  public FileBasic(Path directory) {
    this.fileDirectory = directory;
    if (!Files.exists(directory)) {
      try {
        Files.createDirectories(directory);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public <T> List<T> load() {
    if (Files.exists(fileDirectory)) {
      try {
        List<T> list = Files.list(fileDirectory)
            .map(path -> {
              try (
                  FileInputStream fis = new FileInputStream(path.toFile());
                  ObjectInputStream ois = new ObjectInputStream(fis)
              ) {
                Object data = ois.readObject();
                return (T) data;
              } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
              }
            })
            .toList();
        return list;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      return new ArrayList<>();
    }
  }

  public Object deserialization(Path path) {
    try (FileInputStream fis = new FileInputStream(path.toFile());
        ObjectInputStream ois = new ObjectInputStream(fis)) {
      return ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

}
