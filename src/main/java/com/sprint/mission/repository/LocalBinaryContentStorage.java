package com.sprint.mission.repository;

import com.sprint.mission.dto.request.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

//[ ]  discodeit.storage.type 값이 local 인 경우에만 Bean으로 등록되어야 합니다.
//
//Path root
//로컬 디스크의 루트 경로입니다.
//discodeit.storage.local.root-path 설정값을 정의하고, 이 값을 통해 주입합니다.
//void init()
//루트 디렉토리를 초기화합니다.
//Bean이 생성되면 자동으로 호출되도록 합니다.
//Path resolvePath(UUID)
//파일의 실제 저장 위치에 대한 규칙을 정의합니다.
//파일 저장 위치 규칙 예시: {root}/{UUID}
//put, get 메소드에서 호출해 일관된 파일 경로 규칙을 유지합니다.
//ResponseEntity<Resource> donwload(BinaryContentDto)
//get 메소드를 통해 파일의 바이너리 데이터를 조회합니다.
//BinaryContentDto와 바이너리 데이터를 활용해 ResponseEntity<Resource> 응답을 생성 후 반환합니다.

// 특정 프로퍼티 값이 설정된 경우에만 Bean 등록
//discodeit.storage.type=local 설정이 있는 경우 LocalStorageService가 빈으로 등록
@Repository
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;

    public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String root) {
        this.root = Paths.get(root);
        init();
    }

    //루트 디렉토리를 초기화합니다.
    //Bean이 생성되면 자동으로 호출되도록
    void init(){
        if (!Files.exists(root)){
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (Stream<Path> list = Files.list(root)){
                list.forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public UUID put(UUID id, byte[] content) {
        Path path = resolvePath(id);
        try {
            Files.createFile(path);
            Files.write(path, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    @Override
    public InputStream get(UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto content) {
        //content.fileName();
        return null;
    }

    /**
     * 편의
     */
    private Path resolvePath(UUID id) {
        //파일 저장 위치 규칙 예시: {root}/{UUID}
        return root.resolve(id.toString());
    }
}


