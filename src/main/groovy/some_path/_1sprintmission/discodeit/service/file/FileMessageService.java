package some_path._1sprintmission.discodeit.service.file;

import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.Message;
import some_path._1sprintmission.discodeit.repository.file.FileMessageRepository;

import java.util.List;

public class FileMessageService {

    private final FileMessageRepository messageRepository;

    public FileMessageService(String filePath) {
        this.messageRepository = new FileMessageRepository(filePath);
    }

    // Save a message
    public void saveMessage(Channel channel, Message message) {
        messageRepository.saveMessage(channel, message);
    }

    // Find messages by channel
    public List<Message> findMessagesByChannel(Channel channel) {
        return messageRepository.findMessagesByChannel(channel);
    }

    // Update a message
    public void updateMessage(Channel channel, Message oldMessage, String newContent) {
        messageRepository.updateMessage(channel, oldMessage, newContent);
    }

    // Delete a message
    public void deleteMessage(Channel channel, Message messageToDelete) {
        messageRepository.deleteMessage(channel, messageToDelete);
    }
}
