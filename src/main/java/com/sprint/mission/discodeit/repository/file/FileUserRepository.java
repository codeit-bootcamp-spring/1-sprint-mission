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
    private final String USERS_PATH = Paths.get("users").toString();

    public FileUserRepository(){
        // 초기화 시 파일의 존재 유무 확인
        File dir = new File(USERS_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // FileIO를 통해서 save
    @Override
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

    // !!! 이쯤에서 질문있습니다 !!!
    // 길어서 pull-request-template 쪽에 작성했으니 확인해주세요!
    // save 기능과 load 기능을 Message, Channel에서는 분리했는데
    // 이쪽은 제가 겪었던 오류가 있어서, 확인받고 싶어 우선은 수정하지 않고 제출합니다.

    // FileIO를 통해서 load
    @Override
    public Optional<User> findUserById(UUID id){
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
            return Optional.ofNullable(user); // 역직렬화된 객체는 User 타입으로 반환
        } catch (IOException | ClassNotFoundException e){ // 만약 예외가 발생하면 null을 반환하고, 예외를 처리합니다.
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Collection<User> getAllUsers(){
        // Map<UUID, User> userMap = new HashMap<>();
        List<User> userList = new ArrayList<>();
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
                        //userMap.put(id, findUserById(id).orElse(null));
                        userList.add(findUserById(id).orElse(null));
                    }
                }
            }
        }
        return userList.isEmpty() ? null : userList;
    }

    // 삭제
    @Override
    public void deleteAllUsers(){
        File file = new File("users/");
        File[] fileList = file.listFiles();
        for(File fileName : fileList){
            fileName.delete();
        }
        System.out.println("deleteAllUsers 삭제 완료");
    }

    @Override
    public void deleteUserById(UUID id){
        String fileName = "users/" + id + ".ser";
        File userFile = new File(fileName);
        if(userFile.delete()){
            System.out.println("deleteUserById 삭제 완료");
        }
    }
}
