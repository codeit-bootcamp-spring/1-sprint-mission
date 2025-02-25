package com.sprint.mission.discodeit.util;

import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class FileIO {
  public void saveToFile(File file, Serializable object) {
    try (
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos)
    ) {
      oos.writeObject(object);
    } catch (IOException e) {
      throw new RuntimeException("파일 저장 중 오류 발생: " + file, e);
    }
  }
  
  public <T> T loadFromFile(File file, Class<T> type) {
    if (!file.exists()) {
      throw new NoSuchElementException("file not found: " + file);
    }
    try (
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis)
    ) {
      return type.cast(ois.readObject());
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException("파일 로드 중 오류 발생: " + file, e);
    }
  }
  
  public <T> List<T> loadAllFromDirectory(Path directory, String extension, Class<T> type) {
    List<T> result = new ArrayList<>();
    if (!Files.exists(directory)) {
      return result;
    }
    try {
      Files.list(directory)
          .filter(path -> path.toString().endsWith(extension))
          .forEach(path -> {
            T t = loadFromFile(path.toFile(), type);
            result.add(t);
          });
    } catch (IOException e) {
      throw new RuntimeException("디렉터리에서 파일 로드 중 오류 발생: " + directory, e);
    }
    return result;
  }
  
  public void deleteFile(File file) {
    if (!file.exists()) {
      return;
    }
    try {
      Files.delete(file.toPath());
    } catch (IOException e) {
      throw new RuntimeException("파일 삭제 중 오류 발생: " + file, e);
    }
  }
}
