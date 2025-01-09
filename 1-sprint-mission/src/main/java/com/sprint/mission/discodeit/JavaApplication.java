package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
        // 서비스 초기화
        UserService 사용자서비스 = new JCFUserService();
        ChannelService 채널서비스 = new JCFChannelService();
        MessageService 메시지서비스 = new JCFMessageService();

        // STEP 1: 사용자 등록 및 조회
        System.out.println("=== STEP 1: 사용자 등록 ===");
        User 사용자1 = 사용자서비스.create(new User("이규석", "lks1230"));
        User 사용자2 = 사용자서비스.create(new User("석규이", "skl1230"));
        사용자목록출력("등록된 사용자", 사용자서비스.findAll());

        // STEP 2: 채널 등록 및 조회
        System.out.println("\n=== STEP 2: 채널 등록 ===");
        Channel 채널1 = 채널서비스.create(new Channel("일반 채팅"));
        Channel 채널2 = 채널서비스.create(new Channel("기술 토론"));
        채널목록출력("등록된 채널", 채널서비스.findAll());

        // STEP 3: 메시지 등록
        System.out.println("\n=== STEP 3: 메시지 등록 ===");
        Message 메시지1 = 메시지서비스.create(new Message("안녕하세요!", 사용자1.getId(), 채널1.getId()));
        Message 메시지2 = 메시지서비스.create(new Message("프로젝트 진행 상황은 어떠신가요?", 사용자2.getId(), 채널1.getId()));
        메시지목록출력("등록된 메시지", 메시지서비스.findAll());

        // STEP 4: 데이터 수정
        System.out.println("\n=== STEP 4: 데이터 수정 ===");
        사용자서비스.update(사용자1.getId().toString(), new User("이규석(수정됨)", "newemail"));
        채널서비스.update(채널2.getId().toString(), new Channel("기술 토론(수정됨)"));
        메시지서비스.update(메시지2.getId().toString(), new Message("프로젝트 거의 완료되었습니다!", 사용자2.getId(), 채널1.getId()));
        사용자목록출력("수정된 사용자", 사용자서비스.findAll());
        채널목록출력("수정된 채널", 채널서비스.findAll());
        메시지목록출력("수정된 메시지", 메시지서비스.findAll());

        // STEP 5: 데이터 삭제
        System.out.println("\n=== STEP 5: 데이터 삭제 ===");
        사용자서비스.delete(사용자2.getId().toString());
        채널서비스.delete(채널1.getId().toString());
        메시지서비스.delete(메시지1.getId().toString());
        사용자목록출력("남은 사용자", 사용자서비스.findAll());
        채널목록출력("남은 채널", 채널서비스.findAll());
        메시지목록출력("남은 메시지", 메시지서비스.findAll());
    }

    private static void 사용자목록출력(String 제목, List<User> 사용자목록) {
        System.out.println(제목 + ":");
        for (User 사용자 : 사용자목록) {
            System.out.printf(" - ID: %s, 닉네임: %s\n", 사용자.getId(), 사용자.getUsername());
        }
    }

    private static void 채널목록출력(String 제목, List<Channel> 채널목록) {
        System.out.println(제목 + ":");
        for (Channel 채널 : 채널목록) {
            System.out.printf(" - ID: %s, 채널명: %s\n", 채널.getId(), 채널.getName());
        }
    }

    private static void 메시지목록출력(String 제목, List<Message> 메시지목록) {
        System.out.println(제목 + ":");
        for (Message 메시지 : 메시지목록) {
            System.out.printf(" - 사용자: %s, 채널: %s, 내용: %s\n",
                    메시지.getUserId(), 메시지.getChannelId(), 메시지.getContent());
        }
    }
}
