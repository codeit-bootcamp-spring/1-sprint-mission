package mission.repository.jcf;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.repository.MessageRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {

    private final Map<Channel, List<Message>> data = new HashMap<>();

    public Message createMessage(Message message){
        Channel writedAt = message.getWritedAt();
        data.putIfAbsent(writedAt, new ArrayList<>());
        data.get(writedAt).add(message);
        return message;
    }

    public Set<Message> findAll(){
        return data.values().stream() // 리스트를 스트림으로
                .flatMap(List::stream) // 각 리스트 하나의 스트림 평탄화
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Message findMessageById(UUID id){
        for (Message message : findAll()) {
            if (message.getId() == id){
                return message;
            }
        }
        return null;
    }

    public Set<Message> findMessagesInChannel(Channel channel){
        return data.get(channel);
    }

    public Message updateMessage(UUID id, String newMessage){
        findMessageById(id).setMessage(newMessage);
        return findMessageById(id);
    }

    public void delete(Message deletingMessage) {
        data.get(deletingMessage.getWritedAt()).remove(deletingMessage);
        // 개인이 보관한다면 아래의 코드는 주석처리
        deletingMessage.getWriter().getMessages().remove(deletingMessage);
    }

    //throw new NoSuchElementException("메시지 id가 틀렸습니다.");

}
