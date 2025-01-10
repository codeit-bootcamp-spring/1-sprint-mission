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

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();

        // Create
        System.out.println("<등록>");
        User user1 = new User("철수", "chulsoo@gmail.com");
        User user2 = new User("영희", "yeonghee@naver.com");
        userService.create(user1);
        userService.create(user2);
        Channel channel1 = new Channel("코드잇");
        Channel channel2 = new Channel("스프린트");
        channelService.create(channel1);
        channelService.create(channel2);
        Message message1 = new Message("안녕하세요.", user1, channel1);
        Message message2 = new Message("Test Message", user1, channel2);
        messageService.create(message1);
        messageService.create(message2);

        System.out.println();

        // Read - 단일
        System.out.println("<단일 조회>");
        userService.read(user1.getId());
        channelService.read(channel1.getId());
        messageService.read(message1.getId());

        System.out.println();

        // Read - 전체
        System.out.println("<전체 조회>");
        System.out.println(userService.readAll());
        System.out.println(channelService.readAll());
        System.out.println(messageService.readAll());

        System.out.println();

        // Update
        System.out.println("<수정>");
        userService.update(user1.getId(), "철수2");
        userService.read(user1.getId());
        channelService.update(channel2.getId(), "부트캠프");
        channelService.read(channel2.getId());
        messageService.update(message1.getId(), "변경된 메시지입니다.");
        messageService.read(message1.getId());

        System.out.println();

        // Delete
        System.out.println("<삭제>");
        userService.delete(user2.getId());
        userService.read(user2.getId());
        channelService.delete(channel2.getId());
        channelService.read(channel2.getId());
        messageService.delete(message2.getId());
        messageService.read(message2.getId());
    }
}
