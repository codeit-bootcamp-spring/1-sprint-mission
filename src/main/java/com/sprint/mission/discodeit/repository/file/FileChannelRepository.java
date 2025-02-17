package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileChannelRepository() {
        // CHANNEL 클래스를 기준으로 디렉토리 생성
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"),
                "file-data-map", Channel.class.getSimpleName());
        init(DIRECTORY);
    }
    // 디렉토리가 없다면 생성
    private void init(Path DIRECTORY){
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    // 파일 경로 생성
    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);  // 채널의 UUID를 기준으로 파일 경로 생성
    }

    @Override
    public Channel save(Channel channel) {
        Path path = resolvePath(channel.getId());  // 채널 ID에 맞는 파일 경로 생성
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(channel);  // 채널 객체를 직렬화하여 저장
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        Channel channelNullable = null;
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                channelNullable = (Channel) ois.readObject();  // 채널 객체를 역직렬화하여 반환
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(channelNullable);  // 존재하지 않으면 Optional.empty() 반환
    }

    @Override
    public List<Channel> findAll() {
        try {
            return Files.list(DIRECTORY)  // 디렉토리에서 모든 파일을 읽어오기
                    .filter(path -> path.toString().endsWith(EXTENSION))  // 확장자가 .ser인 파일만 필터링
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (Channel) ois.readObject();  // 파일을 읽어와서 채널 객체로 변환
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();  // 모든 채널을 리스트로 반환
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        Path path = resolvePath(id);
        return Files.exists(path);  // 파일이 존재하는지 확인
    }

    @Override
    public void deleteById(UUID id) {
        Path path = resolvePath(id);
        try {
            Files.delete(path);  // 해당 파일 삭제
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
