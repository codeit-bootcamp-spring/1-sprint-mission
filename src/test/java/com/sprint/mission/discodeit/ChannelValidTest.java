package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;

import static com.sprint.mission.discodeit.entity.ChannelType.*;

public class ChannelValidTest {
    private static void channelNameTest(ChannelService channelService) {
        // 채널 이름 예외 테스트
        System.out.println("채널 이름이 null일 경우");
        try {
            Channel channel = channelService.create(null, "코드잇 스프린트", PUBLIC);
            System.out.println("채널 생성: " + channel.getId());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("채널 이름이 비어있을 경우");
        try {
            Channel channel = channelService.create("", "코드잇 스프린트", PRIVATE);
            System.out.println("채널 생성: " + channel.getId());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("채널 이름이 100자를 초과할 경우");
        try {
            Channel channel = channelService.create("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghij" +
                    "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghija", "코드잇 스프린트", PUBLIC);
            System.out.println("채널 생성: " + channel.getId());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    static void channelDescriptionTest(ChannelService channelService) {
        // 채널 설명 예외 테스트
        System.out.println("채널 설명이 100자를 초과할 경우");
        try {
            Channel channel = channelService.create("코드잇", "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghij" +
                    "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghija", PUBLIC);
            System.out.println("채널 생성: " + channel.getId());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    static void channelTypeTest(ChannelService channelService) {
        // 채널 타입 예외 테스트
        System.out.println("채널 타입이 null일 경우");
        try {
            Channel channel = channelService.create("코드잇", "코드잇 스프린트", null);
            System.out.println("채널 생성: " + channel.getId());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        ChannelService channelService = new BasicChannelService(new FileChannelRepository());

        channelNameTest(channelService);
        System.out.println();
        channelDescriptionTest(channelService);
        System.out.println();
        channelTypeTest(channelService);
    }
}
