package mission.repository.file;

import mission.entity.Channel;
import mission.entity.Message;
import mission.repository.MessageRepository;
import mission.service.exception.NotFoundId;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageRepository implements MessageRepository {

    private static final Path MS_DIRECT_PATH = Path.of("MS_Directory");

    // 수정, 생성이 쓰는 메서드
    // 수정, 생성 오류 메시지 따로 설정하기 위해 throws
    @Override
    public Message createOrUpdateMessage(Message message) throws IOException {
        Path msDirectPath = getMsDirectPath(message.getId());

        if (!Files.exists(msDirectPath)) {
            Files.createFile(msDirectPath);
        }

        ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(msDirectPath));
        oos.writeObject(message);
        return message;
    }


    @Override
    public Message findById(UUID id) {
        try {
            return readMessageFromFile(getMsDirectPath(id));
        } catch (Exception e) {
            throw new NotFoundId();
        }
    }

    // 너무 많이 활용해서 여기서 바로 오류 catch
    @Override
    public Set<Message> findAll() {
        try {
            return Files.exists(MS_DIRECT_PATH)
                    ? Files.list(MS_DIRECT_PATH)
                    .filter(path -> path.toString().endsWith(".ser"))
                    .map(this::readMessageFromFile)
                    .collect(Collectors.toCollection(HashSet::new))

                    : new HashSet<>();
        } catch (IOException e) {
            throw new RuntimeException("findAll => I/O 오류 발생: ", e);
        }
    }

    @Override
    public Set<Message> findMessagesInChannel(Channel channel){
        return findAll().stream()
                .filter(message -> message.getWritedAt().equals(channel))
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public void delete(UUID messageId){
        try {
            Files.delete(getMsDirectPath(messageId));
        } catch (IOException e) {
            System.out.println("파일 삭제 실패");
        }
    }

    /**
     * 편의 메서드
     */
    private Path getMsDirectPath(UUID id) {
        return MS_DIRECT_PATH.resolve(id + ".ser");
    }

    private Message readMessageFromFile(Path msPath) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(msPath))) {
            return (Message) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException("입출력 오류가 발생했습니다." + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 메시지 디렉토리 생성
     */
    public void createDirectory() throws IOException {
        if (Files.exists(MS_DIRECT_PATH)) {
            Files.list(MS_DIRECT_PATH).forEach(
                    file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            throw new RuntimeException("삭제할 수 없는 Message파일이 있습니다.");
                        }
                    });
        } else {
            Files.createDirectory(MS_DIRECT_PATH);
        }
    }

}
