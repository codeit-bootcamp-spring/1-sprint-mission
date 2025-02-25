//package com.sprint.mission;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.service.basic.*;
//import com.sprint.mission.discodeit.service.file.FileService;
//import com.sprint.mission.discodeit.service.jcf.JCFServiceJCF;
//
//import java.io.IOException;
//import java.io.Serializable;
//
//public class JavaApplicationMain {
//    static JCFServiceJCF jcfService = JCFServiceJCF.getInstance();
//    static FileService fileService = FileService.getInstance();
//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        User user1 = fileService.saveUser("Name1");
//        fileService.showInfoUser(user1);
//
//        System.out.println(" ");
//        fileService.modifyUser(user1,"ReName1");
//        fileService.showInfoUser(user1);
//
//        System.out.println(" ");
//        Channel channel1 = fileService.saveChannel("Channel1",user1);
//        fileService.showInfoChannel(channel1);
//
//        System.out.println(" ");
//        Message message1 = fileService.saveMessage("Hello World!", user1,channel1);
//        fileService.showInfoMessage(message1);
//
//        fileService.modifyChannel(channel1,"RenameChannel1");
//
//        System.out.println("\n");
//        User user2 = fileService.saveUser("Name2");
//        fileService.showInfoUser(user2);
//
//        fileService.joinChannel(channel1,user2);
//        fileService.deleteUser(user1);
//
//        System.out.println("\n");
//        fileService.showInfoChannel(channel1);
//
//        fileService.modifyMessage(message1,"Hello World!!");
//
//        System.out.println("\n");
//        fileService.showInfoMessage(message1);
//
//        fileService.deleteChannel(channel1);
//
//        UserService userService = BasicUserService.getInstance();
//        ChannelService channelService = BasicChannelService.getInstance();
//        MessageService messageService = BasicMessageService.getInstance();
//
//        // 셋업
//        User user = setupUser(userService);
//        Channel channel = setupChannel(channelService);
//        // 테스트
//        messageCreateTest(messageService, channel, user);
//
//    }
//
//    private static User setupUser (UserService userService) throws IOException, ClassNotFoundException {
//        return userService.saveUser();
//    }
//    private static Channel setupChannel (ChannelService channelService) throws IOException, ClassNotFoundException {
//        return channelService.saveChannel();
//    }
//    private static void messageCreateTest (MessageService messageService,Channel channel, User user) throws IOException, ClassNotFoundException {
//        messageService.saveMessage("no content",user,channel);
//    }
//}
