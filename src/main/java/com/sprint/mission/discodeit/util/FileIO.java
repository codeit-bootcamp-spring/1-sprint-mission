package com.sprint.mission.discodeit.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIO {
  
  public void initFile(File file) {
    try {
      if (!file.exists()) {
        file.createNewFile();
      }
    } catch (Exception e) {
      throw new RuntimeException("파일 초기화에 문제가 생겼습니다.");
    }
  }
  
  public List<Serializable> loadFile(File file) {
    List<Serializable> objects = new ArrayList<>();
    while (true) {
      try (FileInputStream fis = new FileInputStream(file);
           ObjectInputStream ois = new ObjectInputStream(fis)) {
        Serializable object = (Serializable) ois.readObject();
        objects.add(object);
      } catch (EOFException e) {
        break;
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    return objects;
  }
  
  public void saveToFile(File file, Serializable object) throws FileNotFoundException {
    try (FileOutputStream fos = new FileOutputStream(file);
         ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(object);
    } catch (IOException e) {
      throw new RuntimeException("파일에 저장 중 문제가 발생했습니다.", e);
    }
  }
  
  public void updateFile(File file, List<Serializable> objects) {
    File tempFile = new File("temp.ser");
    
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
         ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tempFile))) {
      while (true) {
        try {
          Identifiable obj = (Identifiable) ois.readObject();
        } catch (EOFException e) {
          break;
        }
      }
      
      if (!file.delete() || !tempFile.renameTo(file)) {
        throw new RuntimeException("파일 업데이트 중 문제가 발생했습니다.");
      }
      
      for (Serializable object : objects) {
        oos.writeObject(object);
      }
      
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
  
  public void removeObjectFromFile(File originalFile, Identifiable object) {
    File tempFile = new File("temp.ser");
    
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(originalFile));
         ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tempFile))) {
      List<Identifiable> objects = new ArrayList<>();
      while (true) {
        try {
          Identifiable obj = (Identifiable) ois.readObject();
          objects.add(obj);
        } catch (EOFException e) {
          break;
        }
      }
      
      for (Identifiable obj : objects) {
        if (!obj.getId().equals(object.getId())) {
          oos.writeObject(obj);
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    
    if (!originalFile.delete() || !tempFile.renameTo(originalFile)) {
      throw new RuntimeException(object + " 제거 중 문제가 발생했습니다.");
    }
  }
  
}