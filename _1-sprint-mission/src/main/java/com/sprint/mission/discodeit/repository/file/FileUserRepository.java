package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Repository
@Primary  // 기본적으로 사용될 구현체
public class FileUserRepository extends AbstractFileRepository<User> implements UserRepository {

    public FileUserRepository() {
        super("user");
    }

    //생성,업데이트
    @Override
    public User save(User user){
        Path path = resolvePath(user.getId()); // 파일 경로 결정
        try(
            FileOutputStream fos = new FileOutputStream(path.toFile()); //데이터를 출력
            ObjectOutputStream oos = new ObjectOutputStream(fos); // 객체를 직렬화해서 파일에 저장
        ){
            oos.writeObject(user); //객체를 직렬화하여 파일에 씀
        }catch(IOException e){
            throw new RuntimeException("파일 저장 실패: " + path, e);
        }
        return user;
    }
    //단일조회
    @Override
    public Optional<User> findById(UUID userId){
        User userNullable = null;
        Path path = resolvePath(userId);
        if(Files.exists(path)){ //파일이 존재하는 경우에만 읽기 실행
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());//해당 파일을 읽기 위한 스트림 생성
                    ObjectInputStream ois = new ObjectInputStream(fis) //직렬화된 데이터를 역직렬화(파일->객체)
            ){
                userNullable = (User) ois.readObject();
            }catch (IOException|ClassNotFoundException e){
                throw new RuntimeException("파일 읽기 실패: " + path, e);
            }
        }
        return Optional.ofNullable(userNullable);
    }
    @Override
    public Optional<User> findByUsername(String username) {
        return findAll().stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findAll().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }
    //다중조회
    @Override
    public List<User> findAll(){
        try{
            return Files.list(DIRECTORY)//경로에 있는 파일 목록을 스트림 형태로 반환
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                                ){
                            return (User)ois.readObject();
                        }catch (IOException | ClassNotFoundException e){
                            throw new RuntimeException("파일 읽기 실패: " + path, e);
                        }
                    })
                    .toList();
        }catch (IOException e){
            throw new RuntimeException("디렉토리 읽기 실패: " + DIRECTORY, e);
        }
    }

    //존재하는지 확인
    @Override
    public boolean existsById(UUID id) {
        Path path = resolvePath(id);
        return Files.exists(path);
    }
    @Override
    public boolean existsByEmail(String email) {
        return this.findAll().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.findAll().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }
    //삭제
    @Override
    public void deleteById(UUID userId){
        Path path = resolvePath(userId);
        try{
            Files.delete(path);
        }catch (IOException e){
            throw new RuntimeException("파일 삭제 실패: " + path, e);
        }
    }


}
