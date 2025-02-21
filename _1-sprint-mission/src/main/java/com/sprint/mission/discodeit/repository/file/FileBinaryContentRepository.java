package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    //데이터 저장
    private final Path DIRECTORY; // 데이터를 저장할 디렉토리 경로
    private final String EXTENSION = ".ser"; // 파일확장자를 .ser로 설정하여 직렬화된 파일임을 나타냄
    //생성자
    public FileBinaryContentRepository() {
        //파일 저장 디렉토리 지정
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"),"file-data-map", BinaryContent.class.getSimpleName());
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
    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Path path = resolvePath(binaryContent.getId()); // 파일 경로 결정
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile()); //데이터를 출력
                ObjectOutputStream oos = new ObjectOutputStream(fos); // 객체를 직렬화해서 파일에 저장
        ){
            oos.writeObject(binaryContent); //객체를 직렬화하여 파일에 씀
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID binaryContentId) {
        BinaryContent binaryContent = null;
        Path path = resolvePath(binaryContentId);
        if(Files.exists(path)){ //파일이 존재하는 경우에만 읽기 실행
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());//해당 파일을 읽기 위한 스트림 생성
                    ObjectInputStream ois = new ObjectInputStream(fis) //직렬화된 데이터를 역직렬화(파일->객체)
            ){
                binaryContent = (BinaryContent) ois.readObject();
            }catch (IOException|ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(binaryContent);
    }

    @Override
    public List<BinaryContent> findAllById(List<UUID> binaryContentIds) {
        try{
            return Files.list(DIRECTORY)//경로에 있는 파일 목록을 스트림 형태로 반환
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ){
                            return (BinaryContent)ois.readObject();
                        }catch (IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(binaryContent -> binaryContentIds.contains(binaryContent.getId()))
                    .toList();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(UUID binaryContentId) {
        Path path = resolvePath(binaryContentId);
        try{
            Files.delete(path);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(UUID binaryContentId) {
        Path path = resolvePath(binaryContentId);
        return Files.exists(path);
    }
}
