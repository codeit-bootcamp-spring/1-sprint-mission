package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateDTO;
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

//	static User setupUser(UserService userService) {
//
//		return user;
//	}
//


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
		User user = userService.createUser(new UserCreateDTO("홍길동", "1234", "dis@code.it", "filePath_gildong"));
		User user2 = userService.createUser(new UserCreateDTO("김이박", "5678", "dis2@code.it", "filePath_kim"));


		List<String > privateUserList =	new ArrayList<>();

		privateUserList.add(user2.getId().toString());

		Channel PBChannel = channelService.createPublicChannel(new ChannelCreateDTO("공개 채널"));
		Channel PVChannel = channelService.createPrivateChannel(new PrivateChannelCreateDTO("비공개 채널", privateUserList));

		System.out.println(PBChannel.getId());
		System.out.println(PVChannel.getId());
		System.out.println(user.getId());
		System.out.println(user2.getId());


	}

}
