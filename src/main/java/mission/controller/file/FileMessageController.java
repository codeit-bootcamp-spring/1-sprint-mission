package mission.controller.file;

import mission.controller.MessageController;
import mission.entity.Message;
import mission.entity.User;
import mission.service.file.FileChannelService;
import mission.service.file.FileMessageService;
import mission.service.file.FileUserService;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileMessageController implements MessageController {

    private final FileChannelService fileChannelService = FileChannelService.getInstance();
    private final FileUserService fileUserService = FileUserService.getInstance();
    private final FileMessageService fileMessageService = FileMessageService.getInstance();

    @Override
    public void create(UUID channelId, UUID userId, String message){
        try {
            fileMessageService.createOrUpdate(Message.createMessage(fileChannelService.findById(channelId), fileUserService.findById(userId), message));
        } catch (IOException e) {
            throw new RuntimeException("I/O 오류 : 생성 실패");
        }
    }

    @Override
    public void update(UUID messageId, String newMessage){
        fileMessageService.update(messageId, newMessage);
    }

    @Override
    public Message findById(UUID messageId){
        return fileMessageService.findById(messageId);
    }

    @Override
    public Set<Message> findAll(){
        return fileMessageService.findAll();
    }

    @Override
    public Set<Message> findMessagesByUserId(UUID userId){
        return fileUserService.findById(userId).getMessagesImmutable();
    }

    @Override
    public Set<Message> findMessagesInChannel(UUID channelId) {
        return fileMessageService.findMessagesInChannel(fileChannelService.findById(channelId));
    }

    @Override
    public Set<Message> findContainingMessageInChannel(UUID channelId, String writedMessage) {
        return findMessagesInChannel(channelId).stream()
                .filter(message -> message.getMessage().contains(writedMessage))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public void delete(UUID messageId){
        fileMessageService.delete(messageId);
    }

    public void createDirectory(){
        fileMessageService.createDirectory();
    }

}
