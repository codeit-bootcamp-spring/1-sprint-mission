package mission.service.file;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.repository.file.FileMessageRepository;
import mission.service.MessageService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {


    private final FileMessageRepository fileMessageRepository = new FileMessageRepository();

    @Override
    public Message create(Message message) {
        try {
            return fileMessageRepository.createMessage(message);
        } catch (IOException e) {
            throw new RuntimeException("Message파일 생성을 실패했습니다" + e.getMessage());
        }
    }

    @Override
    public List<Message> findAll() {
        return fileMessageRepository.findAll();
    }

    @Override
    public List<Message> findMessagesInChannel(Channel channel) {
        // return channel.getMessageList();
        // 위처럼 바로 반환시 메시지 생성 후 IO 오류 발생 메시지도 포함되버림
        List<Message> messageList = findAll();
        List<Message> messageListInChannel = messageList.stream()
                .filter(message -> message.getWritedAt().equals(channel))
                .collect(Collectors.toList());

        return messageListInChannel;
    }

    public List<Message> findMessagesInChannelByUser(Channel channel, User user){
        return findMessagesInChannel(channel).stream()
                .filter(message -> message.getWriter().equals(user))
                .collect(Collectors.toList());
    }

    public List<Message> findMessageInChannelByString(Channel channel, String writedString) {
        List<Message> messagesInChannel = findMessagesInChannel(channel);
        return messagesInChannel.stream()
                .filter(message -> message.getMessage().equals(writedString))
                .collect(Collectors.toList());
    }

    // 전체 메시지 조회
    public List<Message> findMessageByString(String writedMessage){
        return fileMessageRepository.findMessageByString(writedMessage);
    }

    public Message findMessageById(UUID id){
        return fileMessageRepository.findMessageById(id);
    }

    @Override
    public Message update(UUID messageId, String newMessage) {
        return fileMessageRepository.updateMessage(messageId, newMessage);
    }

    @Override
    public void delete(Message message) {
        fileMessageRepository.delete(message);
    }
}
//    public Message findMessage(User writer, String writedMessage){
//        for (Message message : writer.getMessages()) {
//            if (message.getMessage().equals(writedMessage)){
//                return message;
//            }
//        }
//        return null
//
//    public Message findMessageById(Channel channel, UUID messageId){
//        Map<UUID, Message> messageMap = findMessagesInChannel(channel).stream()
//                .collect(Collectors.toMap(
//                        Message::getId,  // Message객체 id를 키
//                        Function.identity()  // 파라미터 -> 반환값
//                ));
//        try {
//            return messageMap.get(messageId);
//        } catch (Exception e){
//            throw new NullPointerException("MessageId를 잘못 입력했습니다");
//        }
//    }

