package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.dto.channel.FindChannelResponseDto;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Comparator;
import java.util.List;

public class DisplayChannel {

    // 채널 1개 출력
    public static void displayChannel(FindChannelResponseDto channel, UserService userService) {
        if (channel == null) {
            System.out.println("채널 없음");
            return;
        }

        printChannel(userService, channel);
    }

    // 모든 유저 모든 채널 출력
    public static void displayAllChannel(List<FindChannelResponseDto> channels, UserService userService) {

        channels.stream()
                .sorted(Comparator.comparing((FindChannelResponseDto channel) -> userService.find(channel.getOwnerId()).getEmail().split("@")[0]))
                .forEach(channel -> {
                    printChannel(userService, channel);
                });
    }

    // 채널 출력 시 공통(출력) 부분
    public static void printChannel(UserService userService, FindChannelResponseDto channel){

        System.out.print(
                "채널 주인: " + userService.find(channel.getOwnerId()).getEmail()
                        + ", 채널명: " + channel.getName()
                        + ", 카테고리: " + channel.getCategory()
                        + ", 채널 설명: " + channel.getExplanation()
                        + ", 멤버: "
        );

        // 멤버 목록 출력
        // stream 쓰지 않아도 list에 forEach 메서드 내장되어 있음
        channel.getMembers().forEach(member -> {
            System.out.print(userService.find(member).getEmail() + " ");
        });

        System.out.println();
    }
}