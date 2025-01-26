package mission.repository.jcf;

import mission.entity.Channel;
import mission.entity.Message;
import mission.repository.MessageRepository;
import mission.service.exception.NotFoundId;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {

    // TreeSet 선택 1. 모든 채널의 메시지는 무수히 많을 것 2. 시간 순서 필요
    private final Map<Channel, TreeSet<Message>> data = new HashMap<>();

    @Override
    public Message createOrUpdateMessage(Message message){
        Channel writedChannel = message.getWritedAt();

        data.putIfAbsent(writedChannel, new TreeSet<>());

        Set<Message> messageSet = data.get(writedChannel);
        messageSet.remove(findById(message.getId()));
        messageSet.add(message);
        return message;
    }

    @Override
    public Message findById(UUID id){
        return findAll().stream()
                .filter(message -> message.getId().equals(id))
                .findAny().orElseThrow(() -> new NotFoundId());
    }

    @Override
    public Set<Message> findAll(){
        return data.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public Set<Message> findMessagesInChannel(Channel channel){
        return data.get(channel);
    }

    @Override
    public void delete(UUID messageId) {
        Message deletingMessage = findById(messageId);
        Channel findChannel = deletingMessage.getWritedAt();

        data.get(findChannel).remove(deletingMessage);
        deletingMessage.removeMessage();

        // 아무메시지도 없으면 key값 없애기
        if (data.get(findChannel) == null){
            data.remove(findChannel);
        }
    }
}
