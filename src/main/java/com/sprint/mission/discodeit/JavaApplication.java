package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.FactoryService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.factory.Factory;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.Map;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
//        userServiceTest();
//      channelServiceTest();
//        messageServiceTest();
        FactoryService factoryService = new FactoryService();
        //UserService 인스턴스 생성
        factoryService.createFileUserRepository();
        factoryService.createUserValidator();
        UserService fileUserService = factoryService.createFileUserService();

        //ChannelService 인스턴스 생성
        factoryService.createFileChannelRepository();
        factoryService.createChannelValidator();
        ChannelService fileChannelService = factoryService.createFileChannelService();

        //MessageService 인스턴스 생성
        factoryService.createMessageValidator();
        factoryService.createFileMessageRepository();
        MessageService fileMessageService = factoryService.createFileMessageService();
        factoryService.createObserverManager();
        factoryService.createChannelObserver();


//        User user1 = fileUserService.CreateUser("코드일","codeit1234@codeit.com","codeit1234","1Q2w3e4r@");
//        User user2 = fileUserService.CreateUser("코드이","code1234@codeit.com","codeit1234","1q2W3e4r@");
//
//        Channel ch1 = fileChannelService.createChannel("CH.1");
//        Channel ch2 = fileChannelService.createChannel("CH.2");
//
//        Message msg1 = fileMessageService.createMsg(user1,ch1,"user1이 보내는 첫번째 테스트 메시지");
//        Message msg2 = fileMessageService.createMsg(user1,ch1,"user1이 보내는 두번째 테스트 메시지");
//        Message msg3 = fileMessageService.createMsg(user1,ch1,"user1이 보내는 세번째 테스트 메시지");
//        Message msg4 = fileMessageService.createMsg(user2,ch2,"user2이 보내는 첫번째 테스트 메시지");
//        Message msg5 = fileMessageService.createMsg(user2,ch2,"user2이 보내는 두번째 테스트 메시지");
//
//        fileMessageService.deleteMsg(msg1.getMsguuId());

        System.out.println("-----------------모든 메시지 조회 getAllMsg()------------------");
        Map<UUID, Map<UUID, Message>> allMessages = fileMessageService.getAllMsg();
        for (Map<UUID, Message> channelMessages : allMessages.values()) {
            for (Message message : channelMessages.values()) {
                System.out.println(message);
                System.out.println("===============================================");
            }
        }

//        fileChannelService.deleteChannel(ch2.getuuId());
//        System.out.println("-----------------ch2 삭제한후의 모든 메시지 출력------------------");
//
//        allMessages = fileMessageService.getAllMsg();
//        for (Map<UUID, Message> channelMessages : allMessages.values()) {
//            for (Message message : channelMessages.values()) {
//                System.out.println(message);
//                System.out.println("===============================================");
//            }
//        }



//        System.out.println("----------------모든 사용자 조회--------------");
//        Map<UUID, User> allPrintUser = fileUserService.getAllUsers();
//        for(User user  : allPrintUser.values()){
//            System.out.println(user.toString());
//            System.out.println("-----------------------------------------------");
//        }


//        System.out.println("----------------모든 채널 조회-------------------");
//        Map<UUID, Channel> allPrintCh = fileChannelService.getAllChannels();
//        for (Channel channel : allPrintCh.values()) {
//            System.out.println(channel);
//            System.out.println("===============================================");
//
//        }


    }


    static void userServiceTest(){

        Factory factory = new FactoryService();

        UserService userService = factory.createUserService();
        ChannelService channelService = factory.createChannelService();
        MessageService messageService = factory.createMessageService();

        User user1 = userService.CreateUser("설유일","tjf7894@gmail.com", "tjf7894", "1Q2w3e4r@");
        User user2 = userService.CreateUser("홍길동","killDong@gmail.com", "killdong","1Q2w3e4r@");

        Channel channel1 = channelService.createChannel("CH.1");
        Channel channel2 = channelService.createChannel("CH.2");

        Message msg1 = messageService.createMsg(user1, channel1, "안녕");
        Message msg2 = messageService.createMsg(user2, channel2, "hello");
        System.out.println("-------------사용자 조회---------------------");
        User printUser =  userService.getUser(user1.getuuID());
        System.out.println(printUser.toString());

        System.out.println("----------------모든 사용자 조회--------------");
        Map<UUID, User> allPrintUser = userService.getAllUsers();
        for(User user  : allPrintUser.values()){
            System.out.println(user.toString());
            System.out.println("-----------------------------------------------");
        }

        System.out.println("--------------사용자 정보 수정-------------");
        userService.updateUser(user1.getuuID(), "이메일 수정", "아이디 수정", "비밀번호 수정");
        System.out.println(printUser.toString());

        System.out.println("--------------사용자 삭제 후 -------------");
        userService.deleteUser(user2.getuuID());
        allPrintUser = userService.getAllUsers();
        for(User user  : allPrintUser.values()){
            System.out.println("=====================================");
            System.out.println(user.toString());
            System.out.println("=====================================");

        }
    }

    static void channelServiceTest(){

        Factory factory = new FactoryService();

        UserService userService = factory.createUserService();
        ChannelService channelService = factory.createChannelService();
        MessageService messageService = factory.createMessageService();

        User user1 = userService.CreateUser("설유일", "tjf7894@gmail.com", "tjf7894", "1Q2w3e4r@");
        User user2 = userService.CreateUser("홍길동", "killDong@gmail.com", "killdong", "1Q2w3e4r@");

        Channel channel1 = channelService.createChannel("CH.1");
        Channel channel2 = channelService.createChannel("CH.2");

        Message msg1 = messageService.createMsg(user1, channel1, "안녕");
        Message msg2 = messageService.createMsg(user2, channel2, "hello");

        System.out.println("---------------채널 조회------------------");
        channelService.getChannel(channel1.getuuId());
        System.out.println(channel1);

        System.out.println("----------------모든 채널 조회-------------------");
        Map<UUID, Channel> allPrintCh = channelService.getAllChannels();
        for (Channel channel : allPrintCh.values()) {
            System.out.println(channel);
            System.out.println("===============================================");

        }

        System.out.println("---------------채널 이름 수정--------------");
        channelService.updateChannel(channel1.getuuId(), "ch.수정 테스트");
        System.out.println(channel1);

        System.out.println("----------------채널 삭제----------------");
        channelService.deleteChannel(channel1.getuuId());
        System.out.println("----------------삭제 후 전체조회----------------");

        allPrintCh = channelService.getAllChannels();
        for (Channel channel : allPrintCh.values()) {
            System.out.println("===============================================");
            System.out.println(channel.toString());
            System.out.println("===============================================");
        }
    }

    static void messageServiceTest(){
        Factory factory = new FactoryService();

        UserService userService = factory.createUserService();
        ChannelService channelService = factory.createChannelService();
        MessageService messageService = factory.createMessageService();

        User user1 = userService.CreateUser("설유일", "tjf7894@gmail.com", "tjf7894", "1Q2w3e4r@");
        User user2 = userService.CreateUser("홍길동", "killDong@gmail.com", "killdong", "1Q2w3e4r@");

        Channel channel1 = channelService.createChannel("CH.1");
        Channel channel2 = channelService.createChannel("CH.2");

        Message msg1 = messageService.createMsg(user1, channel1, "안녕");
        Message msg2 = messageService.createMsg(user2, channel2, "hello");

        System.out.println("-----------------메시지 조회 getMessage ------------------");
        Message printMsg =  messageService.getMessage(msg1.getMsguuId());
        System.out.println(printMsg);

        System.out.println("-----------------모든 메시지 조회 getAllMsg()------------------");
        Map<UUID, Map<UUID, Message>> allMessages = messageService.getAllMsg();

        for (Map<UUID, Message> channelMessages : allMessages.values()) {
            for (Message message : channelMessages.values()) {
                System.out.println(message);
                System.out.println("===============================================");
            }
        }

        System.out.println("-----------------메시지 내용 수정 updateMsg()------------------");
        messageService.updateMsg(msg1.getMsguuId() ,  "바뀐내용");
        System.out.println("-----------------수정 후------------------");
        System.out.println(msg1);



        System.out.println("-----------------메시지 삭제 deleteMsg(UUID msgId)------------------");
        messageService.deleteMsg(msg1.getMsguuId());;
        for (Map<UUID, Message> channelMessages : allMessages.values()) {
            for (Message message : channelMessages.values()) {
                System.out.println(message);
                System.out.println("===============================================");
            }
        }
    }
}
