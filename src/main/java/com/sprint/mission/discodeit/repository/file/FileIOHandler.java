package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Entity;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.HashMap;
import java.util.UUID;

//todo IO핸들러를 싱글톤객체로 호출하고있는데, Bean에 넣고 꺼내쓰는게 낫나?
@Component
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
    public boolean serializeHashMap(HashMap<UUID, ? extends Entity> entityMap, String fileName) {
        if (entityMap == null || fileName == null) {
            return false;
        }
        String filePath = System.getProperty("user.dir") + "\\serFiles\\" + fileName + ".ser";
        try (FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(entityMap);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //역직렬화 성공여부에 따라 해쉬맵 or null 반환
    public HashMap<UUID, ? extends Entity> deserializeHashMap(String fileName) {
        if (fileName == null) {
            return null;
        }
        String filePath = System.getProperty("user.dir") + "\\serFiles\\" + fileName + ".ser";
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (HashMap<UUID, ? extends Entity>) ois.readObject();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    //public BufferedImage loadImage(String imagePath) {

    //}

}
