package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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
public class FileBinaryContentRepository extends AbstractFileRepository<BinaryContent> implements BinaryContentRepository {

    public FileBinaryContentRepository() {
        super("binary-content");
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
            throw new RuntimeException("파일 저장 실패: " + path, e);
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
                throw new RuntimeException("파일 읽기 실패: " + path, e);
            }
        }
        return Optional.ofNullable(binaryContent);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (BinaryContent) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("파일 읽기 실패: " + path, e);
                        }
                    })
                    .filter(content -> binaryContentIds.contains(content.getId()))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("디렉토리 읽기 실패: " + DIRECTORY, e);
        }
    }


    @Override
    public void deleteById(UUID binaryContentId) {
        Path path = resolvePath(binaryContentId);
        try{
            Files.delete(path);
        }catch (IOException e){
            throw new RuntimeException("파일 삭제 실패: " + path, e);
        }
    }

    @Override
    public boolean existsById(UUID binaryContentId) {
        Path path = resolvePath(binaryContentId);
        return Files.exists(path);
    }
}
