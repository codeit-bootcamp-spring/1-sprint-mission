package discodeit;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.jcf.JCFChannelService;
import discodeit.jcf.JCFMessageService;
import discodeit.jcf.JCFUserService;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    static void userCRUDTest(UserService userService) {
        // 생성
        User user = userService.create("철수", "abc@gmail.com", "12345");
        System.out.println("유저 생성: " + user.getId());
        // 조회
        User foundUser = userService.find(user.getId());
        System.out.println("유저 조회(단건): " + foundUser.getId());
        List<User> foundUsers = userService.findAll();
        System.out.println("유저 조회(다건): " + foundUsers.size());
        // 수정
        User updatedUser = userService.update(user.getId(), "영희", "efgqwe@naver.com", "123");
        System.out.println("유저 수정: " + updatedUser);
        // 삭제
        userService.delete(updatedUser.getId());
        List<User> users = userService.findAll();
        System.out.println("유저 삭제: " + users.size());
    }
    static void channelCRUDTest(ChannelService channelService) {
        // 생성
        Channel channel = channelService.create("코드잇", "스프린트", ChannelType.PUBLIC);
        System.out.println("채널 생성: " + channel.getId());
        // 조회
        Channel foundChannel = channelService.find(channel.getId());
        System.out.println("채널 조회(단건): " + foundChannel.getId());
        List<Channel> foundChannels = channelService.findAll();
        System.out.println("채널 조회(다건): " + foundChannels.size());
        // 수정
        Channel updatedChannel = channelService.update(channel.getId(), "코드잇2", "스프린트2", ChannelType.PRIVATE);
        System.out.println("채널 수정: " + updatedChannel);
        // 삭제
        channelService.delete(updatedChannel.getId());
        List<Channel> channels = channelService.findAll();
        System.out.println("채널 삭제: " + channels.size());

    }
    static void messageCRUDTest(MessageService messageService) {
        // 생성
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        Message message = messageService.create("안녕하세요.", channelId, authorId);
        System.out.println("메시지 생성: " + message.getId());
        // 조회
        Message foundMessage = messageService.find(message.getId());
        System.out.println("메시지 조회(단건): " + foundMessage.getId());
        List<Message> foundMessages = messageService.findAll();
        System.out.println("메시지 조회(다건): " + foundMessages.size());
        // 수정
        Message updatedMessage = messageService.update(message.getId(), "반갑습니다.");
        System.out.println("메시지 수정: " + updatedMessage.getContent());
        // 삭제
        messageService.delete(message.getId());
        List<Message> messages = messageService.findAll();
        System.out.println("메시지 삭제: " + messages.size());
    }

    static User setupUser(UserService userService) {
        User user = userService.create("woody", "woody@codeit.com", "woody1234");
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create("공지", "공지 채널입니다", ChannelType.PUBLIC);
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요", channel.getId(), author.getId());
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        // 서비스 초기화
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService(channelService, userService);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, user);
    }
}
