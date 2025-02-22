package com.sprint.mission.discodeit.util;

import java.io.*;
import java.util.HashMap;


public class SerializationUtil<K, V> {
  private static final String FILE_PATH = "record.ser";
  // Java에서는 기본 생성자를 명시적으로 작성해주지 않아도 자동으로 제공함

  // 파일에 객체 직렬화하기
  public void saveData(HashMap<K, V> data){
    try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
         ObjectOutputStream oos = new ObjectOutputStream(fos);
    ) {
      oos.writeObject(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 파일에 객체 역직렬화하기
  public HashMap<K, V> loadData(){
    try (FileInputStream fis = new FileInputStream(FILE_PATH);
         ObjectInputStream ois = new ObjectInputStream(fis)) {
      return (HashMap<K, V>) ois.readObject(); // Object 타입으로 반환하므로 (List<T>)로 캐스팅해주고 있음.
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return new HashMap<>();
    }
  }
}
