package mission.repository.jcf;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.repository.MessageRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {

    private final Map<Channel, List<Message>> data = new HashMap<>();

    public Message createMessage(Channel writeAt, User writer, String writedMessage){
        Message createdMessage = new Message(writedMessage);

        // User가 갖고 있는 자기가 쓴 메시지 목록에 추가
        writer.createMessage(createdMessage, writeAt);
        data.putIfAbsent(createdMessage.getWritedAt(), new ArrayList<>());
        data.get(writeAt).add(createdMessage);
        return createdMessage;
    }

    public List<Message> findAll(){
        return data.values().stream() // 리스트를 스트림으로
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
        return data.get(channel);
    }

    public Message updateMessage(UUID id, String newMessage){
        findMessageById(id).setMessage(newMessage);
        return findMessageById(id);
    }

    public void delete(Channel writedAt, Message deletingMessage, User writer) {
        data.get(writedAt).remove(deletingMessage);
        writer.getMessages().remove(deletingMessage);
    }

    //throw new NoSuchElementException("메시지 id가 틀렸습니다.");

}
