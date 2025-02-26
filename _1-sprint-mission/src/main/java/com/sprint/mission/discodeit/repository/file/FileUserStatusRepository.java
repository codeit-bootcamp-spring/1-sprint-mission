package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary  // 기본적으로 사용될 구현체
public class FileUserStatusRepository extends AbstractFileRepository<UserStatus> implements UserStatusRepository  {

    public FileUserStatusRepository() {
        super("user-status");
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Path path = resolvePath(userStatus.getId());
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(userStatus);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + path, e);
        }
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID userStatusId) {
        UserStatus userStatus = null;
        Path path = resolvePath(userStatusId);
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    ){
                userStatus = (UserStatus) ois.readObject();
            }catch (IOException | ClassNotFoundException e){
                throw new RuntimeException("파일 읽기 실패: " + path, e);
            }
        }
        return Optional.ofNullable(userStatus);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return findAll().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        try{
            return Files.list(DIRECTORY)
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path->{
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                                ){
                            return (UserStatus)ois.readObject();
                        }catch (IOException | ClassNotFoundException e){
                            throw new RuntimeException("파일 읽기 실패: " + path, e);
                        }
                    }).toList();
        }catch (IOException e){
            throw new RuntimeException("디렉토리 읽기 실패: " + DIRECTORY, e);
        }
    }

    @Override
    public void deleteById(UUID userStatusId) {
        Path path = resolvePath(userStatusId);
        try {
            Files.delete(path);
        }catch (IOException e){
            throw new RuntimeException("파일 삭제 실패: "+path,e);
        }
    }

    @Override
    public void deleteByUserId(UUID userId) {
        this.findByUserId(userId)
                .ifPresent(userStatus -> this.deleteById(userStatus.getId()));
    }

    @Override
    public boolean existsById(UUID userStatusId) {
        Path path = resolvePath(userStatusId);
        return Files.exists(path);
    }
}
