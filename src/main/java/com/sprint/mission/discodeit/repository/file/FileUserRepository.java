package com.sprint.mission.discodeit.repository.file;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUserRepository implements UserRepository {

    /**
    [ ]  JCF 대신 FileIO와 객체 직렬화를 활용해 메소드를 구현하세요.
    JCF 이용하지 않고, 각 도메인 모델 이름(users, chnnels, messages)을 딴
    폴더에 ser파일을 개별 객체로 저장합니다.
    **/
    
    // 폴더 주소
    private final Path USERS_PATH;
    private final String EXETENSION = ".ser";

    public FileUserRepository(){
        // 서치해보니 File.separator 도 좋지만, Path.get도 가독성이 좋다고 하여 채택하여 사용했습니다!
        this.USERS_PATH = Paths.get(System.getProperty("user.dir"), "file-data-map", "crs", User.class.getSimpleName());
        if(Files.notExists(USERS_PATH)){
            try{
                Files.createDirectories(USERS_PATH);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    public User saveFile(Path path, User user){
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user;
    }


    public User loadFile(Path path){
        User userNullable = null;
        if(Files.exists(path)){
            try(FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)) {
                userNullable = (User) ois.readObject();
            }catch (IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        return userNullable;
    }

    private Path resolvePath(UUID id) {
        return USERS_PATH.resolve(id + EXETENSION);
    }

    // FileIO를 통해서 save
    @Override
    public User saveUser(User user){
        Path path = resolvePath(user.getId());
        return saveFile(path, user);
    }

    // FileIO를 통해서 load
    @Override
    public Optional<User> findUserById(UUID id){
        Path path = resolvePath(id);
        return Optional.ofNullable(loadFile(path));
    }

    @Override
    public Collection<User> getAllUsers(){
        // USERS_PATH 는 생성자에서 "없으면 생성하라" 라고 하고 있어서
        // 검증 로직은 빼기로 했습니다!
        // 혹시 그럼에도 불구하고 필요할까요?
        try{
            return Files.list(USERS_PATH) // PATH 가 디렉토리가 아닐 경우 자동으로 NotDirectoryEX을 던진다.
                    .filter(path -> path.toString().endsWith(EXETENSION)) // 디렉토리에 해당 조건을 만족하는 파일이 없다면 빈 리스트가 반환된다
                    .map(this::loadFile)
                    .toList();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUserById(UUID id){
        Path path =resolvePath(id);
        try{
            Files.deleteIfExists(path);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
