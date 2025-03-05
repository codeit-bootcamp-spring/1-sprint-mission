package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileChannelRepository implements ChannelRepository {

    private final Path DIRECTORY;

    public FileChannelRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "Channel.ser");
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createFile(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Channel save(Channel channel) {
        List<Channel> channels = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIRECTORY.toFile()))) {
            while (true) {
                try {
                    Channel existingChannel = (Channel) ois.readObject();
                    channels.add(existingChannel);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (FileNotFoundException e) {
            // 파일이 없는 경우, 새 파일 생성 후 저장
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 채널 업데이트 또는 추가
        boolean channelUpdated = false;
        for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).getId().equals(channel.getId())) {
                channels.set(i, channel);
                channelUpdated = true;
                break;
            }
        }
        if (!channelUpdated) {
            channels.add(channel);
        }

        // 파일에 다시 쓰기
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DIRECTORY.toFile()))) {
            for (Channel c : channels) {
                oos.writeObject(c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return channel;
    }
    @Override
    public Optional<Channel> findById(UUID id) {
        List<Channel> allContents = readAllContents();
        return allContents.stream()
                .filter(content -> content.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Channel> findAllByIdIn(List<UUID> ids) {
        List<Channel> allContents = readAllContents();
        return allContents.stream()
                .filter(content -> ids.contains(content.getId()))
                .toList();
    }
@Override
    public List<Channel> readAllContents() {
        List<Channel> contents = new ArrayList<>();
        if (Files.exists(DIRECTORY)) {
            try (
                    FileInputStream fis = new FileInputStream(DIRECTORY.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                while (true) {
                    try {
                        Channel content = (Channel) ois.readObject();
                        contents.add(content);
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return contents;
    }

    @Override
    public boolean existsById(UUID id) {
        return findById(id).isPresent();
    }

    @Override
    public void deleteById(UUID id) {
        List<Channel> allContents = readAllContents();
        List<Channel> updatedContents = allContents.stream()
                .filter(content -> !content.getId().equals(id))
                .toList();
        saveAllContents(updatedContents);
    }

    private void saveAllContents(List<Channel> contents) {
        try (
                FileOutputStream fos = new FileOutputStream(DIRECTORY.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            for (Channel content : contents) {
                oos.writeObject(content);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}