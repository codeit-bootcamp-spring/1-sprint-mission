package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// UserService delete 조건 때문에 미리 생성
public class FileBinaryContentRepository implements BinaryContentRepository {
    // 저장할 폴더 위치
    private final Path DIRECTORY;
    // 확장자 명
    private final String EXTENSION = ".ser";

    public FileBinaryContentRepository(){
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"),"file-data-map", "BinaryContent-data");

        if(Files.notExists(DIRECTORY)){
            try{
                Files.createDirectories(DIRECTORY);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID binaryContentId){
        return DIRECTORY.resolve(binaryContentId + EXTENSION);
    }


    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Path path = resolvePath(binaryContent.getId());

        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                ){
            oos.writeObject(binaryContent);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID binaryContentId) {
        BinaryContent binaryContentNullable = null;
        Path path = resolvePath(binaryContentId);
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ){
                binaryContentNullable = (BinaryContent) ois.readObject();

            }catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(binaryContentNullable);
    }

    @Override
    public Optional<BinaryContent> findByUserId(UUID userId) {
        try{
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map( path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                                ){
                            return (BinaryContent) ois.readObject();
                        }catch (IOException | ClassNotFoundException e){
                            throw new RuntimeException("Error reading BinaryContent : " + path, e);
                        }
                    })
                    // binaryContent가 null이 아니고, binaryContent의 userId가 null이 아니며,
                    // binaryContent의 userId가 현재 찾고 있는 userId와 동일할 때
                    .filter(binaryContent -> binaryContent != null && binaryContent.getUserId() != null && binaryContent.getUserId().equals(userId))
                    .findFirst();
        }catch (IOException e){
            throw new RuntimeException("Error Directroy : " + DIRECTORY, e);
        }
    }

    @Override
    public List<BinaryContent> findAll() {
        try{
            return Files.list(DIRECTORY)
                    .filter( path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ){
                            return (BinaryContent) ois.readObject();
                        }catch(IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    }).toList();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UUID binaryContentId) {
        Path path = resolvePath(binaryContentId);
        try{
            Files.delete(path);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
