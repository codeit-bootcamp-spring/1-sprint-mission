package com.sprint.mission.discodeit.repository.file;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileSerializationUtil {

    // 객체를 파일에 저장하는 메서드 (직렬화)
    public static <T> void saveToSer(String fileName, Map<UUID, T> dataMap) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(dataMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일에서 객체를 로드하는 메서드 (역직렬화)
    //readObject()의 반환 타입이 Object이므로 Map<UUID, T>로 캐스팅할 때 컴파일러가 타입 안전성을 보장할 수 없기 때문에 경고가 발생합니다.
    //경고가 사라지지만, 런타임에 실제 타입이 다르면 ClassCastException이 발생할 수 있음을 주의해야 합니다.
    @SuppressWarnings("unchecked")
    public static <T> Map<UUID, T> loadFromSer(String fileName) {
        Map<UUID, T> map = new HashMap<>();
        File file = new File(fileName);

        if (!file.exists() || file.length() == 0) {
            return map;  // 파일이 없거나 크기가 0이면 빈 맵 반환
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            map = (Map<UUID, T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return map;
    }
}
