package some_path._1sprintmission.Message;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.Message;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.service.jcf.JCFMessageService;

@SpringBootApplication
public class CreateMessage {
    public static void main(String[] args) {
        JCFMessageService messageService = new JCFMessageService(null);

        User user1 = new User("spring", "sff@nasvdf.com", "010-8425-4558");
        User user2 = new User("summer", "sff@nasvdf.com", "010-8425-4558");
        User user3 = new User("fall", "sff@nasvdf.com", "010-8425-4558");
        User user4 = new User("winter", "sff@nasvdf.com", "010-8425-4558");

        Channel channel = new Channel("example Channel");

        user1.joinChannel(channel);
        user2.joinChannel(channel);
        user3.joinChannel(channel);
        System.out.println("channel Members");
        System.out.println("=======================================");
        channel.getMembers().stream()
                .forEach(u -> System.out.println(u));

        Message message = new Message(user1, "hi");
        Message message2 = new Message(user2, "Hi, how are you guys?");
        Message message3 = new Message(user3, "good, thank you");
        messageService.create(message, channel, message.getSender());
        messageService.create(message2, channel, message2.getSender());
        messageService.create(message3, channel, message3.getSender());

        System.out.println("=======================================");
        System.out.println("channel Message box");
        channel.getMessages().stream()
                .forEach(s -> System.out.println(s.getSender().getUsername() + " : " + s.getContent()));

        Message message4 = new Message(user4, "me too"); //not join channel user error
        messageService.create(message4,channel, message4.getSender());



    }
}
