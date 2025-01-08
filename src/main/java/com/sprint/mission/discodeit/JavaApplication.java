package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.service.JCFChannelService;
import com.sprint.mission.discodeit.service.JCFMessageService;
import com.sprint.mission.discodeit.service.JCFUserService;

public class JavaApplication {
    public static <UUID> void main(String[] args) {
        //-> 도메인 별 서비스 구현체 테스트
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        //등록
        //user 등록
         userService.createUser("Alice");
         userService.createUser("Charlie");
         userService.createUser("Liz");
        userService.getAllUsers().forEach(user ->
                System.out.println("User: " + user.getUsername()));
        System.out.println();

        //Channel 등록
        channelService.createChannel("General");
        channelService.createChannel("Premium");
        channelService.createChannel("Standard");

        channelService.getAllChannels().forEach(channel ->
                System.out.println("Channel: " + channel.getChannelName()));
        System.out.println();

        //Message 등록
            var userId = userService.getAllUsers().get(0).getId(); // 첫번째 인덱스에 있는 userId 반환
            var channelId = channelService.getAllChannels().get(0).getId(); //위와 동일한 구성

        messageService.createMessage("new Message arrived", userId, channelId);
        messageService.getAllMessages().forEach(message ->
                System.out.println("Content: " + message.getContent()));


        System.out.println();
        //조회(단건, 다건)
        //수정 -> 수정된 데이터 조회
        userService.updateUser(userService.getUser(userId), "Hazel"); //id 부분 선언 후 변경 필요
        userService.getAllUsers().forEach(user ->
                System.out.println("수정 후 Users : " + user.getUsername())); //수정이 잘 되었는지 확인 필요
        System.out.println();

        //삭제
        //조회를 통해 삭제되었는지 확인
       channelService.deleteChannel(channelService.getChannel(channelId)); //id 부분 선언 후 변경 필요
        channelService.getAllChannels().forEach(channel ->
                System.out.println("남아있는 Channel : " + channel.getChannelName()));

    }
}
