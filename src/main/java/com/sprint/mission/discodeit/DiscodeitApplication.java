package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@SpringBootApplication
public class DiscodeitApplication {

	//JCF 기반 Repository 설정
	static UserRepository userRepository=new JCFUserRepository();

	static ChannelRepository channelRepository = new JCFChannelRepository();
	static MessageRepository messageRepository = new JCFMessageRepository();

	static User setupUser(UserService userService) {
		User user = userService.createUser(new UserCreateDTO("홍길동", "1234", "dis@code.it", "filePath_gildong"));
		User user2 = userService.createUser(new UserCreateDTO("김이박", "5678", "dis2@code.it", "filePath_kim"));

		return user;
	}

//	static Channel setupChannel(ChannelService channelService) {
//		Channel channel = channelService.createPublicChannel(new ChannelCreateDTO("공개채널", new ArrayList<>(userRepository.load().values())), ChannelType.PUBLIC);
//		return channel;
//	}

//	static void messageCreateTest(MessageService messageService, Channel channel, User author) {
//		Message message = messageService.createMessage(author.getId(), channel.getId(), "안녕하세요");
//		System.out.println("메시지 생성: " + message.getId());
//	}


	public static void main(String[] args) {
		//Spring의 애플리케이션 컨텍스트 인터페이스로, 빈의 생성과 관계 설정을 담당한다.
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
		
		//서비스 초기화, context에서 각 서비스 빈을 가져온다.
		UserService userService=context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);
		BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
		UserStatusService userStatusService = context.getBean(UserStatusService.class);
		ReadStatusService readStatusService = context.getBean(ReadStatusService.class);


		//셋업
		User user = setupUser(userService);

//		System.out.println(userService.findUserDTO(user.getId()));
//		System.out.println(userService.isNameExist(user.getUserName()));
//
//		List<UUID> userList = new ArrayList<>();
//		userList.add(user.getId());
//
//		//Channel channel = setupChannel(channelService);
//		Channel channel = channelService.createPublicChannel(new ChannelCreateDTO("공개채널",userList), ChannelType.PUBLIC);
//		System.out.println(channelService.readChannel(channel.getId()) + " " + channel.getChannelName());
//
//		System.out.println(channelService.findDTO(channel.getId()) + " " + channel.getChannelName());
//
//		channelService.deleteChannel(channel.getId());
//		System.out.println(channelService.readAllChannel());


	}

}
