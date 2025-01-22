package com.sprint.mission.discodeit.repository.file;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileUserRepository implements UserRepository {

    /**
    [ ]  JCF 대신 FileIO와 객체 직렬화를 활용해 메소드를 구현하세요.
    JCF 이용하지 않고, 각 도메인 모델 이름(users, chnnels, messages)을 딴
    폴더에 ser파일을 개별 객체로 저장합니다.
    **/
    
    // 폴더 주소
    private final String USERS_PATH = "users/";

    public void FileUserService(){
        // 초기화 시 파일의 존재 유무 확인
        File dir = new File(USERS_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // FileIO를 통해서 save
    public void saveUser(User user){
        //System.out.println("3             " + user.getId());
        String filePath = USERS_PATH + user.getId() + ".ser";
        //System.out.println("4             " + user.getId());
        //System.out.println("Saving user to: " + filePath); // 파일 경로 로그 추가
        try {
            try (FileOutputStream fos = new FileOutputStream(filePath, false); // false는 덮어쓰기 모드
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(user); // 사용자 객체 직렬화
            }
        } catch (IOException e) {
            e.printStackTrace(); // IOException 처리
        }
    }

    // FileIO를 통해서 load
    public User findUserById(UUID id){
        String filePath = USERS_PATH + id + ".ser";
        //System.out.println("Loading user from: " + filePath); // 로드 경로 로그 추가

        if(!Files.exists(Paths.get(filePath))){
            //System.out.println("User file not found: " + filePath);
            return null;
        }else{
            //System.out.println(filePath);
        }

        try(FileInputStream fis = new FileInputStream(filePath); // FileInputStream을 사용하여 파일을 읽고
            ObjectInputStream ois = new ObjectInputStream(fis)){ // ObjectInputStream을 사용하여 직렬화된 객체를 역직렬화
            // 여기서 무슨 일이 벌어지는 거 같은데?
            // 역직렬화 후 객체의 상태를 출력하여 확인
            User user = (User) ois.readObject();

            //System.out.println("Deserialized User ID: " + user.getId());
            //System.out.println("Deserialized User Nickname: " + user.getNickname());
            //System.out.println( "findUserById   역직렬화 이후  user.getId()   : " + user.getId());
            return user; // 역직렬화된 객체는 User 타입으로 반환
        } catch (IOException | ClassNotFoundException e){ // 만약 예외가 발생하면 null을 반환하고, 예외를 처리합니다.
            e.printStackTrace();
            return null;
        }
    }

    // 스프린트 미션 1, 피드백 이후 인터페이스를 제작했었습니다(그래서 추상 클래스 반환이 Map으로 설정돼있음)
    // 시간 문제 상 변경이 어려워서 전달만 Map으로 합니다
    public Map<UUID, User> getAllUsers(){
        Map<UUID, User> userMap = new HashMap<>();
        // USERS_PATH 아래 모든 파일 들고오기
        // findUserById 로 User을 들고와야 한다.
        File userDir = new File("users");

        if(userDir.exists() && userDir.isDirectory()){
            File[] files = userDir.listFiles(); // 모든 파일의 주소 반환
            if(files != null){
                for(File file : files){
                    // 파일이 파일이고, file의 이름 마지막이 ".ser"로 끝난다면
                    if(file.isFile() && file.getName().endsWith(".ser")){
                        // fromString 문자열 -> UUID
                        UUID id = UUID.fromString(file.getName().replace(".ser", ""));
                        userMap.put(id, findUserById(id));
                    }
                }
            }
        }
        return userMap;
    }

    // 삭제
    public void deleteAllUsers(){
        File file = new File("users/");
        File[] fileList = file.listFiles();
        for(File fileName : fileList){
            fileName.delete();
        }
        System.out.println("deleteAllUsers 삭제 완료");
    }
    public void deleteUserById(UUID id){
        String fileName = "users/" + id + ".ser";
        File userFile = new File(fileName);
        userFile.delete();
        System.out.println("deleteUserById 삭제 완료");
    }
}
