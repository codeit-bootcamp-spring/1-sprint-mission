package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.factory.Factory;
import com.sprint.mission.discodeit.service.file.FileUserService;
//import com.sprint.mission.discodeit.factory.FactoryService;
import java.util.Map;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
//        userServiceTest();
//        channelServiceTest();
//        messageServiceTest();
//        FactoryService factoryService = new FactoryService();
//        //UserService 인스턴스 생성
//        factoryService.createFileUserRepository();
//        factoryService.createUserValidator();
//        UserService basicUserService = factoryService.createBasicUserService();
//
//        //ChannelService 인스턴스 생성
//        factoryService.createFileChannelRepository();
//        factoryService.createChannelValidator();
//        ChannelService basicChannelService = factoryService.createBasicChannelService();
//
//        //MessageService 인스턴스 생성
//        factoryService.createMessageValidator();
//        factoryService.createFileMessageRepository();
//        MessageService basicMessageService = factoryService.createBasicMessageService();
//        factoryService.createObserverManager();
//        factoryService.createChannelObserver();


//        User user1 = basicUserService.CreateUser("코드일","codeit1234@codeit.com","codeit1234","1Q2w3e4r@");
//        User user2 = basicUserService.CreateUser("코드이","code1234@codeit.com","codeit1234","1q2W3e4r@");
//
//        System.out.println("----------------모든 사용자 조회--------------");
//        Map<UUID, User> allPrintUser = basicUserService.getAllUsers();
//        for(User user  : allPrintUser.values()){
//            System.out.println(user);
//            System.out.println("-----------------------------------------------");
//        }
//
////        Channel ch1 = basicChannelService.createChannel("CH.1");
////        Channel ch2 = basicChannelService.createChannel("CH.2");
//
//        Map<UUID, Channel> allprintChannel = basicChannelService.getAllChannels();
//        for(Channel channel : allprintChannel.values()){
//            System.out.println(channel);
//            System.out.println("-----------------------------------------------");
//        }
//
////        Message msg1 = basicMessageService.createMsg(user1,ch1,"user1이 보내는 첫번째 테스트 메시지");
////        Message msg2 = basicMessageService.createMsg(user1,ch1,"user1이 보내는 두번째 테스트 메시지");
////        Message msg3 = basicMessageService.createMsg(user1,ch1,"user1이 보내는 세번째 테스트 메시지");
////        Message msg4 = basicMessageService.createMsg(user2,ch2,"user2이 보내는 첫번째 테스트 메시지");
////        Message msg5 = basicMessageService.createMsg(user2,ch2,"user2이 보내는 두번째 테스트 메시지");
//
//        System.out.println("-----------------모든 메시지 조회 getAllMsg()------------------");
//        Map<UUID, Map<UUID, Message>> allMessages = basicMessageService.getAllMsg();
//
//        for (Map<UUID, Message> channelMessages : allMessages.values()) {
//            for (Message message : channelMessages.values()) {
//                System.out.println(message);
//                System.out.println("===============================================");
//            }
//        }

    }

}
