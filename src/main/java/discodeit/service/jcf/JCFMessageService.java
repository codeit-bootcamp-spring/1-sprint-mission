package discodeit.service.jcf;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;
import discodeit.service.MessageService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private static volatile JCFMessageService instance;
    private final Map<UUID, Message> data;
    private JCFUserService jcfUserService;
    private JCFChannelService jcfChannelService;

    private JCFMessageService() {
        this.data = new HashMap<>();
    }

    public static JCFMessageService getInstance() {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFMessageService();
                }
            }
        }
        return instance;
    }

    public void setService() {
        this.jcfUserService = jcfUserService.getInstance();
        this.jcfChannelService = jcfChannelService.getInstance();
    }

    public Map<UUID, Message> getData() {
        return new HashMap<>(data);
    }

    @Override
    public UUID createMessage(UUID userId, UUID channelId, String content) {
        try {
            if (!jcfChannelService.validateChannel(channelId) || !jcfUserService.validateUser(userId))
                throw new Exception();

            Channel channel = jcfChannelService.getData().get(channelId);
            User user = jcfUserService.getData().get(userId);

            if (!channel.getUsers().contains(user)) {
                System.out.println("메세지를 보낼 수 없습니다: " +
                        user.getName() + "은 아직 '" + channel.getName() + "' 채널에 입장하지 않았습니다.");
                return null;
            }

            Message message = new Message(user, channel, content);
            data.put(message.getId(), message);
            channel.getMessages().add(message);
            System.out.println("메세지 생성 완료");
            return message.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void viewMessage(UUID messageId) {
        if (validateMessage(messageId)) {
            Message message = data.get(messageId);
            System.out.println(message.getChannel().getName() + " > " +
                    message.getSender().getName() + ": " +
                    message.getContent()
                    + " (시간: " + message.getUpdatedAt() + ")");
        } else {
            System.out.println("Error: 존재하지 않는 메시지");
        }
    }

    @Override
    public void viewAllMessages() {
        System.out.println("--- 디스코드잇에서 생성된 모든 메세지 ---");
        data.entrySet().stream()
                .forEach(entry -> {
                    String time = entry.getValue().getCreatedAt().equals(entry.getValue().getUpdatedAt())
                            ? String.valueOf(entry.getValue().getCreatedAt())
                            : entry.getValue().getUpdatedAt() + " 수정";

                    System.out.println("채널 '" + entry.getValue().getChannel().getName() + "' > "
                            + entry.getValue().getSender().getName() + ": "
                            + entry.getValue().getContent()
                            + " (time: " + time + ")");
                });
    }

    @Override
    public void updateMessage(UUID userId, UUID messageId, String newContent) {
        System.out.print("메세지 수정 요청: ");
        if (!validateMessage(messageId) || !jcfUserService.validateUser(userId)) {
            System.out.println("메세지 또는 사용자 데이터가 올바르지 않습니다.");
            return;
        }
        Message message = data.get(messageId);
        User user = jcfUserService.getData().get(userId);

        // 메시지 작성자인 경우에만 수정 가능
        if (message.getSender() != user) {
            System.out.println("메세지 작성자만 수정 가능합니다.");
            return;
        }
        message.updateContent(newContent);
        System.out.println("메세지가 수정되었습니다.");
    }

    @Override
    public void deleteMessage(UUID messageId) {
        System.out.print("메세지 삭제 요청: ");
        if (!validateMessage(messageId)) {
            System.out.println("존재하지 않는 메세지입니다.");
            return;
        }

        // 해당 채널의 메세지 리스트에서 삭제
        Message message = data.get(messageId);
        int idx = message.getChannel().getMessages().indexOf(message);
        message.getChannel().getMessages().remove(idx);
        data.remove(messageId);
        System.out.println("Message ID: " + message.getId() + "가 삭제됩니다.");
    }

    @Override
    public boolean validateMessage(UUID messageId) {
        if (data.containsKey(messageId) && data.get(messageId) != null) return true;
        return false;
    }
}
