package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.dto.message.FindMessageResponseDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

public class DisplayMessage {

    // 메시지 1건 조회
    public static void displayMessage(FindMessageResponseDto message, UserService userService, ChannelService channelService) {

        System.out.println(
                "채널: " + channelService.find(message.getChannelId()).getName()
                        + ", 작성자: " + userService.find(message.getWriterId()).getEmail()
                        + ", 내용: " + message.getContext()
        );
    }

    // 메시지 전체 조회
    public static void displayAllMessage(List<FindMessageResponseDto> messages, UserService userService, ChannelService channelService) {

        if (messages == null) {
            System.out.println("메시지 없음");
            return;
        }
        messages.forEach(message -> {
                    System.out.println(
                            "채널: " + channelService.find(message.getChannelId()).getName()
                                    + ", 작성자: " + userService.find(message.getWriterId()).getEmail()
                                    + ", 내용: " + message.getContext()
                    );
                });
    }
}
