package mission.repository.jcf;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.repository.MessageRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {

    private final Map<Channel, List<Message>> messageMap = new HashMap<>();

    public Message createMessage(Channel writeAt, User writer, String writedMessage){
        Message createdMessage = new Message(writedMessage);

        // User가 갖고 있는 자기가 쓴 메시지 목록에 추가
        writer.createMessage(createdMessage, writeAt);
        messageMap.putIfAbsent(createdMessage.getWritedAt(), new ArrayList<>());
        messageMap.get(writeAt).add(createdMessage);
        return createdMessage;
    }

    public List<Message> findAll(){
        return messageMap.values().stream() // 리스트를 스트림으로
                .flatMap(List::stream) // 각 리스트 하나의 스트림 평탄화
                .collect(Collectors.toList());
    }

    public Message findMessageById(UUID id){
        List<Message> all = findAll();
        for (Message message : all) {
            if (message.getId() == id){
                return message;
            }
        }
        return null;
    }

    public List<Message> findMessagesInChannel(Channel channel){
        return messageMap.get(channel);
    }

    public Message updateMessage(UUID id, String newMessage){
        findMessageById(id).setMessage(newMessage);
        return findMessageById(id);
    }

    //throw new NoSuchElementException("메시지 id가 틀렸습니다.");

}
