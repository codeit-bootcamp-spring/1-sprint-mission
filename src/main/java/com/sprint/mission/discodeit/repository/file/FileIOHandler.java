package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
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

    //이 메서드만 우선 IO예외 캐치해서 여기서 죽임.
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

    //이 메서드만 우선 IO예외 캐치해서 여기서 죽임.
    //이미지 불러오기.
    public BufferedImage loadImage(String imagePath){
        if (imagePath == null) {
            System.out.println("이미지 로드 실패. IO핸들러에 null이 전달되었습니다.");
            return null;
        }
        try {
            File image = new File(imagePath);
            return ImageIO.read(image);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("이미지 로드 실패.");
            return null;
        }
    }

    //이 메서드만 우선 IO예외 캐치해서 여기서 죽임.
    //바이너리컨텐츠 객체 직렬화
    public boolean serializeBinaryContent(BinaryContent binaryContent, String binaryContentPath){
        if (binaryContent == null || binaryContentPath == null) {
            return false;
        }
        try (FileOutputStream fos = new FileOutputStream(binaryContentPath);
             ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(binaryContent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("BinaryContent 직렬화에 실패했습니다. 실패한 파일 경로: " + binaryContentPath);
            return false;
        }
    }

    //이 메서드만 우선 IO예외 캐치해서 여기서 죽임.
    //역직렬화 성공여부에 따라 바이너리컨텐츠 or null 반환
    public BinaryContent deserializeBinaryContent(String binaryContentPath){
        if (binaryContentPath == null) {
            throw new NullPointerException("IO 핸들러에 전달된 파일경로가 null인 상태입니다.");
        }
        try (FileInputStream fis = new FileInputStream(binaryContentPath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (BinaryContent) ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println("BinaryContent 직렬화에 실패했습니다. 실패한 파일 경로: " + binaryContentPath);
            return null;
        }
    }

}
