package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Entity;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
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
    public boolean serializeHashMap(HashMap<UUID, ? extends Entity> entityMap, String fileName) throws IOException {
        if (entityMap == null || fileName == null) {
            throw new NullPointerException("IO 핸들러에 전달된 엔티티맵 혹은 파일이름이 null인 상태입니다.");
        }
        String filePath = System.getProperty("user.dir") + "\\serFiles\\" + fileName + ".ser";
        try (FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(entityMap);
            return true;
        } catch (IOException e) {
            throw new IOException("엔티티맵 직렬화에 실패했습니다. 실패한 파일 경로", e);
        }
    }

    //역직렬화 성공여부에 따라 해쉬맵 or null 반환
    public HashMap<UUID, ? extends Entity> deserializeHashMap(String fileName) throws IOException {
        if (fileName == null) {
            throw new NullPointerException("IO 핸들러에 전달된 파일이름이 null인 상태입니다.");
        }
        String filePath = System.getProperty("user.dir") + "\\serFiles\\" + fileName + ".ser";
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (HashMap<UUID, ? extends Entity>) ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            throw new IOException("엔티티맵 역직렬화 실패", e);
        }
    }

    //해쉬맵과 파일이름 받아서 직렬화. 직렬화 수행여부 리턴
    public boolean serializeLinkedHashMap(LinkedHashMap<UUID, ? extends Entity> entityMap, String fileName) throws IOException {
        if (entityMap == null || fileName == null) {
            throw new NullPointerException("IO 핸들러에 전달된 엔티티맵 혹은 파일이름이 null인 상태입니다.");
        }
        String filePath = System.getProperty("user.dir") + "\\serFiles\\" + fileName + ".ser";
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(entityMap);
            return true;
        } catch (IOException e) {
            throw new IOException("엔티티맵 직렬화에 실패했습니다. 실패한 파일 경로", e);
        }
    }

    //역직렬화 성공여부에 따라 해쉬맵 or null 반환
    public LinkedHashMap<UUID, ? extends Entity> deserializeLinkedHashMap(String fileName) throws IOException {
        if (fileName == null) {
            throw new NullPointerException("IO 핸들러에 전달된 파일이름이 null인 상태입니다.");
        }
        String filePath = System.getProperty("user.dir") + "\\serFiles\\" + fileName + ".ser";
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (LinkedHashMap<UUID, ? extends Entity>) ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            throw new IOException("엔티티맵 역직렬화 실패", e);
        }
    }

    //파일 삭제하기
    public boolean deleteFile(String filePath){
        if (filePath == null) {
            throw new NullPointerException("IO 핸들러에 전달된 파일경로가 null인 상태입니다.");
        }
        File file = new File(System.getProperty("user.dir") + "\\" + filePath);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("파일 삭제 성공!");
                return true;
            } else {
                System.out.println("파일 삭제 실패");
                return false;
            }
        } else {
            System.out.println("파일 삭제 실패. 해당 파일이 존재하지 않습니다. ");
            return false;
        }
    }

    //이미지 불러오기.
    public BufferedImage loadImage(String imagePath) throws IOException {
        if (imagePath == null) {
            throw new NullPointerException("IO 핸들러에 전달된 이미지경로가 null인 상태입니다.");
        }

        try {
            File image = new File(imagePath);
            return ImageIO.read(image);
        } catch (IOException e) {
            throw new IOException("이미지 불러오기 중 오류가 발생했습니다", e);
        }
    }

    //이미지 저장하기
    public boolean saveImage(BufferedImage image, String imagePath) throws IOException{
        if (image == null || imagePath == null) {
            return false;
        }
        try {
            File outputImage = new File(imagePath);
            ImageIO.write(image, "jpg",outputImage);
            return true;
        } catch (IOException e) {
            throw new IOException("이미지 불러오기 중 오류가 발생했습니다", e);
        }
    }

}
