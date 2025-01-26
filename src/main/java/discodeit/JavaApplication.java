package discodeit;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.repository.ChannelRepository;
import discodeit.repository.MessageRepository;
import discodeit.repository.UserRepository;
import discodeit.repository.file.FileChannelRepository;
import discodeit.repository.file.FileMessageRepository;
import discodeit.repository.file.FileUserRepository;
import discodeit.repository.jcf.JCFChannelRepository;
import discodeit.repository.jcf.JCFMessageRepository;
import discodeit.repository.jcf.JCFUserRepository;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;
import discodeit.service.basic.BasicChannelService;
import discodeit.service.basic.BasicMessageService;
import discodeit.service.basic.BasicUserService;
import discodeit.validator.ChannelValidator;
import discodeit.validator.MessageValidator;
import discodeit.validator.UserValidator;

import java.util.List;

public class JavaApplication {

    static void userCRUDTest(UserService userService) {
        // 생성
        User user = userService.create("user", "user@codeit.com", "010-1111-1111", "qwer1234");
        System.out.println("유저 생성");
        System.out.println(userService.getInfo(user.getId()));
        // 조회
        User foundUser = userService.find(user.getId());
        System.out.println("유저 조회(단건)");
        System.out.println(userService.getInfo(foundUser.getId()));
        List<User> foundUsers = userService.findAll();
        System.out.println("유저 조회(다건): " + foundUsers.size() + System.lineSeparator());
        // 수정
        userService.update(user.getId(), "uuuuser", "user@codeit.com", "010-1111-1111");
        System.out.println("유저 수정");
        System.out.println(userService.getInfo(user.getId()));
        // 삭제
        userService.delete(user.getId());
        List<User> foundUsersAfterDelete = userService.findAll();
        System.out.println("유저 삭제: " + foundUsersAfterDelete.size() + System.lineSeparator());
    }

    static void channelCRUDTest(ChannelService channelService, User owner) {
        // 생성
        Channel channel = channelService.create("공지", "공지 채널입니다.", owner);
        System.out.println("채널 생성");
        System.out.println(channelService.getInfo(channel.getId()));
        // 조회
        Channel foundChannel = channelService.find(channel.getId());
        System.out.println("채널 조회(단건)");
        System.out.println(channelService.getInfo(foundChannel.getId()));
        List<Channel> foundChannels = channelService.findAll();
        System.out.println("채널 조회(다건): " + foundChannels.size() + System.lineSeparator());
        // 수정
        channelService.update(channel.getId(), "공지", "공지입니다");
        System.out.println("채널 수정");
        System.out.println(channelService.getInfo(channel.getId()));
        // 삭제
        channelService.delete(channel.getId());
        List<Channel> foundChannelsAfterDelete = channelService.findAll();
        System.out.println("채널 삭제: " + foundChannelsAfterDelete.size() + System.lineSeparator());
    }

    static void messageCRUDTest(MessageService messageService, Channel channel, User author) {
        // 생성
        Message message = messageService.create("안녕하세요.", author, channel.getId());
        Message message1 = messageService.create("안녕하세요.", author, channel.getId());
        System.out.println("메시지 생성");
        System.out.println(messageService.getInfo(message.getId()) + System.lineSeparator());
        // 조회
        Message foundMessage = messageService.find(message.getId());
        System.out.println("메시지 조회(단건)");
        System.out.println(messageService.getInfo(foundMessage.getId()) + System.lineSeparator());
        List<Message> foundMessages = messageService.findAll();
        System.out.println("메시지 조회(다건): " + foundMessages.size() + System.lineSeparator());
        // 수정
        messageService.update(message.getId(), "반갑습니다.");
        System.out.println("메시지 수정");
        System.out.println(messageService.getInfo(message.getId()) + System.lineSeparator());
        // 삭제
        messageService.delete(message.getId());
        List<Message> foundMessagesAfterDelete = messageService.findAll();
        System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size() + System.lineSeparator());
    }

    static User setupUser(UserService userService) {
        User user = userService.create("user", "user1@codeit.com", "010-1111-1112", "qwer1234");
        return user;
    }

    static Channel setupChannel(ChannelService channelService, User owner) {
        Channel channel = channelService.create("공지", "공지 채널입니다.", owner);
        return channel;
    }

    static UserService userServiceInit() {
        UserRepository userRepository = new JCFUserRepository();
//        UserRepository userRepository = new FileUserRepository();
        UserValidator validator = new UserValidator();
        return new BasicUserService(userRepository, validator);
    }

    static ChannelService channelServiceInit() {
        ChannelRepository channelRepository = new JCFChannelRepository();
//        ChannelRepository channelRepository = new FileChannelRepository();
        ChannelValidator validator = new ChannelValidator();
        return new BasicChannelService(channelRepository, validator);
    }

    static MessageService messageServiceInti(UserService userService, ChannelService channelService) {
        MessageRepository messageRepository = new JCFMessageRepository();
//        MessageRepository messageRepository = new FileMessageRepository();
        MessageValidator validator = new MessageValidator();
        return new BasicMessageService(messageRepository, validator, userService, channelService);
    }

    public static void main(String[] args) {
        // 서비스 초기화
        UserService userService = userServiceInit();
        ChannelService channelService = channelServiceInit();
        MessageService messageService = messageServiceInti(userService, channelService);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService, user);

        // 테스트
        userCRUDTest(userService);
        channelCRUDTest(channelService, user);
        messageCRUDTest(messageService, channel, user);

        // 파일 지우기
//        userService.delete(user.getId());
//        channelService.delete(channel.getId());
    }
}
