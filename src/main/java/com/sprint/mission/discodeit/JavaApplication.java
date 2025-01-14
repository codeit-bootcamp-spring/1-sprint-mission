package com.sprint.mission.discodeit;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.validation.MessageValidator;

import java.util.Map;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {

//        userServiceTest();
//        channelServiceTest();
        messageServiceTest();
    }

    static void userServiceTest(){

        UserService userService = new JCFUserService();


        User user1 = userService.CreateUser("설유일","tjf7894@gmail.com", "tjf7894", "1Q2w3e4r@");
        User user2 = userService.CreateUser("홍길동","길동@gmail.com", "gildong","1q2w3e@");

        System.out.println("-------------사용자 조회---------------------");
        User printUser =  userService.getUser(user1.getuuID());
        System.out.println(printUser.toString());

        System.out.println("----------------모든 사용자 조회--------------");
        Map<UUID, User> allPrintUser = userService.getAllUsers();
        for(User user  : allPrintUser.values()){
            System.out.println("=====================================");
            System.out.println(user.toString());
            System.out.println("=====================================");

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
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageValidator validationCheck = new MessageValidator(channelService, userService);
        MessageService messageService = new JCFMessageService(validationCheck);

        Channel channel1 = channelService.createChannel("CH.1");
        Channel channel2 = channelService.createChannel("CH.2");

        System.out.println("---------------채널 조회------------------");
        Channel printCh = channelService.getChannel(channel1.getuuId());
        System.out.println(printCh.toString());


        System.out.println("----------------모든 채널 조회-------------------");
        Map<UUID, Channel> allPrintCh = channelService.getAllChannels();

        for (Channel channel : allPrintCh.values()) {
            System.out.println("===============================================");
            System.out.println(channel.toString());
            System.out.println("===============================================");

        }

        System.out.println("---------------채널 이름 수정--------------");
        channelService.updateChannel(channel1.getuuId(), "ch.수정 테스트");
        System.out.println(printCh.toString());

        System.out.println("----------------채널 삭제----------------");
        channelService.deleteChannel(channel1.getuuId());
        allPrintCh = channelService.getAllChannels();
        for (Channel channel : allPrintCh.values()) {
            System.out.println("===============================================");
            System.out.println(channel.toString());
            System.out.println("===============================================");

        }


    }

    static void messageServiceTest(){
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageValidator messageValida = new MessageValidator(channelService, userService);
        MessageService messageService = new JCFMessageService(messageValida);

        User user1 =  userService.CreateUser("설유일","tjf7894@gmail.com", "tjf7894", "1Q2w3e4r@");
        User user2 = userService.CreateUser("홍길동","qasdzxc7894@gmail.com", "gildong","1Q2w3e4r@");


        Channel channel1 = channelService.createChannel("CH.1");
        Channel channel2 = channelService.createChannel("CH.2");



        System.out.println("메시지 추가 addChannelMsg ");
        Message msg1 = messageService.CreateMsg(user1, channel1, "안녕");
        Message msg2 = messageService.CreateMsg(user2, channel2, "hello");

        System.out.println("-----------------조회 getMessage ------------------");
        Message printMsg =  messageService.getMessage(msg1.getMsguuId());
        User sendUser = userService.getUser(printMsg.SendUser().getuuID());
        Channel destinationCh = channelService.getChannel(printMsg.getDestinationChannel().getuuId());

        System.out.println(
                "목적지 채널 : "+ destinationCh.getChannelName() +"\n" +
                        "보낸 유저 : " +sendUser.getName() +"\n"+
                        "메시지 내용 : " + printMsg.getContent());

        System.out.println("-----------------모든 메시지 조회 getAllMsg()------------------");
        Map<UUID, Map<UUID, Message>> allMessages = messageService.getAllMsg();

        for (Map<UUID, Message> channelMessages : allMessages.values()) {
            for (Message message : channelMessages.values()) {
                System.out.println("===============================================");
                System.out.println(message.toString());
                System.out.println("===============================================");
            }
        }


        System.out.println("-----------------메시지 내용 수정 updateMsg()------------------");
        messageService.updateMsg(msg1.getMsguuId() ,  "바뀐내용");

        System.out.println("-----------------수정 후------------------");
        System.out.println(
                "목적지 채널 : "+ destinationCh.getChannelName() +"\n" +
                        "보낸 유저 : " +sendUser.getName() +"\n"+
                        "메시지 내용 : " + printMsg.getContent());


        System.out.println("-----------------메시지 삭제 deleteMsg(UUID msgId)------------------");
        messageService.deleteMsg(msg1.getMsguuId());;
        for (Map<UUID, Message> channelMessages : allMessages.values()) {
            for (Message message : channelMessages.values()) {
                System.out.println("===============================================");
                System.out.println(message.toString());
                System.out.println("===============================================");
            }
        }
    }
}
