package com.sprint.mission.discodeit.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;

@Component
public class SerializationUtil<K, V> {

  private final String filePath;

  public SerializationUtil(@Value("${serialization.file-path:record.ser}") String filePath) {
    this.filePath = filePath;
  }

  public void saveData(HashMap<K, V> data) {
    if (data == null) {
      return;
    }
    try (FileOutputStream fos = new FileOutputStream(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public HashMap<K, V> loadData() {
    File file = new File(filePath);
    if (!file.exists()) {
      return new HashMap<>();
    }
    try (FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis)) {
      return (HashMap<K, V>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return new HashMap<>();
    }
  }
}
