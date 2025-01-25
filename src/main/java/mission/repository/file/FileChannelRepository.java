package mission.repository.file;


import mission.entity.Channel;
import mission.repository.ChannelRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileChannelRepository implements ChannelRepository {

    private static final Path CHANNEL_DIRECT_PATH = Path.of("userDirectory");

    @Override
    // 생성과 수정 2개 기능을 함
    public Channel register(Channel channel) throws IOException {
        Path channelPath = getChannelDirectPath(channel.getId());

        if (!Files.exists(channelPath)) {
            Files.createFile(channelPath);
        }

        try (ObjectOutputStream ois = new ObjectOutputStream(Files.newOutputStream(channelPath))) {
            ois.writeObject(channel);
        }
        return channel;
    }

    @Override
    public Set<Channel> findAll() throws IOException {
        return Files.exists(CHANNEL_DIRECT_PATH)
                ? Files.list(CHANNEL_DIRECT_PATH)
                .filter(path -> path.toString().endsWith(".ser"))
                .map(this::readChannelFromFile)
                .collect(Collectors.toCollection(HashSet::new))

                : new HashSet<>();
    }

    @Override
    public Channel findById(UUID id) throws IOException, ClassNotFoundException {
        Path channelPath = getChannelDirectPath(id);
        if (!Files.exists(channelPath)) {
            System.out.println("주어진 id의 채널파일이 존재하지 않습니다.");
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(channelPath))){
            return (Channel) ois.readObject();
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            Files.delete(getChannelDirectPath(id));
        } catch (IOException e) {
            throw new RuntimeException("삭제 실패 " + e.getMessage());
        }
    }

    /**
     * 편의 메서드
     */
    private Path getChannelDirectPath(UUID id) {
        return CHANNEL_DIRECT_PATH.resolve(id.toString() + ".ser");
    }

    public Channel readChannelFromFile(Path channelPath) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(channelPath))) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 파일 역직렬화 실패: " + e.getMessage());
        }
    }

    /**
     * 디렉토리 생성 - 기존 디렉토리의 파일들 삭제 (테스트용)
     */
    public void createChannelDirectory() throws IOException {

        if (Files.exists(CHANNEL_DIRECT_PATH)) {
            try {
                Files.list(CHANNEL_DIRECT_PATH).forEach(
                        file -> {
                            try {
                                Files.delete(file);
                            } catch (IOException e) {
                                throw new RuntimeException("삭제할 수 없는 Channel 파일이 있습니다" + e.getMessage());}});
            } catch (IOException e) {
                System.out.println("디렉토리 초기화 실패");
            } // Files.delete(CHANNEL_DIRECT_PATH); 굳이 디렉토리까지 삭제할 필요가 없다
        } else {
            Files.createDirectory(CHANNEL_DIRECT_PATH);
        }
    }
}
