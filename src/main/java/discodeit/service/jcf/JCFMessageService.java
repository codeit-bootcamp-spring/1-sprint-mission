package discodeit.service.jcf;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;
import discodeit.service.MessageService;
import discodeit.service.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class JCFMessageService implements MessageService {

    private static volatile JCFMessageService instance;

    private JCFUserService jcfUserService;
    private JCFChannelService jcfChannelService;

    private final List<Message> data;

    private JCFMessageService() {
        this.data=new ArrayList<>();
    }

    public static JCFMessageService getInstance() {
        if(instance == null) {
            synchronized (JCFUserService.class) {
                if(instance == null) {
                    instance = new JCFMessageService();
                }
            }
        }
        return instance;
    }

    public void setJcfUserService(JCFUserService jcfUserService) {
        this.jcfUserService = jcfUserService;
    }

    public void setJcfChannelService(JCFChannelService jcfChannelService) {
        this.jcfChannelService = jcfChannelService;
    }

    @Override
    public Message createMessage(User user, Channel channel, String content) {
        if(!channel.getUsers().contains(user)){
            System.out.println("메세지를 보낼 수 없습니다: " +
                    user.getName() + "은 아직 '" + channel.getName() + "' 채널에 입장하지 않았습니다.");
            return null;
        }
        Message message=new Message(user, channel, content);
        data.add(message);
        channel.getMessages().add(message);
        // [심화] 도메인 간 관계 확인
        System.out.println("[심화] User: " + message.getSender().getName()
                + " Channel: " + message.getChannel().getName()
                + " Message: " + message.getContent());
        return message;
    }

    @Override
    public void viewMessage(Message message) {
        if(data.contains(message)){
            System.out.println( message.getChannel().getName() + " > " +
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
        data.stream()
                .forEach( message -> {
                    System.out.println("채널 '" + message.getChannel().getName() + "' > "
                                        + message.getSender().getName() + ": "
                                        + message.getContent()
                                        + " (time: " + message.getUpdatedAt() + ")");
                });
    }

    @Override
    public void updateMessage(User user, Message message, String newContent) {
        System.out.print("메세지 수정 요청: ");
        try {
            // 메시지 작성자인 경우에만 수정 가능
            if(message.getSender()!=user) {
                throw new IllegalArgumentException("올바르지 않은 작성자");
            }
            message.updateContent(newContent);
            System.out.println("메세지가 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("해당 메세지를 작성한 사용자만 메세지를 수정할 수 있습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMessage(Message message) {
        System.out.print("메세지 삭제 요청: ");
        try{
            // 해당 채널의 메세지 리스트에서 삭제
            int idx = message.getChannel().getMessages().indexOf(message);
            message.getChannel().getMessages().remove(idx);
            data.remove(message);
            System.out.println("Message ID: " + message.getId() + "가 삭제됩니다.");
        } catch (Exception e) {
            System.out.println("Message ID: " + message.getId() + "가 삭제 중 오류 발생");
        }

    }
}
