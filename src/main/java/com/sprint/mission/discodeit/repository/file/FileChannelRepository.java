package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository extends FileRepository implements ChannelRepository {

    public FileChannelRepository(@Value("${discodeit.repository.channel}")String fileDirectory){
        super(fileDirectory);
    }
    @Override
    public void save(Channel channel) {
        saveToFile(resolvePath(channel.getId()), channel);

    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return loadFromFile(resolvePath(channelId));
    }


    @Override
    public Map<UUID, Channel> findAll() {
        Map<UUID, Channel> channelMap = new HashMap<>();
        try (Stream<Path> pathStream = Files.walk(getDIRECTORY())) {
            pathStream.filter(path -> path.toString().endsWith(".ser"))
                    .forEach(path ->{
                        Optional<Channel> channel = loadFromFile(path);
                        channel.ifPresent(ch -> channelMap.put(ch.getId(), ch));
                    });
        } catch (IOException e) {
            System.out.println("파일을 읽을 수 없습니다" + e.getMessage());
        }
        return channelMap;
    }


    @Override
    public void delete(UUID channelId) {
        deleteFile(resolvePath(channelId));

    }

    @Override
    public boolean existsById(UUID channelId) {
        return Files.exists(resolvePath(channelId));
    }
}