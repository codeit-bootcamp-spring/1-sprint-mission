package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.BinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
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
import com.sprint.mission.discodeit.validator.ChannelValidator;
import com.sprint.mission.discodeit.validator.MessageValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class DiscodeitApplication {
	static void userCRUDTest(UserService userService) {
		// 생성
		UserResponseDto userResponseDto = userService.create(new UserCreateRequestDto("user1", "user1@codeit.kr", "1234", null),
				new BinaryContentRequestDto(null));
		System.out.println("유저 생성");
		System.out.println(userResponseDto);
		// 조회
		System.out.println("유저 조회(단건)");
		System.out.println(userService.find(userResponseDto.id()));
		List<UserResponseDto> foundUsers = userService.findAll();
		System.out.println("유저 조회(다건): " + foundUsers.size());
		System.out.println(foundUsers);
		// 수정
		System.out.println("유저 수정");
		System.out.println(userService.update(new UserUpdateRequestDto(userResponseDto.id(), "uuser", "uuser@codeit.kr", "1234", null),
				new BinaryContentRequestDto(null)));
		// 삭제
		userService.delete(userResponseDto.id());
		List<UserResponseDto> foundUsersAfterDelete = userService.findAll();
		System.out.println("유저 삭제: " + foundUsersAfterDelete.size() + System.lineSeparator());
	}

	static void publicChannelCRUDTest(ChannelService channelService) {
		System.out.println("PUBLIC 채널");

		// 생성
		ChannelResponseDto channelResponseDto = channelService.create(new ChannelCreateRequestDto(ChannelType.PUBLIC, "channel", "introduction", null));
		System.out.println("채널 생성");
		System.out.println(channelResponseDto);
		// 조회
		ChannelResponseDto foundChannel = channelService.find(channelResponseDto.id());
		System.out.println("채널 조회(단건)");
		System.out.println(foundChannel);
		List<ChannelResponseDto> foundChannels = channelService.findAll();
		System.out.println("채널 조회(다건): " + foundChannels.size());
		System.out.println(foundChannels);
		// 수정
		System.out.println("채널 수정");
		System.out.println(channelService.update(new ChannelUpdateRequestDto(channelResponseDto.id(), "공지", "공지입니다")));
		// 삭제
		channelService.delete(channelResponseDto.id());
		List<ChannelResponseDto> foundChannelsAfterDelete = channelService.findAll();
		System.out.println("채널 삭제: " + foundChannelsAfterDelete.size() + System.lineSeparator());
	}

	static void privateChannelCRUDTest(UserService userService, ChannelService channelService) {
		System.out.println("PRIVATE 채널");
		UserResponseDto userResponseDto = userService.create(new UserCreateRequestDto("user2", "user2@codeit.kr", "1234", null),
				new BinaryContentRequestDto(null));

		// 생성
		ChannelResponseDto channelResponseDto = channelService.create(new ChannelCreateRequestDto(ChannelType.PRIVATE, null, null,
				new ArrayList<>(Collections.singleton(userResponseDto.id()))));
		System.out.println("채널 생성");
		System.out.println(channelResponseDto);
		// 조회
		ChannelResponseDto foundChannel = channelService.find(channelResponseDto.id());
		System.out.println("채널 조회(단건)");
		System.out.println(foundChannel);
		List<ChannelResponseDto> foundChannels = channelService.findAllByUserId(userResponseDto.id());
		System.out.println("채널 조회(By userID): " + foundChannels.size());
		System.out.println(foundChannels);
		// 삭제
		channelService.delete(channelResponseDto.id());
		List<ChannelResponseDto> foundChannelsAfterDelete = channelService.findAll();
		System.out.println("채널 삭제: " + foundChannelsAfterDelete.size() + System.lineSeparator());
	}

//	static void messageCRUDTest(MessageService messageService, Channel channel, User author) {
//		// 생성
//		Message message = messageService.create("안녕하세요.", author, channel.getId());
//		Message message1 = messageService.create("안녕하세요.", author, channel.getId());
//		System.out.println("메시지 생성");
//		System.out.println(messageService.getInfo(message.getId()) + System.lineSeparator());
//		// 조회
//		Message foundMessage = messageService.find(message.getId());
//		System.out.println("메시지 조회(단건)");
//		System.out.println(messageService.getInfo(foundMessage.getId()) + System.lineSeparator());
//		List<Message> foundMessages = messageService.findAll();
//		System.out.println("메시지 조회(다건): " + foundMessages.size() + System.lineSeparator());
//		// 수정
//		messageService.update(message.getId(), "반갑습니다.");
//		System.out.println("메시지 수정");
//		System.out.println(messageService.getInfo(message.getId()) + System.lineSeparator());
//		// 삭제
//		messageService.delete(message.getId());
//		List<Message> foundMessagesAfterDelete = messageService.findAll();
//		System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size() + System.lineSeparator());
//	}
//
	static UserResponseDto setupUser(UserService userService) {
        return userService.create(new UserCreateRequestDto("user", "user@codeit.kr", "1234", null),
				new BinaryContentRequestDto(null));
	}

	static ChannelResponseDto setupPublicChannel(ChannelService channelService) {
		return channelService.create(new ChannelCreateRequestDto(ChannelType.PUBLIC, "channel", "introduction", null));
	}

	static ChannelResponseDto setupPrivateChannel(ChannelService channelService, UserResponseDto userResponseDto) {
		return channelService.create(new ChannelCreateRequestDto(ChannelType.PRIVATE, "channel", "introduction",
				new ArrayList<>(Collections.singleton(userResponseDto.id()))));
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		// 서비스 초기화
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

//		// 셋업
		UserResponseDto userResponseDto = setupUser(userService);
		ChannelResponseDto publicChannelResponseDto = setupPublicChannel(channelService);
		ChannelResponseDto privateChannelResponseDto = setupPrivateChannel(channelService, userResponseDto);
//
//		// 테스트
		userCRUDTest(userService);
		publicChannelCRUDTest(channelService);
		privateChannelCRUDTest(userService, channelService);
//		messageCRUDTest(messageService, channel, user);

		// 파일 지우기
//        userService.delete(user.getId());
//        channelService.delete(channel.getId());
	}

}
