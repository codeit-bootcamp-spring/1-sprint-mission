package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();

        // User 등록
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        System.out.println("=== 유저 등록 ===");
        userService.createUser(userId1, System.currentTimeMillis(), System.currentTimeMillis(), "Alice");
        System.out.println(userService.getUser(userId1).getName() + " 유저가 등록되었습니다.");
        userService.createUser(userId2, System.currentTimeMillis(), System.currentTimeMillis(), "Bob");
        System.out.println(userService.getUser(userId2).getName() + " 유저가 등록되었습니다.");
        System.out.println();

        // 사용자 단건 조회
        System.out.println("=== 유저 단건 조회 ===");
        User user1 = userService.getUser(userId1);
        System.out.println("User1: " + user1.getName());

        // 4. 모든 사용자 조회
        System.out.println("\n=== 모든 유저 조회 ===");
        userService.getAllUsers().forEach(user ->
                System.out.println("User: " + user.getName())
        );

        // 5. 사용자 수정
        System.out.println("\n=== 유저 정보 수정 ===");
        userService.updateUser(userId1, "AliceUpdated", System.currentTimeMillis());

        // 6. 수정된 데이터 조회
        System.out.println("\n=== 수정된 데이터 조회 ===");
        User modifiedUser = userService.getUser(userId1);
        System.out.println("Modified User: " + modifiedUser);

        // 7. 사용자 삭제
        System.out.println("\n=== 유저 삭제 ===");
        userService.deleteUser(userId2);

        // 8. 삭제된 데이터 확인
        System.out.println("\n=== 삭제된 유저 데이터 확인 ===");
        User deletedUser = userService.getUser(userId2);
        System.out.println("Deleted User (Should be null): " + deletedUser);

        System.out.println("\n=== 최종 유저 데이터 확인 ===");
        userService.getAllUsers().forEach(user ->
                System.out.println("User: " + user.getName())
        );

        ChannelService channelService = new JCFChannelService();

        // 채널 등록
        UUID channelId1 = UUID.randomUUID();
        UUID channelId2 = UUID.randomUUID();
        channelService.createChannel(channelId1, System.currentTimeMillis(), System.currentTimeMillis(), "Channel1");
        channelService.createChannel(channelId2, System.currentTimeMillis(), System.currentTimeMillis(), "Channel2");

        // 채널 단건 조회
        System.out.println("=== 채널 단건 조회 ===");
        Channel channel1 = channelService.getChannel(channelId1);
        System.out.println("Channel1: " + channel1.getName());
    }
}
