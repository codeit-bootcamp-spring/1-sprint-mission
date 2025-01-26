package discodeit;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;
import discodeit.service.file.FileChannelService;
import discodeit.service.file.FileMessageService;
import discodeit.service.file.FileUserService;

import java.util.List;

public class JavaApplication {

    static void userCRUDTest(UserService userService) {
        // 생성
        User user = userService.create("user", "user@codeit.com", "010-1111-1111", "qwer1234");
        System.out.println("유저 생성: " + user.getId());
        // 조회
        User foundUser = userService.find(user.getId());
        System.out.println("유저 조회(단건): " + userService.getInfo(foundUser.getId()));
        List<User> foundUsers = userService.findAll();
        System.out.println("유저 조회(다건): " + foundUsers.size());
        // 수정
        userService.update(user.getId(), "user", "user@codeit.com", "010-1111-1111");
        System.out.println("유저 수정: " + userService.getInfo(user.getId()));
        // 삭제
        userService.delete(user.getId());
        List<User> foundUsersAfterDelete = userService.findAll();
        System.out.println("유저 삭제: " + foundUsersAfterDelete.size());
    }

    static void channelCRUDTest(ChannelService channelService, User owner) {
        // 생성
        Channel channel = channelService.create("공지", "공지 채널입니다.", owner);
        System.out.println("채널 생성: " + channel.getId());
        // 조회
        Channel foundChannel = channelService.find(channel.getId());
        System.out.println("채널 조회(단건): " + channelService.getInfo(foundChannel.getId()));
        List<Channel> foundChannels = channelService.findAll();
        System.out.println("채널 조회(다건): " + foundChannels.size());
        // 수정
        channelService.update(channel.getId(), "공지", "공지입니다");
        System.out.println("채널 수정: " + channelService.getInfo(channel.getId()));
        // 삭제
        channelService.delete(channel.getId());
        List<Channel> foundChannelsAfterDelete = channelService.findAll();
        System.out.println("채널 삭제: " + foundChannelsAfterDelete.size());
    }

    static void messageCRUDTest(MessageService messageService, Channel channel, User author) {
        // 생성
        Message message = messageService.create("안녕하세요.", author, channel.getId());
        System.out.println("메시지 생성: " + message.getId());
        // 조회
        Message foundMessage = messageService.find(message.getId());
        System.out.println("메시지 조회(단건): " + messageService.getInfo(foundMessage.getId()));
        List<Message> foundMessages = messageService.findAll();
        System.out.println("메시지 조회(다건): " + foundMessages.size());
        // 수정
        messageService.update(message.getId(), "반갑습니다.");
        System.out.println("메시지 수정: " + messageService.getInfo(message.getId()));
        // 삭재
        messageService.delete(message.getId());
        List<Message> foundMessagesAfterDelete = messageService.findAll();
        System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size());
    }

    static User setupUser(UserService userService) {
        User user = userService.create("user", "user@codeit.com", "010-1111-1111", "qwer1234");
        return user;
    }

    static Channel setupChannel(ChannelService channelService, User owner) {
        Channel channel = channelService.create("공지", "공지 채널입니다.", owner);
        return channel;
    }

    public static void main(String[] args) {



        UserService userService = new FileUserService();
        ChannelService channelService = new FileChannelService();
        MessageService messageService = new FileMessageService(userService, channelService);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService, user);

        // 테스트
        userCRUDTest(userService);
        channelCRUDTest(channelService, user);
        messageCRUDTest(messageService, channel, user);
    }
}
