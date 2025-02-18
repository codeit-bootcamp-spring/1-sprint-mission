package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.view.DisplayChannel;
import com.sprint.mission.discodeit.view.DisplayMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class MessageTest {

    // 메시지 생성
    public static Message setUpMessage(Channel channel, UUID writerId, MessageService messageService, ChannelService channelService, UserService userService) throws IOException {

//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//        System.out.println("메시지 내용 입력 :");
//        String context = br.readLine();

        String context = "테스트용 메시지입니다.";

        Message message = new Message(channel, context, writerId);

        messageService.craete(message);

        System.out.println("================================================================================");
        System.out.println("메시지 생성 결과 : ");
        DisplayMessage.displayMessage(message, userService, channelService);
        System.out.println("================================================================================");

        return message;
    }

    // 메시지 수정
    public static void updateMessage(UUID id, MessageService messageService, ChannelService channelService, UserService userService) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


        loopOut:
        while (true) {
            System.out.println("================================================================================");
            System.out.println("1. 현재 메시지 정보 확인");
            System.out.println("2. 메시지 내용 변경");
            System.out.println("3. 종료");
            System.out.println("================================================================================");

            String menu = br.readLine();

            Message message = messageService.read(id);

            try {
                switch (menu) {
                    case "1":
                        System.out.println("================================================================================");
                        System.out.println("현재 정보 : ");
                        DisplayMessage.displayMessage(message, userService, channelService);
                        System.out.println("================================================================================");
                        break;

                    case "2":
                        System.out.println("메시지 입력 :");
                        String context = br.readLine();
                        messageService.updateContext(id, context);
                        break;

                    case "3":
                        System.out.println("종료합니다.");
                        break loopOut;

                    default:
                        System.out.println("1 ~ 3 중 하나를 선택해주세요.");
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    // 메시지 삭제
    public static void deleteMessage(UUID id, MessageService messageService, ChannelService channelService, UserService userService) {

        System.out.println("================================================================================");
        System.out.println("메시지 삭제 전 목록 :");
        DisplayMessage.displayAllMessage(messageService.readAll(), userService, channelService);
        System.out.println("================================================================================");

        messageService.delete(id);

        System.out.println("================================================================================");
        System.out.println("메시지 삭제 후 목록 :");
        DisplayMessage.displayAllMessage(messageService.readAll(), userService, channelService);
        System.out.println("================================================================================");
    }
}