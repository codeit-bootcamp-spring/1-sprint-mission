package com.sprint.mission.discodeit.util;

import java.io.*;
import java.util.List;
import java.util.ArrayList;


public class SerializationUtil<T> {
  private static final String FILE_PATH = "record.ser";
  // Java에서는 기본 생성자를 명시적으로 작성해주지 않아도 자동으로 제공함

  // 파일에 객체 직렬화하기
  public void saveData(List<T> data){
    try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
         ObjectOutputStream oos = new ObjectOutputStream(fos);
    ) {
      oos.writeObject(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 파일에 객체 역직렬화하기
  public List<T> loadData(){
    try (FileInputStream fis = new FileInputStream(FILE_PATH);
         ObjectInputStream ois = new ObjectInputStream(fis)) {
      return (List<T>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }
}
