package discodeit.repository.file;

import discodeit.enity.Message;
import discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    @Override
    public void save(Message message) {
        // ser 파일에 Map으로 저장
        Map<UUID, Message> messages = this.findAll();
        if (messages == null) {
            messages = new HashMap<>();
        }
        try (FileOutputStream fos = new FileOutputStream("message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            // 기존 Map을 읽어와 새로운 객체를 추가한 후 덮어씌움
            messages.put(message.getId(), message);
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID messageId) {
        Map<UUID, Message> messages = this.findAll();
        if (messages == null || !messages.containsKey(messageId)) {
            throw new NoSuchElementException("Message ID: " + messageId + " not found");
        }
        // 기존 Map을 읽어와 데이터를 삭제한 후 덮어씌움
        messages.remove(messageId);
        try (FileOutputStream fos = new FileOutputStream("message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    @Override
    public Message findById(UUID messageId) {
        Map<UUID, Message> messages = findAll();
        Optional<Message> finding_message = messages.values().stream()
                .filter(message -> message.getId().equals(messageId))
                .findAny();
        return finding_message
                .orElseThrow(() -> new NoSuchElementException("Message ID: " + messageId + " not found"));
    }

    public Map<UUID, Message> findAll() {
        Map<UUID, Message> messages = new HashMap<>();
        try (FileInputStream fis = new FileInputStream("message.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            messages = (Map<UUID, Message>) ois.readObject();
        } catch (EOFException e) {
            return messages;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return messages;
    }
}
