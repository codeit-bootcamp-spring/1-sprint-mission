package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.exception.FileException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
  static class AppendingObjectOutputStream extends ObjectOutputStream {

    public AppendingObjectOutputStream(OutputStream out) throws IOException {
      super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException{

    }
  }

  public static <T> List<T> loadAllFromFile(String filePath, Class<T> clazz) {

    List<T> dataList = new ArrayList<>();
    File file = new File(filePath);

    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        throw new RuntimeException("Failed to create file: " + filePath, e);
      }
    }

    try (FileInputStream fis = new FileInputStream(file);
         ObjectInputStream ois = new ObjectInputStream(fis)) {

      while (true) {
        try {
          Object obj = ois.readObject();
          if (clazz.isInstance(obj)) {
            dataList.add(clazz.cast(obj));
          }
        } catch (EOFException e) {
          break;
        }
      }
    } catch (IOException | ClassNotFoundException e) {

      return dataList;
    }

    return dataList;
  }

  public static <T> void saveAllToFile(String filePath, List<T> data) {
    try (FileOutputStream fos = new FileOutputStream(filePath);
         ObjectOutputStream oos = new ObjectOutputStream(fos)) {

      for (T obj : data) {
        oos.writeObject(obj);
      }

    } catch (IOException e) {
      throw new FileException();
    }
  }

  public static <T> void appendToFile(String filePath, List<T> data){

    File file = new File(filePath);
    boolean fileExists = file.exists() && file.length() > 0;
    try(FileOutputStream fos = new FileOutputStream(filePath, true);
    ObjectOutputStream oos = fileExists ? new AppendingObjectOutputStream(fos) : new ObjectOutputStream(fos)){

      for(T obj : data){
        oos.writeObject(obj);
      }

    }catch(IOException e){
      throw new FileException();
    }
  }
}
