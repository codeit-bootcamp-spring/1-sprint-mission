package some_path._1sprintmission.discodeit.service.jcf;

import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.Message;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();
    private final Map<UUID, Channel> channelData; // Channel 데이터 관리

    public JCFMessageService(Map<UUID, Channel> channelData) {
        this.channelData = channelData;
    }

    @Override
    public void create(Message message, Channel channel, User sender) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel does not exist.");
        }

        // Sender가 해당 Channel에 속해 있는지 확인
        if (!channel.getMembers().contains(sender)) {
            throw new IllegalArgumentException("Sender is not a member of the channel.");
        }

        // 메시지 저장
        data.put(message.getId(), message);
        channel.addMessage(message); // Channel에 메시지 추가
    }

    @Override
    public Optional<Message> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<Message> readAllByChannel() {
        return null;
    }

    @Override
    public List<Message> readAllByChannel(UUID channelId, User user) {
        Channel channel = channelData.get(channelId);
        if (channel == null) {
            throw new IllegalArgumentException("Channel does not exist.");
        }

        // User가 해당 Channel의 멤버인지 확인
        if (!channel.getMembers().contains(user)) {
            throw new IllegalArgumentException("User is not a member of the channel.");
        }

        return channel.getMessages();
    }

    @Override
    public void update(UUID id, Message updatedMessage) {
        Message existingMessage = data.get(id);
        if (existingMessage == null) {
            throw new IllegalArgumentException("Message does not exist.");
        }

        // 작성자 권한 확인
        if (!existingMessage.getSender().equals(updatedMessage.getSender())) {
            throw new IllegalStateException("Only the original sender can update this message.");
        }

        data.put(id, updatedMessage);

        // Channel에서 메시지 업데이트
        Channel channel = channelData.get(existingMessage.getId());
        if (channel != null) {
            channel.getMessages().remove(existingMessage);
            channel.getMessages().add(updatedMessage);
        }
    }

    @Override
    public void delete(UUID id) {
        Message existingMessage = data.get(id);
        if (existingMessage == null) {
            throw new IllegalArgumentException("Message does not exist.");
        }

        // 작성자 권한 확인
        if (!existingMessage.getSender().equals(existingMessage.getSender())) {
            throw new IllegalStateException("Only the original sender can delete this message.");
        }

        // 메시지 삭제
        data.remove(id);

        // Channel에서 메시지 삭제
        Channel channel = channelData.get(existingMessage.getId());
        if (channel != null) {
            channel.getMessages().remove(existingMessage);
        }
    }
}