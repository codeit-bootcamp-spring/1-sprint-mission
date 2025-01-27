package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.view.DisplayChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class ChannelTest {

    // 채널 생성
    public static Channel setUpChannel(User user, ChannelService channelService, UserService userService) throws IOException {

//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//        System.out.println("채널명 입력 :");
//        String name = br.readLine();
//
//        System.out.println("채널 설명 입력 :");
//        String explanation = br.readLine();

        String name = "test";
        String explanation = "테스트용 채널입니다.";

        Channel channel = new Channel(user, name, explanation);

        channelService.craete(channel);

        System.out.println("================================================================================");
        System.out.println("채널 생성 결과 : ");
        DisplayChannel.displayChannel(channel, userService);
        System.out.println("================================================================================");

        return channel;
    }

    // 채널 정보 수정
    public static void updateChannel(UUID id, ChannelService channelService, UserService userService) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


        loopOut:
        while (true) {
            System.out.println("================================================================================");
            System.out.println("1. 현재 채널 정보 확인");
            System.out.println("2. 카테고리 변경");
            System.out.println("3. 채널명 변경");
            System.out.println("4. 채널 설명 변경");
            System.out.println("5. 종료");
            System.out.println("================================================================================");

            String menu = br.readLine();

            Channel channel = channelService.read(id);

            try {
                switch (menu) {
                    case "1":
                        System.out.println("================================================================================");
                        System.out.println("현재 정보 : ");
                        DisplayChannel.displayChannel(channel, userService);
                        System.out.println("================================================================================");
                        break;

                    case "2":
                        System.out.println("카테고리 입력 :");
                        String category = br.readLine();
                        channelService.updateCategory(id, category);
                        break;

                    case "3":
                        System.out.println("채널명 입력 :");
                        String name = br.readLine();
                        channelService.updateName(id, name);
                        break;

                    case "4":
                        System.out.println("채널 설명 입력 :");
                        String explanation = br.readLine();
                        channelService.updateExplanation(id, explanation);
                        break;

                    case "5":
                        System.out.println("종료합니다.");
                        break loopOut;

                    default:
                        System.out.println("1 ~ 5 중 하나를 선택해주세요.");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    // 채널 멤버 추가
    public static void addMember(UUID id, UUID memberId, ChannelService channelService, UserService userService) {

        Channel channel = channelService.read(id);

        System.out.println("멤버 추가 전 :");
        DisplayChannel.displayChannel(channel, userService);

        channelService.addMember(id, memberId);
        channel = channelService.read(id);

        System.out.println("멤버 추가 후 :");
        DisplayChannel.displayChannel(channel, userService);
    }

    // 채널 멤버 삭제
    public static void deleteMember(UUID id, UUID memberId, ChannelService channelService, UserService userService) {

        Channel channel = channelService.read(id);

        System.out.println("멤버 삭제 전 :");
        DisplayChannel.displayChannel(channel, userService);

        channelService.deleteMember(id, memberId);
        channel = channelService.read(id);

        System.out.println("멤버 삭제 후 :");
        DisplayChannel.displayChannel(channel, userService);
    }

    // 채널 삭제
    public static void deleteChannel(UUID id, ChannelService channelService, UserService userService) {

        System.out.println("================================================================================");
        System.out.println("채널 삭제 전 목록 :");
        DisplayChannel.displayAllChannel(channelService.readAll(), userService);
        System.out.println("================================================================================");

        channelService.delete(id);

        System.out.println("================================================================================");
        System.out.println("채널 삭제 후 목록 :");
        DisplayChannel.displayAllChannel(channelService.readAll(), userService);
        System.out.println("================================================================================");
    }
}
