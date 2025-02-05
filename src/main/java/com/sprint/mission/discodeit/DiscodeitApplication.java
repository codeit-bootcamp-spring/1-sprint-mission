package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.basic.ChannelService;
import com.sprint.mission.discodeit.service.basic.MessageService;
import com.sprint.mission.discodeit.service.basic.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
        static User setupUser(UserService userService) {
                User user = userService.create("woody", "woody@codeit.com", "woody1234");
                return user;
        }

        static Channel setupChannel(ChannelService channelService) {
                Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
                return channel;
        }

        static void messageCreateTest(MessageService messageService, Channel channel, User author) {
                Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
                System.out.println("메시지 생성: " + message.getId());
        }

        public static void main(String[] args) {
                //  Spring Context 시작
                ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

                //  Spring Context에서 Bean을 가져오기
                UserService userService = context.getBean(BasicUserService.class);
                ChannelService channelService = context.getBean(BasicChannelService.class);
                MessageService messageService = context.getBean(BasicMessageService.class);

                //  기존 JavaApplication의 setup, 테스트 코드 실행
                User user = setupUser(userService);
                Channel channel = setupChannel(channelService);
                messageCreateTest(messageService, channel, user);
        }
}
