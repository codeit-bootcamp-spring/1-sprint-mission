package sprint.mission;

import sprint.mission.discodeit.entity.Channel;
import sprint.mission.discodeit.entity.BaseEntity;
import sprint.mission.discodeit.entity.Message;
import sprint.mission.discodeit.entity.User;
import sprint.mission.discodeit.service.jcf.JCFChannelService;
import sprint.mission.discodeit.service.jcf.JCFMessageService;
import sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class JavaApplication {
    private static void testUser() {
        User frog = new User.Builder("frog", "frog@email.com")
                .build();
        User baek = new User.Builder("baek", "baek@email.com")
                .phoneNumber("010-1234-5678")
                .build();

        JCFUserService userService = JCFUserService.getInstance();
        System.out.println("---------------------------------");
        System.out.println("userService.create()");
        System.out.println("pass User 'frog'! " + System.lineSeparator() + "User info: " + userService.create(frog));
        System.out.println();
        System.out.println("pass User 'baek'! " + System.lineSeparator() + "User info: " + userService.create(baek));
        System.out.println();
        System.out.println("pass User 'frog(already exist)'! " + System.lineSeparator() + "User info: " + userService.create(baek));
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("userService.read()");
        UUID frogKey = frog.getBaseEntity().getId();
        UUID baekKey = baek.getBaseEntity().getId();
        System.out.println("pass UUID 'frogKey'! " + System.lineSeparator() + "User info: " + userService.read(frogKey));
        System.out.println();
        System.out.println("pass UUID 'baekKey'! " + System.lineSeparator() + "User info: " + userService.read(baekKey));
        System.out.println();
        System.out.println("pass UUID 'unknownKey'! " + System.lineSeparator() + "User info: " + userService.read(UUID.randomUUID()));
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("userService.update()");
        System.out.println("pass UUID 'frokKey' and User 'fffrog': " + System.lineSeparator() + "User info: " + userService.update(
                frogKey, new User.Builder("fffrog", "fffrog@email.com")
                        .baseEntity(BaseEntity.createBasicEntity(frogKey))
                        .build()));
        System.out.println(userService.update(
                UUID.randomUUID(), new User.Builder("ppprog", "")
                        .build())); // validation not passed
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("userService.delete()");
        System.out.println("pass UUID 'frogKey'! " + System.lineSeparator() + "User info: " +  userService.delete(frogKey));
        System.out.println();
        System.out.println("pass UUID 'frogKey(already exist)'! " + System.lineSeparator() + "User info: " + userService.delete(frogKey));
        System.out.println();
        System.out.println();
    }
    private static void testMessage() {
        Message hi = Message.createMessage("hi");
        Message lo = Message.createMessage("lo");

        JCFMessageService messageService = JCFMessageService.getInstance();
        System.out.println("---------------------------------");
        System.out.println("messageService.create()");
        System.out.println("pass Message 'hi'! " + System.lineSeparator() + "Message info: " + messageService.create(hi));
        System.out.println();
        System.out.println("pass Message 'lo'! " + System.lineSeparator() + "Message info: " + messageService.create(lo));
        System.out.println();
        System.out.println("pass Message 'lo(already exist)'! " + System.lineSeparator() + "Message info: " + messageService.create(lo));
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("messageService.read()");
        UUID hiKey = hi.getBaseEntity().getId();
        UUID loKey = lo.getBaseEntity().getId();
        System.out.println("pass UUID 'hiKey'! " + System.lineSeparator() + "Message info: " + messageService.read(hiKey));
        System.out.println();
        System.out.println("pass UUID 'loKey'! " + System.lineSeparator() + "Message info: " + messageService.read(loKey));
        System.out.println();
        System.out.println("pass UUID 'unknownKey'! " + System.lineSeparator() + "Message info: " + messageService.read(UUID.randomUUID()));
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("messageService.update()");
        System.out.println("pass UUID 'hiKey' and Message 'mid': " + System.lineSeparator() + "Message info: " +
                messageService.update(hiKey, Message.createMessage(BaseEntity.createBasicEntity(hiKey), "mid")));
//        System.out.println(messageService.update(
//                UUID.randomUUID(), Message.createMessage("mmmmmmmmmmmmmmmmmmmmm"))); // validation not passed
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("messageService.delete()");
        System.out.println("pass UUID 'hiKey'! " + System.lineSeparator() + "Message info: " +  messageService.delete(hiKey));
        System.out.println();
        System.out.println("pass UUID 'hiKey(already exist)'! " + System.lineSeparator() + "Message info: " + messageService.delete(hiKey));
        System.out.println();
        System.out.println();
    }
    private static void testChannel() {
        Channel c1 = Channel.createChannel("c1");
        Channel c2 = Channel.createChannel("c2");

        JCFChannelService channelService = JCFChannelService.getInstance();
        System.out.println("---------------------------------");
        System.out.println("channelService.create()");
        System.out.println("pass Channel 'c1'! " + System.lineSeparator() + "Channel info: " + channelService.create(c1));
        System.out.println();
        System.out.println("pass Channel 'c2'! " + System.lineSeparator() + "Channel info: " + channelService.create(c2));
        System.out.println();
        System.out.println("pass Channel 'c2(already exist)'! " + System.lineSeparator() + "Channel info: " + channelService.create(c2));
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("channelService.read()");
        UUID c1Key = c1.getBaseEntity().getId();
        UUID c2Key = c2.getBaseEntity().getId();
        System.out.println("pass UUID 'c1Key'! " + System.lineSeparator() + "Channel info: " + channelService.read(c1Key));
        System.out.println();
        System.out.println("pass UUID 'c2Key'! " + System.lineSeparator() + "Channel info: " + channelService.read(c2Key));
        System.out.println();
        System.out.println("pass UUID 'unknownKey'! " + System.lineSeparator() + "Channel info: " + channelService.read(UUID.randomUUID()));
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("channelService.update()");
        System.out.println("pass UUID 'c1Key' and Channel 'c3': " + System.lineSeparator() + "Channel info: " +
                channelService.update(c1Key, Channel.createChannel(BaseEntity.createBasicEntity(c1Key), "c3")));
//        System.out.println(channelService.update(
//                UUID.randomUUID(), Channel.createChannel("c12345678910"))); // validation not passed
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("channelService.delete()");
        System.out.println("pass UUID 'c1Key'! " + System.lineSeparator() + "Channel info: " +  channelService.delete(c1Key));
        System.out.println();
        System.out.println("pass UUID 'c1Key(already exist)'! " + System.lineSeparator() + "Channel info: " + channelService.delete(c1Key));
        System.out.println();
        System.out.println();
    }

    public static void main(String[] args) {
        testUser();
        testMessage();
        testChannel();
    }
}
