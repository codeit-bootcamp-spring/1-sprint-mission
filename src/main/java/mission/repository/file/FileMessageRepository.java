package mission.repository.file;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageRepository {

    private static final Path MS_DIRECT_PATH = Path.of("MS_Directory");

    public Message createMessage(Message message) throws IOException {
        Path msDirectPath = getMsDirectPath(message.getId());

        if (!Files.exists(msDirectPath)){
            Files.createFile(msDirectPath);
        }

        ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(msDirectPath));
        oos.writeObject(message);
        return message;
    }

    // 너무 많이 활용해서 여기서 바로 오류 catch
    public Set<Message> findAll(){
        try {
            return Files.exists(MS_DIRECT_PATH)
                    ? Files.list(MS_DIRECT_PATH)
                    .filter(path -> path.toString().endsWith(".ser"))
                    .map(this::readMessageFromFile)
                    .collect(Collectors.toCollection(HashSet::new))

                    : new HashSet<>();
        } catch (IOException e) {
            throw new RuntimeException("findAll => 입출력 오류 발생: ",e);
        }
    }

    public Message findMessageById(UUID id) {
        return Files.exists(getMsDirectPath(id))
                ? readMessageFromFile(getMsDirectPath(id))
                : null;
    }

    public Set<Message> findMessageByString(String writedMessage){
        Set<Message> messageList = findAll();
        Map<String, List<Message>> writedMessageMap = messageList.stream()
                .collect(Collectors.groupingBy(message -> message.getMessage()));
        return new HashSet<>(writedMessageMap.get(writedMessage));
    }

//    public Message findMessageInChannelByString(UUID id) {
//        return Files.exists(getMsDirectPath(id))
//                ? readMessageFromFile(getMsDirectPath(id))
//                : null;
//    }

    public Message updateMessage(UUID messageId, String newMessage) {
        return null;
    }

    public void delete(Message message) {
    }

    /**
     * 편의 메서드
     */
    private Path getMsDirectPath(UUID id){
        return MS_DIRECT_PATH.resolve(id + ".ser");
    }

    private Message readMessageFromFile(Path msPath) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(msPath))){
            return (Message) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException("입출력 오류가 발생했습니다." + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
