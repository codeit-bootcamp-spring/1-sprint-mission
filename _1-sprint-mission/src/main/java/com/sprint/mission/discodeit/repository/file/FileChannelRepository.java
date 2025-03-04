package com.sprint.mission.discodeit.repository.file;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Primary  // 기본적으로 사용될 구현체
public class FileChannelRepository extends AbstractFileRepository<Channel> implements ChannelRepository {

    public FileChannelRepository() {
        super("channel");
    }

    @Override
    public Channel save(Channel channel) {
        Path path = resolvePath(channel.getId());
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ){
            oos.writeObject(channel);
        }catch (IOException e){
            throw new RuntimeException("파일 저장 실패: " + path, e);
        }
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        Channel channelNullable = null;
        Path path = resolvePath(channelId);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                channelNullable = (Channel) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("파일 읽기 실패: " + path, e);
            }
        }
        return Optional.ofNullable(channelNullable);
    }

    @Override
    public List<Channel> findAll() {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (Channel) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("파일 읽기 실패: " + path, e);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("디렉토리 읽기 실패: " + DIRECTORY, e);
        }
    }


    //존재하는지 확인
    @Override
    public boolean existsById(UUID channelId) {
        Path path = resolvePath(channelId);
        return Files.exists(path);
    }

    @Override
    public void deleteById(UUID channelId) {
        Path path = resolvePath(channelId);
        try {
            Files.delete(path);
        }catch (IOException e){
            throw new RuntimeException("파일 삭제 실패: " + path, e);
        }
    }
}
