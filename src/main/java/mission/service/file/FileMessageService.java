package mission.service.file;

import mission.entity.Channel;
import mission.entity.Message;
import mission.repository.file.FileMessageRepository;
import mission.service.MessageService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {

    private final FileMessageRepository fileMessageRepository = new FileMessageRepository();

    private static FileMessageService fileMessageService;

    private FileMessageService() {}
    public static FileMessageService getInstance() {
        if (fileMessageService == null) return fileMessageService = new FileMessageService();
        else return fileMessageService;
    }

    @Override
    public Message createOrUpdate(Message message) throws IOException {
        return fileMessageRepository.createOrUpdateMessage(message);
    }

    @Override
    public Message update(UUID messageId, String newMassage) {
        Message updatingMessage = findById(messageId);
        updatingMessage.setMessage(newMassage);

        try {
            return fileMessageRepository.createOrUpdateMessage(updatingMessage);
        } catch (IOException e) {
            throw new RuntimeException("I/O 오류 : 수정 실패");
        }
    }

    @Override
    public Message findById(UUID messageId) {
        return fileMessageRepository.findById(messageId);
    }

    @Override
    public Set<Message> findAll() {
        return fileMessageRepository.findAll();
    }

    @Override
    public Set<Message> findMessagesInChannel(Channel channel) {
        return fileMessageRepository.findMessagesInChannel(channel);
    }

    @Override
    public void delete(UUID messageId) {
        fileMessageRepository.delete(messageId);
    }

    /**
     * 메시지 디렉토리 생성
     */
    public void createDirectory() {
        try {
            fileMessageRepository.createDirectory();
        } catch (IOException e) {
            throw new RuntimeException("디렉토리 초기화 실패" + e.getMessage());
        }
    }
}