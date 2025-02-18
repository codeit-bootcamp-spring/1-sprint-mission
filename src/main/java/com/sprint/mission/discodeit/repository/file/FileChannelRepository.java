package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class FileChannelRepository implements ChannelRepository, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Path channelDirectory = Paths.get(System.getProperty("user.dir"), "/data/channel");

    public FileChannelRepository(){
        init();
    }

    // 파일 저장
    @Override
    public void save(Channel channel) {

        Path filePath = returnFilePath(channel.getId());

        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 디렉토리 안 모든 파일 가져오기
    @Override
    public Map<UUID, Channel> load() {
        if (Files.exists(channelDirectory)) {
            try {
                // 파일 목록 각각에서 파일 역직렬화
                Map<UUID, Channel> map = Files.list(channelDirectory)    // 해당 디렉터리 파일 목록 가져옴
                        .map(filePath -> {
                            try (
                                    FileInputStream fis = new FileInputStream(filePath.toFile());
                                    ObjectInputStream ois = new ObjectInputStream(fis);
                            ) {
                                Channel channel = (Channel) ois.readObject();
                                return Map.entry(channel.getId(), channel);
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                return map;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new HashMap<>();
        }
    }

    // 파일 삭제
    @Override
    public void delete(UUID id) {

        Path filePath = returnFilePath(id);

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (NoSuchFileException e) {
            System.out.println("존재하지 않는 채널입니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일 경로 초기화
    private void init() {
        if (!Files.exists(channelDirectory)) {
            try {
                Files.createDirectories(channelDirectory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // 파일 경로 가져오기
    private Path returnFilePath(UUID id) {

        return channelDirectory.resolve(id.toString().concat(".ser"));
    }
}
