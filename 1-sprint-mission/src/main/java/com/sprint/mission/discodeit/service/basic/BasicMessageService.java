package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;

import java.io.IOException;

public class BasicMessageService implements MessageService {
    private static final BasicMessageService INSTANCE = new BasicMessageService();
    private final FileUserRepository userRepository = FileUserRepository.getInstance();
    private final FileChannelRepository channelRepository = FileChannelRepository.getInstance();
    private final FileMessageRepository messageRepository = FileMessageRepository.getInstance();

    private BasicMessageService(){}

    public static BasicMessageService getInstance() {
        return INSTANCE;
    }

    @Override
    public void showInfoMessage(Message message) throws IOException, ClassNotFoundException {
        System.out.println(messageRepository.getMessage(message));
    }

    @Override
    public Message saveMessage(String content, User user, Channel channel) throws IOException, ClassNotFoundException {
        return messageRepository.createMessage(content, user.getId(), userRepository.getUser(user).getName(), channel.getChannelId(), channelRepository.getChannel(channel).getName());
    }

    @Override
    public void deleteMessage(Message message) throws IOException, ClassNotFoundException {
        messageRepository.deleteMessage(message.getMessageId());
    }

    @Override
    public void modifyMessage(Message message, String content) throws IOException, ClassNotFoundException {
        messageRepository.modifyMessage(message.getUserId(),message.getMessageId(),content);
    }
}
