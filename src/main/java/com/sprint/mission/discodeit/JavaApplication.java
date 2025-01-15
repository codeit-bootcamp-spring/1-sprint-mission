package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.*;


public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();

        User user1 = new User("성근", "leesin014@naver.com", "pad13");
        userService.registerUser(user1);
        User user2 = new User("승현", "asdafg@gmail.com", "123123123");
        userService.registerUser(user2);
        User user3 = new User("동아", "asd@nate.com", "23asd3");
        userService.registerUser(user3);
        User user4 = new User("정우", "wjddn@naver.com", "222asd33");
        userService.registerUser(user4);

        System.out.println("----------모든 사용자 정보 출력----------");
        userService.getAllUser();
        System.out.println("----------동아 라는 이름을 가진 사용자 정보 출력----------");
        userService.getUserInfo("동아");

        System.out.println("----------사용자 정보(이름,이메일) 수정----------");
        userService.updateUserName(user3, "코드잇");
        userService.updateUserEmail(user3, "codeit@code.com");
        userService.getAllUser();

        System.out.println("----------사용자 삭제----------");
        userService.deleteUser("정우", "222asd33");
        userService.getAllUser();

        Channel channel1 = new Channel("codeit", user1);
        channelService.createChannel(channel1);
        Channel channel2 = new Channel("LOL", user2);
        channelService.createChannel(channel2);

        channel1.addMember(user2);
        channel1.addMember(user3);

        System.out.println("\n\n");
        System.out.println("----------모든 채널 목록 출력----------");
        System.out.println(channelService.getAllChannel());
        System.out.println("----------채널1의 모든 멤버 목록 출력----------");
        System.out.println(channelService.getAllMember(channel1));
        System.out.println("----------채널2의 모든 멤버 목록 출력----------");
        System.out.println(channelService.getAllMember(channel2));
        System.out.println("----------관리자가 아닌 사용자의 채널 이름 변경----------");
        channelService.updateChannel(channel1, "스프린트", user2);

        System.out.println("----------채널2 삭제, 메시지 삭제----------");
        channelService.deleteChannel(channel2, user2);
        messageService.deleteChannelMessage(channel2);

        System.out.println("----------모든 채널 목록 출력----------");
        System.out.println(channelService.getAllChannel());


        Message message1 = new Message(channel1, user1, "시험 메시지");
        Message message2 = new Message(channel1, user1, "안녕하세요 관리자입니다.");
        Message message3 = new Message(channel1, user2, "안녕하세요 저는 멤버입니다");
        Message message4 = new Message(channel2, user2, "반갑습니다");
        Message message5 = new Message(channel1, user1, "시험 메시지2");

        //존재하지 않는 채널일때 매시지 생성 불가능 처리.
        if (channelService.channelExits(message1.getChannel())) {
            messageService.createMessage(message1);
        }
        if (channelService.channelExits(message2.getChannel())) {
            messageService.createMessage(message2);
        }
        if (channelService.channelExits(message3.getChannel())) {
            messageService.createMessage(message3);
        }
        if (channelService.channelExits(message4.getChannel())) {
            messageService.createMessage(message4);
        }
        if (channelService.channelExits(message5.getChannel())) {
            messageService.createMessage(message5);
        }

        System.out.println("\n\n");
        System.out.println("----------user1이 작성한 모든 메시지 출력----------");
        System.out.println(messageService.getWriterMessage(user1));

        System.out.println("----------채널1에서 작성한 모든 메시지 출력----------");
        System.out.println(messageService.getChannelMessage(channel1));

        System.out.println("----------모든 메시지 출력----------");
        System.out.println(messageService.getAllMessage());

        System.out.println("----------message1 삭제----------");
        messageService.deleteMessage(message1, user1);

        System.out.println("----------user1이 작성한 모든 메시지 출력----------");
        System.out.println(messageService.getWriterMessage(user1));

        System.out.println("----------채널1에서 작성한 모든 메시지 출력----------");
        System.out.println(messageService.getChannelMessage(channel1));

        System.out.println("----------메시지2 내용 수정----------");
        messageService.updateMessage(message2, "수정된 메시지", user1);
        System.out.println(messageService.getChannelMessage(channel1));

    }
}
