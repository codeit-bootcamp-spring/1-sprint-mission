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

import java.util.Map;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {

//        userServiceTest();
        channelServiceTest();
//        messageServiceTest();
    }

    static void userServiceTest(){

        UserService userService = new JCFUserService();

        //테스트 유저 생성
        User user1 = new User("설유일","tjf7894@gmail.com", "tjf7894", "1q2w3e4r@");
        User user2 = new User("홍길동","길동@gmail.com", "gildong","1q2w3e@");

        //유저서비스 추가
        userService.userServiceAdd(user1);
        userService.userServiceAdd(user2);


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
        userService.deleteUser(user2);
        allPrintUser = userService.getAllUsers();
        for(User user  : allPrintUser.values()){
            System.out.println("=====================================");
            System.out.println(user.toString());
            System.out.println("=====================================");

        }
    }

    static void channelServiceTest(){
        ChannelService channelService = new JCFChannelService();

        //테스트 채널 생성
        Channel channel1 = new Channel("CH.1");
        Channel channel2 = new Channel("CH.2");

        channelService.addChannel(channel1);
        channelService.addChannel(channel2);

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


        //채널 삭제
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
        MessageService messageService = new JCFMessageService();
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();

        User user1 = new User("설유일","tjf7894@gmail.com", "tjf7894", "1q2w3e4r@");
        User user2 = new User("홍길동","길동@gmail.com", "gildong","1q2w3e@");

        userService.userServiceAdd(user1);
        userService.userServiceAdd(user2);

        Channel channel1 = new Channel("CH.1");
        Channel channel2 = new Channel("CH.2");

        channelService.addChannel(channel1);
        channelService.addChannel(channel2);

        Message msg1 = new Message(user1, channel1, "안녕");
        Message msg2 = new Message(user1, channel1, "hello");


        System.out.println("메시지 추가 addChannelMsg ");
        messageService.addChannelMsg(msg1);
        messageService.addChannelMsg(msg2);

        System.out.println("-----------------조회 getMessage ------------------");
        Message printMsg =  messageService.getMessage(msg1.getMsguuId());
        User sendUser = userService.getUser(printMsg.SendUseruuId());
        Channel destinationCh = channelService.getChannel(printMsg.getDestinationChannel());

        System.out.println(
                "목적지 채널 : "+ destinationCh.getChannelName() +"\n" +
                        "보낸 유저 : " +sendUser.getName() +"\n"+
                        "메시지 내용 : " + printMsg.getContent());

        //메시지 객체에 저장된 필드들이 객체가 아니라 UUID를 저장해서 가독성이 매우 떨어짐... 어떻게 할까..ㅇ,ㅁ
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
