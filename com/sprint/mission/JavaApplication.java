package sprint.mission;

import sprint.mission.discodeit.entity.Channel;
import sprint.mission.discodeit.entity.Common;
import sprint.mission.discodeit.entity.Message;
import sprint.mission.discodeit.entity.User;
import sprint.mission.discodeit.service.jcf.JCFChannelService;
import sprint.mission.discodeit.service.jcf.JCFMessageService;
import sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class JavaApplication {
    private static void testUser() {
        User frog = User.createUser("frog");
        User baek = User.createUser("baek");

        JCFUserService userService = JCFUserService.getInstance();
        System.out.println("userService.create()");
        System.out.println(userService.create(frog));
        System.out.println(userService.create(baek));
        System.out.println(userService.create(baek));
        System.out.println();

        System.out.println("userService.read()");
        UUID frogKey = frog.getCommon().getId();
        UUID baekKey = baek.getCommon().getId();
        System.out.println(userService.read(frogKey));
        System.out.println(userService.read(baekKey));
        System.out.println(userService.read(UUID.randomUUID()));
        System.out.println();

        System.out.println("userService.update()");
        System.out.println(userService.update(frogKey, User.createUser(Common.createCommon(frogKey), "fffrog")));
        System.out.println(userService.update(UUID.randomUUID(), User.createUser("ppprog")));
        System.out.println();

        System.out.println("userService.delete()");
        System.out.println(userService.delete(frogKey));
        System.out.println(userService.delete(frogKey));
        System.out.println();
    }
    private static void testMessage() {
        Message hi = Message.createMessage("hi");
        Message lo = Message.createMessage("lo");

        JCFMessageService messageService = JCFMessageService.getInstance();
        System.out.println("messageService.create()");
        System.out.println(messageService.create(hi));
        System.out.println(messageService.create(lo));
        System.out.println(messageService.create(lo));
        System.out.println();

        System.out.println("messageService.read()");
        UUID hiKey = hi.getCommon().getId();
        UUID loKey = lo.getCommon().getId();
        System.out.println(messageService.read(hiKey));
        System.out.println(messageService.read(loKey));
        System.out.println(messageService.read(UUID.randomUUID()));
        System.out.println();

        System.out.println("messageService.update()");
        System.out.println(messageService.update(hiKey, Message.createMessage(Common.createCommon(hiKey), "mid")));
        System.out.println(messageService.update(UUID.randomUUID(), Message.createMessage("middle")));
        System.out.println();

        System.out.println("messageService.delete()");
        System.out.println(messageService.delete(hiKey));
        System.out.println(messageService.delete(hiKey));
        System.out.println();
    }
    private static void testChannel() {
        Channel c1 = Channel.createChannel("c1");
        Channel lo = Channel.createChannel("c2");

        JCFChannelService channelService = JCFChannelService.getInstance();
        System.out.println("channelService.create()");
        System.out.println(channelService.create(c1));
        System.out.println(channelService.create(lo));
        System.out.println(channelService.create(lo));
        System.out.println();

        System.out.println("channelService.read()");
        UUID c1Key = c1.getCommon().getId();
        UUID c2Key = lo.getCommon().getId();
        System.out.println(channelService.read(c1Key));
        System.out.println(channelService.read(c2Key));
        System.out.println(channelService.read(UUID.randomUUID()));
        System.out.println();

        System.out.println("channelService.update()");
        System.out.println(channelService.update(c1Key, Channel.createChannel(Common.createCommon(c1Key), "c3")));
        System.out.println(channelService.update(UUID.randomUUID(), Channel.createChannel("c123456")));
        System.out.println();

        System.out.println("channelService.delete()");
        System.out.println(channelService.delete(c1Key));
        System.out.println(channelService.delete(c1Key));
        System.out.println();
    }

    public static void main(String[] args) {
        //testUser();
        //testMessage();
        testChannel();
    }
}
