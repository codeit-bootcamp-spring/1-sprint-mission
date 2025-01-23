package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileChannelService implements ChannelService {


    private String fileName = "channel.ser";
    private static Path filePath;

    public static void init(Path directory) {
        //파일 디렉토리 설정 -> service를 묶고, 그 클래스에 옮겨도 될 듯
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        filePath = directory;
    }

    public Channel createChannel(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("채널 이름이 잘못되었습니다");
        }

        Channel channel = new Channel(name);
        try (
                FileOutputStream fos = new FileOutputStream(filePath.resolve(fileName).toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(channel);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return channel;
    }

    @Override
    public Channel readChannel(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id가 null입니다");
        }

        Channel channel;
        try{

            Map<UUID, Channel>  map = Files.list(filePath)
                    .map()
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Channel> readAllChannel() {
        return List.of();
    }

    @Override
    public Channel modifyChannel(UUID id, String name) {
        return null;
    }

    @Override
    public void deleteChannel(UUID id) {

    }
}
