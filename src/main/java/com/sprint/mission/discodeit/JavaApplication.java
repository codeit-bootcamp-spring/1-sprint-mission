package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.jcf.*;

import java.util.UUID; // UUID 임포트 추가

public class JavaApplication {
    public static void main(String[] args) {
        // 1. UserService 테스트
        System.out.println("== 사용자(UserService) 테스트 ==");
        JCFUserService userService = new JCFUserService();

        // 사용자 생성
        // User : 객체 생성 -> 이름과 이메일을 생성자 전달
        // new ( 인스턴스 생성할 예정)
        User user = new User("장건희", "gunhee0760@gmail.com");
        userService.create(user);
        //Create -> User를 서비스에 저장
        System.out.println("생성된 사용자: " + userService.read(user.getId()));

        // 2. ChannelService 테스트
        System.out.println("\n== 채널(ChannelService) 테스트 ==");
        JCFChannelService channelService = new JCFChannelService();

        // 채널 생성
        Channel channel = new Channel("General", "일반 테스트 채널");
        channelService.create(channel);
        System.out.println("생성된 채널: " + channelService.read(channel.getId()));

        // 3. MessageService 테스트
        System.out.println("\n== 메시지(MessageService) 테스트 ==");
        JCFMessageService messageService = new JCFMessageService(userService, channelService);

        // 사용자와 채널이 제대로 등록되었는지 확인
        System.out.println("등록된 사용자: " + userService.read(user.getId()));
        System.out.println("등록된 채널: " + channelService.read(channel.getId()));

        // 메시지 생성

        try {
            Message message = new Message("안녕하세요, 장건희입니다!", user.getId(), channel.getId());
            messageService.create(message);
            System.out.println("생성된 메시지: " + messageService.read(message.getId()));
        } catch (IllegalArgumentException e) {
            System.err.println("메시지 생성 실패: " + e.getMessage());
        }
        // 잘못된 메시지 생성 테스트
        // 잘못된 데이터를 검증 임의의 UUID 생성
        // 예외 발생 -> 에러메세지 출력? (try~catch)
        try {
            Message invalidMessage = new Message("유효하지 않은 메시지", UUID.randomUUID(), UUID.randomUUID());
            messageService.create(invalidMessage);
        } catch (IllegalArgumentException e) {
            System.err.println("잘못된 메시지 생성 실패: " + e.getMessage());
            System.err.println("테스트 완료: 유효하지 않은 사용자 또는 채널은 메세지를 생성할 수 없습니다. ");
        }

        // 메시지 수정 및 삭제
        try {
            Message message = new Message("안녕하세요, 장건희입니다!", user.getId(), channel.getId());
            messageService.create(message);

            // 수정
            message.updateContent("안녕하세요, 업데이트된 장건희입니다!");
            messageService.update(message.getId(), message);
            System.out.println("수정된 메시지: " + messageService.read(message.getId()));

            // 삭제
            messageService.delete(message.getId());
            System.out.println("삭제된 메시지 확인: " + messageService.read(message.getId()));
        } catch (IllegalArgumentException e) {
            System.err.println("메시지 작업 중 오류 발생: " + e.getMessage());
        }
    }
}
