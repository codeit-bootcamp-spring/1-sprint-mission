package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

/*
해당 코드 내에 기존의 jcf crud 코드와 새로 작성된 file crud 코드 모두 위치해 있습니다
테스트 진행시 주석처리를 해제하거나 주석을 해서 진행을 해야합니다.
질문 1: 현재 주석처리로 테스트를 진행해야 하는데 코드를 나눌 수 있는 다른 방법 중 효율적인 방법이 혹시.. 있을까요?

아직 list를 사용하지 않고 map을 사용하는 형식은 갖추지 못 했습니다.
방어적 복사도 아직 활용하지 못했고, for 문 대신 람다식을 활용해보는 것도 아직 진행하지 못했습니다.
이는 연휴를 활용해 최대한 구현하도록 노력하겠습니다!
 */

public class JavaApplication {
//    //jcf test시
//    /*
//    public static void main(String[] args) {
//        JCFUserService userService = new JCFUserService();
//        JCFChannelService channelService = new JCFChannelService();
//        JCFMessageService messageService = new JCFMessageService(channelService, userService); // userService 추가
//
//        // 유저 생성
//        User alice = userService.createUser("Alice", "password123");
//        User belle = userService.createUser("Belle", "password456");
//
//        System.out.println("\n유저 생성 완료-------");
//        //유저 전체 조회
//        System.out.println("\n유저 다건 조회-------");
//        userService.printAllUsers();
//
//        // 유저 단건 조회 (alice)
//        System.out.println("\n단건 유저 조회-------");
//        userService.printSingleUser(alice.getId());
//
//        // 유저 이름 수정
//        userService.updateUsername(alice.getId(), "Kim");
//        userService.updatePassword(alice.getId(), "thisisnewpassword");
//        System.out.println("\n유저 수정 완료 (유저이름, 비밀번호)-------");
//        userService.printSingleUser(alice.getId());
//
//        // 전체 조회
//        System.out.println("\n유저 다건 조회-------");
//        userService.printAllUsers();
//
//        // 유저 삭제
//        System.out.println("\n유저 삭제-------");
//        if (userService.deleteUser(alice.getId())) {
//            System.out.println("유저 삭제 완료.");
//        } else {
//            System.out.println("삭제 실패: 해당 유저를 찾을 수 없습니다.");
//        }
//
//        // 삭제 후 전체 조회
//        System.out.println("\n유저 다건 조회 (삭제 후)-------");
//        userService.printAllUsers();
//
//
//
//        System.out.println("\n============================================================================================================\n");
//
////============================================================================================================
//        // 채널 생성
//        System.out.println("\n");
//        System.out.println("채널 생성 완료-------");
//        Channel channel1 = channelService.createChannel("General");
//        Channel channel2 = channelService.createChannel("channel2");
//        //채널 전체 조회
//        System.out.println("\n채널 다건 조회-------");
//        channelService.printAllChannels();
//
//        // 채널 단건 조회 (channel1)
//        System.out.println("\n단건 채널 조회-------");
//        channelService.printSingleChannel(channel1.getId());
//
//
//        // 채널 이름 수정
//        channelService.updateChannelName(channel1.getId(), "changedChannel");
//        System.out.println("\n채널 수정 완료 (채널 이름)-------");
//        channelService.printSingleChannel(channel1.getId());
//
//        // 전체 조회
//        System.out.println("\n채널 다건 조회-------");
//        channelService.printAllChannels();
//
//        // 채널 삭제
//        System.out.println("\n채널 삭제-------");
//        if (channelService.deleteChannel(channel1.getId())) {
//            System.out.println("채널 삭제 완료.");
//        } else {
//            System.out.println("삭제 실패: 해당 채널를 찾을 수 없습니다.");
//        }
//
//        // 삭제 후 전체 조회
//        System.out.println("\n채널 다건 조회 (삭제 후)-------");
//        channelService.printAllChannels();
//
//
//        System.out.println("\n============================================================================================================\n");
////============================================================================================================
//        System.out.println("\n메시지 생성-------");
//
//        channelService.addUserToChannel(channel1.getId(), alice.getId());
//        channelService.addUserToChannel(channel2.getId(), belle.getId());
//
//        Message message1 = messageService.createMessage("Kim", channel1.getId(), "hihi");
//        Message message2 = messageService.createMessage("Belle", channel2.getId(), "my name is Belle");
////        Message message3 = messageService.createMessage("Leee", channel2.getId(), "my name is Belle");
//
//
//        Message invalidMessage = messageService.createMessage("unknownUser", channel2.getId(), "my name is unknown");
//        if (invalidMessage == null) {
//            System.out.println("Invalid user 메시지 생성 실패");
//        }
//        Message invalidChannelMessage = messageService.createMessage("Belle", UUID.randomUUID(), "Invalid channel test.");
//        if (invalidChannelMessage == null) {
//            System.out.println("Invalid channel 메시지 생성 실패");
//        }
//
//        System.out.println("\n메세지 다건 조회-------");
//        messageService.printAllMessages();
//
//        System.out.println("\n단건 메세지 조회-------");
//        if (message1 != null) {
//            messageService.printSingleMessage(message1.getId());
//        }
//        if (message2 != null) {
//            messageService.printSingleMessage(message2.getId());
//        }
//
//        System.out.println("\n메세지 삭제-------");
//        if (messageService.deleteMessage(message2.getId())) {
//            System.out.println("메세지 삭제 완료");
//        } else {
//            System.out.println("삭제 실패: 해당 메세지를 찾을 수 없습니다.");
//        }
//
//        System.out.println("\n메세지 다건 조회 (삭제 후)-------");
//        messageService.printAllMessages();
//
//    }
//    */

    //============================================================================================================
    //============================================================================================================
//    //file test시


    public static void main(String[] args) {
        // User Service
        UserService userService = new FileUserService();

        // 유저 생성
        User user1 = userService.createUser("user1", "password1");
        User user2 = userService.createUser("user2", "password2");

        // 유저 출력
        System.out.println("\n현재 유저 리스트:");
        List<User> allUsers = userService.getAllUsers();
        for (User user : allUsers) {
            System.out.println(user.getUsername());
        }

        // 유저 이름 업데이트
        userService.updateUsername(user1.getId(), "updatedUser1");

        // 최종 확인
        System.out.println("\n최종 유저 리스트:");
        for (User user : userService.getAllUsers()) {
            System.out.println(user.getUsername());
        }

        //============================================================================================================

        // Channel Service
        ChannelService channelService = new FileChannelService();

        // 채널 생성
        Channel channel1 = channelService.createChannel("channel1name");
        Channel channel2 = channelService.createChannel("channel2name");

        // 채널 출력
        System.out.println("\n현재 채널 리스트:");
        for (Channel channel : channelService.getAllChannels()) {
            System.out.println(channel.getChannelName());
        }

        // 채널 이름 업데이트
        channelService.updateChannelName(channel1.getId(), "updatedchannel1");

        // 채널 확인
        System.out.println("\n최종 채널 리스트:");
        for (Channel channel : channelService.getAllChannels()) {
            System.out.println(channel.getChannelName());
        }

        //============================================================================================================

        // Message Service
        MessageService messageService = new FileMessageService(channelService, userService);

        // 유저를 채널에 추가
        channelService.addUserToChannel(channel1.getId(), user1.getId());
        channelService.addUserToChannel(channel2.getId(), user2.getId());

        // 메시지 생성
        Message message1 = messageService.createMessage("user1", channel1.getId(), "This is from channel 1");
        Message message2 = messageService.createMessage("user2", channel2.getId(), "This is from channel 2");

        // 메시지 출력
        System.out.println("\n현재 메시지 리스트:");
        for (Message message : messageService.getAllMessages()) {
            System.out.println("작성자: " + message.getUsername() + ", 내용: " + message.getContent());
        }

        // 메시지 업데이트
        if (message1 != null) {
            messageService.updateMessageContent(message1.getId(), "Updated message for channel1!");
        }

        // 메시지 확인
        System.out.println("\n최종 메시지 리스트:");
        for (Message message : messageService.getAllMessages()) {
            System.out.println("작성자: " + message.getUsername() + ", 내용: " + message.getContent());
        }
    }

}
