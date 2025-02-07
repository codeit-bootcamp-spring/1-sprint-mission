package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

	static User setupUser(UserService userService){
		User user = userService.createUser
				("코드일","codeit1234@codeit.com","codeit1234","1Q2w3e4r@");
		return user;
	}

	static User setupUser2(UserService userService){
		User user = userService.createUser
				("코드이","code1234@codeit.com","codeit1234","1q2W3e4r@");
		return user;
	}


	static Channel setupChannel(ChannelService channelService){
		Channel ch = channelService.createPublicChannel("CH.1","테스트 채널 1");
		return ch;
	}

	static Channel setupChannel2(ChannelService channelService){
		Channel ch = channelService.createPrivateChannel("CH.2", "테스트 채널 2");
		return ch;
	}

	static void allPrintUser(List<User> data){
		for(User user  : data){
			System.out.println(user);
			System.out.println("-----------------------------------------------");
		}
	}
	static void allPrintChannel(List<Channel> data){
		for(Channel channel : data){
			System.out.println(channel);
			System.out.println("-----------------------------------------------");
		}
	}

	static void allprintMessages( List<Message> data){
		for (Message message: data) {
			System.out.println(message);
			System.out.println("===============================================");
			}
		}

    static void messageCreateTest(MessageService messageService,UUID channelId, UUID authorId, String content){
        Message message = messageService.createMsg(content,channelId,authorId);
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(BasicUserService.class);
        ChannelService channelService = context.getBean(BasicChannelService.class);
        MessageService messageService = context.getBean(BasicMessageService.class);



        User user1 = DiscodeitApplication.setupUser(userService);
        User user2 = DiscodeitApplication.setupUser2(userService);
        DiscodeitApplication.allPrintUser(userService.findAll());

        Channel ch1 = DiscodeitApplication.setupChannel(channelService);
        Channel ch2 = DiscodeitApplication.setupChannel2(channelService);
        DiscodeitApplication.allPrintChannel(channelService.findAll());

		messageCreateTest(messageService,ch1.getId(),user1.getId(),"user1이 ch1으로 보내는 메시지");
		messageCreateTest(messageService,ch1.getId(),user2.getId(),"user2이 ch1으로 보내는 메시지");
		messageCreateTest(messageService,ch2.getId(),user2.getId(),"user1이 ch2으로 보내는 메시지");
		messageCreateTest(messageService,ch2.getId(),user2.getId(),"user2이 ch2으로 보내는 메시지");
        DiscodeitApplication.allprintMessages(messageService.findAll());


//        System.out.println("----------ch1 채널 삭제후 모든 채널 조회-----------");
//        channelService.delete(ch1.getId());
//        System.out.println("----------ch1 채널 삭제후 메시지 조회----------");
//        DiscodeitApplication.allPrintChannel(channelService.findAll());
//        DiscodeitApplication.allprintMessages(messageService.findAll());
    }
}
