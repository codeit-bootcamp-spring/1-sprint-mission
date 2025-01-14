package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.jcf.JCFEntityService;

public class JavaApplication {
    public static void main(String[] args) {
        // 싱글톤 서비스 인스턴스 생성
        JCFEntityService<User> userService = JCFEntityService.getInstance(User.class);
        JCFEntityService<Channel> channelService = JCFEntityService.getInstance(Channel.class);
        JCFEntityService<Message> messageService = JCFEntityService.getInstance(Message.class);

        // 사용자 생성
        User user1 = new User("Alice");
        userService.create(user1);

        // 채널 생성
        Channel channel1 = new Channel("General");
        channelService.create(channel1);

        // 메시지 생성
        Message message1 = new Message("Hello World!", user1, channel1);
        messageService.create(message1);

        // 엔티티 조회 및 출력
        userService.read(user1.getId()).ifPresent(u -> System.out.println("User: " + u.getName()));
        channelService.read(channel1.getId()).ifPresent(c -> System.out.println("Channel: " + c.getTitle()));
        messageService.read(message1.getId()).ifPresent(m -> System.out.println("Message: " + m.getContent()));

        // 엔티티 업데이트
        user1.setName("Bob");
        userService.update(user1.getId(), user1);

        // 업데이트 확인
        userService.read(user1.getId()).ifPresent(u -> System.out.println("Updated User: " + u.getName()));

        // 엔티티 삭제 및 확인
        messageService.delete(message1.getId());
        System.out.println("Message exists after deletion: " + messageService.read(message1.getId()).isPresent());

        // Stream API를 사용하여 조건에 맞는 사용자 필터링
        userService.findByCondition(u -> u.getName().startsWith("B"))
                .forEach(u -> System.out.println("Filtered User: " + u.getName()));
    }
}
