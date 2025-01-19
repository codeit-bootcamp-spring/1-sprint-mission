package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

/*
현재는 유저, 채널, 메세지가 서로에게 영향을 주지 않는 코드를 구성하여 개별적으로 돌아가게 되어있지만 추후 서로 연결되도록 할 것입니다!
 */
    /*
    질문1. 각JCFService 에서의 출력메서드 관련
    원래 이렇게 단건 다건 출력 메서드를 지정해 놓는 것이 맞는건가요..?
    제가 원래는 이 모든 출력 메서드들을 전부 메인에 정의해놨었는데 추후 수정이 불편할 거 같아서
    이렇게 분리하는 코드로 변경했습니다. 이 선택이 옳은 걸까요?

    질문2. 향상된 for문
    향상된 for문이 읽기 전용이라 수정이나 삭제가 되지 않는다는 것을 알게 되었는데 혹시 기본 for문 말고도 대체할 수 있는 방법 중 좋은 것이 있을까요..?
     */

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService(channelService);

        // 유저 생성
        User alice = userService.createUser("Alice", "password123");
        User belle = userService.createUser("Belle", "password456");

        System.out.println("\n유저 생성 완료-------");
        //유저 전체 조회
        System.out.println("\n유저 다건 조회-------");
        userService.printAllUsers();

        // 유저 단건 조회 (alice)
        System.out.println("\n단건 유저 조회-------");
        userService.printSingleUser(alice.getId());

        // 유저 이름 수정
        userService.updateUsername(alice.getId(), "Kim");
        userService.updatePassword(alice.getId(), "thisisnewpassword");
        System.out.println("\n유저 수정 완료 (유저이름, 비밀번호)-------");
        userService.printSingleUser(alice.getId());

        // 전체 조회
        System.out.println("\n유저 다건 조회-------");
        userService.printAllUsers();

        // 유저 삭제
        System.out.println("\n유저 삭제-------");
        if (userService.deleteUser(alice.getId())) {
            System.out.println("유저 삭제 완료.");
        } else {
            System.out.println("삭제 실패: 해당 유저를 찾을 수 없습니다.");
        }

        // 삭제 후 전체 조회
        System.out.println("\n유저 다건 조회 (삭제 후)-------");
        userService.printAllUsers();



        System.out.println("\n============================================================================================================\n");

//============================================================================================================
        // 채널 생성
        System.out.println("\n");
        System.out.println("채널 생성 완료-------");
        Channel channel1 = channelService.createChannel("General");
        Channel channel2 = channelService.createChannel("channel2");
        //채널 전체 조회
        System.out.println("\n채널 다건 조회-------");
        channelService.printAllChannels();

        // 채널 단건 조회 (channel1)
        System.out.println("\n단건 채널 조회-------");
        channelService.printSingleChannel(channel1.getId());


        // 채널 이름 수정
        channelService.updateChannelName(channel1.getId(), "changedChannel");
        System.out.println("\n채널 수정 완료 (채널 이름)-------");
        channelService.printSingleChannel(channel1.getId());

        // 전체 조회
        System.out.println("\n채널 다건 조회-------");
        channelService.printAllChannels();

        // 채널 삭제
        System.out.println("\n채널 삭제-------");
        if (channelService.deleteChannel(channel1.getId())) {
            System.out.println("채널 삭제 완료.");
        } else {
            System.out.println("삭제 실패: 해당 채널를 찾을 수 없습니다.");
        }

        // 삭제 후 전체 조회
        System.out.println("\n채널 다건 조회 (삭제 후)-------");
        channelService.printAllChannels();


        System.out.println("\n============================================================================================================\n");
//============================================================================================================


        // 채널 생성
        Channel generalChannel = channelService.createChannel("General");
        Channel randomChannel = channelService.createChannel("Random");
        channelService.printAllChannels();
        System.out.println("\n- - - - - \n");

        // 메세지 생성
        System.out.println("\n메세지 생성 완료-------");
        Message message1 = messageService.createMessage("Kim", generalChannel.getId(), "Hello, everyone!");
        Message message2 = messageService.createMessage("Lee", randomChannel.getId(), "This is a second message.");
        Message message3 = messageService.createMessage("Park", channel2.getId(), "HAHAHA");
        System.out.println("\n메세지 다건 조회-------");
        messageService.printAllMessages();

        // 메세지 단건 조회
        System.out.println("\n단건 메세지 조회-------");
        messageService.printSingleMessage(message1.getId());

        // 메세지 수정
        messageService.updateMessageContent(message1.getId(), "Updated Hello, everyone!");
        messageService.updateMessageContent(message2.getId(), "Updated This is a second message.");
        messageService.updateMessageContent(message3.getId(), "Updated HAHAHA");
        System.out.println("\n메세지 수정 완료 (메세지 내용)-------");
        messageService.printSingleMessage(message1.getId());

        // 전체 메세지 조회
        System.out.println("\n메세지 다건 조회-------");
        messageService.printAllMessages();

        // 메세지 삭제
        System.out.println("\n메세지 삭제-------");
        if (messageService.deleteMessage(message1.getId())) {
            System.out.println("메세지 삭제 완료.");
        } else {
            System.out.println("삭제 실패: 해당 메세지를 찾을 수 없습니다.");
        }

        // 삭제 후 전체 메세지 조회
        System.out.println("\n메세지 다건 조회 (삭제 후)-------");
        messageService.printAllMessages();
    }

}
