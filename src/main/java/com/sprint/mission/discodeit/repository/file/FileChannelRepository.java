package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileChannelRepository {
    private static FileChannelRepository instance;
    private final Path directory;
    private final Path filePath;


    private FileChannelRepository() {
        this.directory = Paths.get(System.getProperty("user.dir"), "data");
        this.filePath = directory.resolve("channels.ser");
        init();
    }

    public static synchronized FileChannelRepository getInstance() {
        if (instance == null) {
            instance = new FileChannelRepository();
        }
        return instance;
    }

    private void init(){
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                System.out.println("디렉토리 생성 완료");
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패: " + e.getMessage(), e);
            }
        }
    }

    public void save(List<Channel> channels) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            throw new RuntimeException("채널 저장 실패: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Channel> load() {
        List<Channel> channels = new ArrayList<>();
        Path filePath = directory.resolve("channels.ser");

        if (Files.exists(filePath)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
                channels = (List<Channel>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("사용자 로드 실패: " + e.getMessage(), e);
            }
        }

        return channels;
    }

}
