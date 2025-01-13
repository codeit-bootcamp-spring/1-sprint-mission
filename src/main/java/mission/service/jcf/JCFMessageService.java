package mission.service.jcf;

import mission.entity.Message;
import mission.repository.jcf.JCFUserRepository;
import mission.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();
    //private final Set<String> stringList = new HashSet<>();
    private final JCFUserRepository userRepository = new JCFUserRepository();

    @Override
    public Message create(String message) {
        Message createdMessage = new Message(message);
        data.put(createdMessage.getId(), createdMessage);
        return createdMessage;
    }

    @Override
    public Message findMessage(String message) {
        // uuid는 어차피 unique타입
        for (Message value : data.values()) {
            if (value.getMessage().equals(message)) {
                return value;
            }
        }
        throw new IllegalStateException("그런 메시지 보내적이 없어요");
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID id, String newMessage) {
        if (!data.containsKey(id)) {
            throw new NoSuchElementException("메시지 id가 틀렸습니다.");
        }

        Message updatingMessage = data.get(id);
        updatingMessage.setMessage(newMessage);
        return updatingMessage;
    }

    @Override
    public void delete(UUID id, String message) {
        if (!data.containsKey(id)) {
            throw new NoSuchElementException("메시지 id가 틀렸습니다.");
        }

        // 메시지 내용이 같은 Message객체 찾아서 없애기
        if (data.get(id).getMessage().equals(message)) {
            data.remove(id);
        } else {
            throw new NoSuchElementException("메시지가 틀렸습니다");
        }
    }
}
