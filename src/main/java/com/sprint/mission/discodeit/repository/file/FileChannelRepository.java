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
        try (
                FileOutputStream fos = new FileOutputStream(DIRECTORY.toFile(), true);
                ObjectOutputStream oos = new ObjectOutputStream(fos) {
                    @Override
                    protected void writeStreamHeader() throws IOException {
                        if (fos.getChannel().position() == 0) {
                            super.writeStreamHeader();
                        } else {
                            reset();
                        } //역직렬화 헤더 오류 해결 코드. 파일에 한번만 헤더 들어갈 수 있도록
                    }
                }
        ) {
            oos.writeObject(channel);
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
