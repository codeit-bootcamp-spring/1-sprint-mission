package mission.service.file;

import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;
import mission.repository.file.FileMessageRepository;
import mission.service.MessageService;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {

    private final FileMessageRepository fileMessageRepository = new FileMessageRepository();

    private static FileMessageService fileMessageService;

    private FileMessageService() {
    }

    public static FileMessageService getInstance() {
        if (fileMessageService == null) return fileMessageService = new FileMessageService();
        else return fileMessageService;
    }


    @Override
    public Message createOrUpdate(Message message) throws IOException {
        return fileMessageRepository.createMessage(message);
    }

    /**
     * 업테이트
     */
    @Override
    public Message update(UUID messageId, String newMassage) {
        Message updatingMessage = findById(messageId);
        updatingMessage.setMessage(newMassage);

        try {
            return fileMessageRepository.createMessage(updatingMessage);
        } catch (IOException e) {
            throw new RuntimeException("I/O 오류 : 수정 실패");
        }
    }

    @Override
    public Set<Message> findAll() {
        return fileMessageRepository.findAll();
    }

    @Override
    public Set<Message> findMessagesInChannel(Channel channel) {
        // return channel.getMessageList();
        // 위처럼 바로 반환시 메시지 생성 후 IO 오류 발생 메시지도 포함되버림
        Set<Message> messageList = findAll();
        return messageList.stream()
                .filter(message -> message.getWritedAt().equals(channel))
                .collect(Collectors.toCollection(HashSet::new));
    }

    public List<Message> findMessagesInChannelByUser(Channel channel, User user) {
        return findMessagesInChannel(channel).stream()
                .filter(message -> message.getWriter().equals(user))
                .collect(Collectors.toList());
    }

    public Set<Message> findMessageInChannelByString(Channel channel, String writedString) {
        return findMessagesInChannel(channel).stream()
                .filter(message -> message.getMessage().equals(writedString))
                .collect(Collectors.toCollection(HashSet::new));
    }

    // 전체 메시지 조회
    public Set<Message> findMessageByString(String writedMessage) {
        return fileMessageRepository.findMessageByString(writedMessage);
    }

    public Message findMessageById(UUID id) {
        return fileMessageRepository.findMessageById(id);
    }

    /**
     * 삭제
     */
    @Override
    public void delete(UUID messageId) {
        try {
            fileMessageRepository.delete(findById(messageId));
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 실패: ", e);
        }
    }
}