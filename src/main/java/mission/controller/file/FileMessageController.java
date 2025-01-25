package mission.controller.file;

import mission.entity.Message;
import mission.entity.User;
import mission.service.file.FileChannelService;
import mission.service.file.FileMessageService;
import mission.service.file.FileUserService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class FileMessageController {

    private final FileChannelService fileChannelService = FileChannelService.getInstance();
    private final FileUserService fileUserService = FileUserService.getInstance();
    private final FileMessageService fileMessageService = FileMessageService.getInstance();


    /**
     * 생성
     */
    public void create(UUID channelId, UUID userId, String message){
        try {
            fileMessageService.createOrUpdate(Message.createMessage(fileChannelService.findById(channelId), fileUserService.findById(userId), message));
        } catch (IOException e) {
            throw new RuntimeException("I/O 오류 : 생성 실패");
        }
    }

    /**
     * 수정
     */
    public void update(UUID messageId, String newMessage){
        fileMessageService.update(messageId, newMessage);
    }


    /**
     * 조회
     */
    public Message findById(UUID messageId){
        return fileMessageService.findById(messageId);
    }

    public Set<Message> findMessagesByUser(UUID userId){
        User writer = fileUserService.findById(userId);
        return writer.getMessagesImmutable();
    }

    public Set<Message> findAll(){
        return fileMessageService.findAll();
    }

    /**
     * 삭제
     */
    public void delete(UUID messageId){
        fileMessageService.delete(messageId);
    }

    /**
     * 채널 디렉토리 생성
     */

}
