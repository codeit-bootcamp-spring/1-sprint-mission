package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Entity;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.HashMap;
import java.util.UUID;

public class FileIOHandler {

    //외부에서 생성자 접근 불가
    private FileIOHandler() {}

    // I/O 핸들러 객체 LazyHolder 싱글톤 구현.
    private static class FileIOHandlerHolder {
        private static final FileIOHandler INSTANCE = new FileIOHandler();
    }

    // 외부에서 호출 가능한 싱글톤 인스턴스.
    public static FileIOHandler getInstance() {
        return FileIOHandler.FileIOHandlerHolder.INSTANCE;
    }

    //해쉬맵과 파일이름 받아서 직렬화. 직렬화 수행여부 리턴
    public boolean serializeHashMap(HashMap<UUID, ? extends Entity> hashMap, String fileName) {
        if (hashMap == null || hashMap.isEmpty() || fileName == null) {
            return false;
        }
        try (FileOutputStream fos = new FileOutputStream(fileName+".ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);)
        {
            oos.writeObject(hashMap);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //
    public HashMap<UUID, ? extends Entity> deserializeHashMap(String fileName) {
        if (fileName == null) {
            return null;
        }
        try (FileInputStream fis = new FileInputStream(fileName+".ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            HashMap<UUID, ? extends Entity> deserializedHashMap = (HashMap<UUID, ? extends Entity>) ois.readObject();
            return deserializedHashMap;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
