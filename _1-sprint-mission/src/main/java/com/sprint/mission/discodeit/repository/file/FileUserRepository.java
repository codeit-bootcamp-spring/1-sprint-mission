package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Repository
public class FileUserRepository implements UserRepository {
    //데이터 저장
    private final Path DIRECTORY; // 데이터를 저장할 디렉토리 경로
    private final String EXTENSION = ".ser"; // 파일확장자를 .ser로 설정하여 직렬화된 파일임을 나타냄
    //생성자
    public FileUserRepository() {
        //파일 저장 디렉토리 지정
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"),"file-data-map",User.class.getSimpleName());
        //디렉토리가 없으면 생성하여 파일 생성
        if(Files.notExists(DIRECTORY)){
            try{
                Files.createDirectories(DIRECTORY);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
    //주어진 UUID를 이용해 해당 User 객체의 파일 경로를 반환
    private Path resolvePath(UUID id){
        return DIRECTORY.resolve(id+EXTENSION);
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
            throw new RuntimeException(e);
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
                throw new RuntimeException(e);
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
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAllById(List<UUID> uuids) {
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
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(user -> uuids.contains(user.getId()))
                    .toList();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    //존재하는지 확인
    @Override
    public boolean existsById(UUID id) {
        Path path = resolvePath(id);
        return Files.exists(path);
    }
    //삭제
    @Override
    public void deleteById(UUID userId){
        Path path = resolvePath(userId);
        try{
            Files.delete(path);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }


}
