package com.sprint.mission;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.proxy.*;

import java.util.UUID;

public class JavaApplication {
    private static void testUser() {
        User frog = new User.Builder("frog", "frog@email.com")
                .build();
        User baek = new User.Builder("baek", "baek@email.com")
                .phoneNumber("010-1234-5678")
                .build();

        UserService userService = UserServiceProxy.getInstance();
        System.out.println("---------------------------------");
        System.out.println("userService.createUser()");
        System.out.println("pass User 'frog'! " + System.lineSeparator() + "User info: " + userService.createUser(frog));
        System.out.println();
        System.out.println("pass User 'baek'! " + System.lineSeparator() + "User info: " + userService.createUser(baek));
        System.out.println();
        System.out.println("pass User 'frog(already exist)'! " + System.lineSeparator() + "User info: " + userService.createUser(baek));
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("userService.findUserById()");
        UUID frogKey = frog.getId();
        UUID baekKey = baek.getId();
        System.out.println("pass UUID 'frogKey'! " + System.lineSeparator() + "User info: " + userService.findUserById(frogKey));
        System.out.println();
        System.out.println("pass UUID 'baekKey'! " + System.lineSeparator() + "User info: " + userService.findUserById(baekKey));
        System.out.println();
        System.out.println("pass UUID 'unknownKey'! " + System.lineSeparator() + "User info: " + userService.findUserById(UUID.randomUUID()));
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("userService.updateUserById()");
        System.out.println("pass UUID 'frogKey' and User 'fffrog': " + System.lineSeparator() + "User info: " + userService.updateUserById(
                frogKey, new User.Builder("fffrog", "fffrog@email.com")
                        .build()));
//        System.out.println(userService.updateUserById(
//                UUID.randomUUID(), new User.Builder("ppprog", "")
//                        .build())); // validation not passed
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("userService.deleteUserById()");
        System.out.println("pass UUID 'frogKey'! " + System.lineSeparator() + "User info: " +  userService.deleteUserById(frogKey));
        System.out.println();
        System.out.println("pass UUID 'frogKey(already deleted)'! " + System.lineSeparator() + "User info: " + userService.deleteUserById(frogKey));
        System.out.println();
        System.out.println();
    }
    private static void testMessage() {
        Message hi = Message.createMessage("hi");
        Message lo = Message.createMessage("lo");

        MessageService messageService = MessageServiceProxy.getInstance();
        System.out.println("---------------------------------");
        System.out.println("messageService.createMessage()");
        System.out.println("pass Message 'hi'! " + System.lineSeparator() + "Message info: " + messageService.createMessage(hi));
        System.out.println();
        System.out.println("pass Message 'lo'! " + System.lineSeparator() + "Message info: " + messageService.createMessage(lo));
        System.out.println();
        System.out.println("pass Message 'lo(already exist)'! " + System.lineSeparator() + "Message info: " + messageService.createMessage(lo));
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("messageService.findMessageById()");
        UUID hiKey = hi.getId();
        UUID loKey = lo.getId();
        System.out.println("pass UUID 'hiKey'! " + System.lineSeparator() + "Message info: " + messageService.findMessageById(hiKey));
        System.out.println();
        System.out.println("pass UUID 'loKey'! " + System.lineSeparator() + "Message info: " + messageService.findMessageById(loKey));
        System.out.println();
        System.out.println("pass UUID 'unknownKey'! " + System.lineSeparator() + "Message info: " + messageService.findMessageById(UUID.randomUUID()));
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("messageService.updateMessageById()");
        System.out.println("pass UUID 'hiKey' and Message 'mid': " + System.lineSeparator() + "Message info: " +
                messageService.updateMessageById(
                        hiKey, Message.createMessage("mid")));
//        System.out.println(messageService.update(
//                UUID.randomUUID(), Message.createMessage("mmmmmmmmmmmmmmmmmmmmm"))); // validation not passed
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("messageService.deleteMessageById()");
        System.out.println("pass UUID 'hiKey'! " + System.lineSeparator() + "Message info: " +  messageService.deleteMessageById(hiKey));
        System.out.println();
        System.out.println("pass UUID 'hiKey(already deleted)'! " + System.lineSeparator() + "Message info: " + messageService.deleteMessageById(hiKey));
        System.out.println();
        System.out.println();
    }
    private static void testChannel() {
        Channel c1 = Channel.createChannel("c1");
        Channel c2 = Channel.createChannel("c2");

        ChannelService channelService = ChannelServiceProxy.getInstance();
        System.out.println("---------------------------------");
        System.out.println("channelService.createChannel()");
        System.out.println("pass Channel 'c1'! " + System.lineSeparator() + "Channel info: " + channelService.createChannel(c1));
        System.out.println();
        System.out.println("pass Channel 'c2'! " + System.lineSeparator() + "Channel info: " + channelService.createChannel(c2));
        System.out.println();
        System.out.println("pass Channel 'c2(already exist)'! " + System.lineSeparator() + "Channel info: " + channelService.createChannel(c2));
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("channelService.findChannelById()");
        UUID c1Key = c1.getId();
        UUID c2Key = c2.getId();
        System.out.println("pass UUID 'c1Key'! " + System.lineSeparator() + "Channel info: " + channelService.findChannelById(c1Key));
        System.out.println();
        System.out.println("pass UUID 'c2Key'! " + System.lineSeparator() + "Channel info: " + channelService.findChannelById(c2Key));
        System.out.println();
        System.out.println("pass UUID 'unknownKey'! " + System.lineSeparator() + "Channel info: " + channelService.findChannelById(UUID.randomUUID()));
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("channelService.updateChannelById()");
        System.out.println("pass UUID 'c1Key' and Channel 'c3': " + System.lineSeparator() + "Channel info: " +
                channelService.updateChannelById(
                        c1Key, Channel.createChannel("c3")));
//        System.out.println(channelService.update(
//                UUID.randomUUID(), Channel.createChannel("c12345678910"))); // validation not passed
        System.out.println();
        System.out.println();

        System.out.println("---------------------------------");
        System.out.println("channelService.deleteChannelById()");
        System.out.println("pass UUID 'c1Key'! " + System.lineSeparator() + "Channel info: " +  channelService.deleteChannelById(c1Key));
        System.out.println();
        System.out.println("pass UUID 'c1Key(already deleted)'! " + System.lineSeparator() + "Channel info: " + channelService.deleteChannelById(c1Key));
        System.out.println();
        System.out.println();
    }

    public static void main(String[] args) {
        testUser();
        testMessage();
        testChannel();
    }
}
