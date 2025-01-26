package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        // [A] JCF 기반
        System.out.println("=== [Basic*Service + JCF] ===");
        runCrudTest(
                new BasicUserService(new JCFUserRepository()),
                new BasicChannelService(new JCFChannelRepository()),
                new BasicMessageService(new JCFMessageRepository(),
                        new BasicUserService(new JCFUserRepository()),
                        new BasicChannelService(new JCFChannelRepository()))
        );

        // [B] File 기반
        System.out.println("\n=== [Basic*Service + File] ===");
        runCrudTest(
                new BasicUserService(new FileUserRepository()),
                new BasicChannelService(new FileChannelRepository()),
                new BasicMessageService(new FileMessageRepository(),
                        new BasicUserService(new FileUserRepository()),
                        new BasicChannelService(new FileChannelRepository()))
        );
    }

    /**
     * 1~6단계 CRUD 로직을 실행
     */
    private static void runCrudTest(UserService userService, ChannelService channelService, MessageService messageService) {
        // 1) 사용자 등록
        User user1 = userService.create(new User("이규석", "lks1230@sample.com"));
        User user2 = userService.create(new User("코드잇", "codeit@sample.com"));
        사용자목록("등록된 사용자", userService.findAll());

        // 2) 채널 등록
        Channel ch1 = channelService.create(new Channel("일반 채팅"));
        Channel ch2 = channelService.create(new Channel("기술 토론"));
        채널목록("등록된 채널", channelService.findAll());

        // 3) 메시지 생성
        Message msg1 = messageService.create(new Message("안녕하세요!", user1.getId(), ch1.getId()));
        Message msg2 = messageService.create(new Message("프로젝트 진행 상황은 어때요?", user2.getId(), ch1.getId()));
        메시지목록("등록된 메시지", messageService.findAll());

        // 4) 업데이트
        userService.update(
                user1.getId().toString(),
                new User("이규석(수정)", "lks_updated@sample.com")
        );
        channelService.update(
                ch2.getId().toString(),
                new Channel("기술 토론(수정)")
        );
        messageService.update(
                msg2.getId().toString(),
                new Message("거의 마무리되었습니다!", user2.getId(), ch1.getId())
        );

        사용자목록("수정 후 사용자", userService.findAll());
        채널목록("수정 후 채널", channelService.findAll());
        메시지목록("수정 후 메시지", messageService.findAll());

        // 5) 삭제
        userService.delete(user2.getId().toString());
        channelService.delete(ch1.getId().toString());
        messageService.delete(msg1.getId().toString());

        사용자목록("삭제 후 사용자", userService.findAll());
        채널목록("삭제 후 채널", channelService.findAll());
        메시지목록("삭제 후 메시지", messageService.findAll());
    }

    // ======================
    // 출력용 메서드들
    // ======================
    private static void 사용자목록(String title, List<User> list) {
        System.out.println("=== " + title + " ===");
        for (User user : list) {
            System.out.printf(" - ID: %s, 이름: %s, 이메일: %s%n",
                    user.getId(), user.getUsername(), user.getEmail()
            );
        }
        System.out.println();
    }

    private static void 채널목록(String title, List<Channel> list) {
        System.out.println("=== " + title + " ===");
        for (Channel ch : list) {
            System.out.printf(" - ID: %s, 채널명: %s%n",
                    ch.getId(), ch.getName()
            );
        }
        System.out.println();
    }

    private static void 메시지목록(String title, List<Message> list) {
        System.out.println("=== " + title + " ===");
        for (Message msg : list) {
            System.out.printf(" - 메시지 ID: %s, 내용: %s, 사용자: %s, 채널: %s%n",
                    msg.getId(), msg.getContent(), msg.getUserId(), msg.getChannelId()
            );
        }
        System.out.println();
    }
}
