package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
	/*
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
		System.out.println(userService.update(new UserUpdateRequestDto(userResponseDto.id(), "uuser", "uuser@codeit.kr", "", null),
				new BinaryContentRequestDto(null)));
		// 삭제
		userService.delete(userResponseDto.id());
		List<UserResponseDto> foundUsersAfterDelete = userService.findAll();
		System.out.println("유저 삭제: " + foundUsersAfterDelete.size() + System.lineSeparator());
	}

	static void publicChannelCRUDTest(ChannelService channelService) {
		System.out.println("PUBLIC 채널");

		// 생성
		ChannelResponseDto channelResponseDto = channelService.create(new ChannelCreateRequestDto(ChannelType.PUBLIC, "channel", "description", null));
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

	static void messageCRUDTest(MessageService messageService, ChannelService channelService, UserResponseDto userResponseDto, ChannelResponseDto channelResponseDto) {
		// 생성
		MessageResponseDto messageResponseDto1 = messageService.create(new MessageCreateRequestDto("안녕하세요",
				userResponseDto.id(),
				channelResponseDto.id(),
				new ArrayList<>(Collections.singleton(new BinaryContentRequestDto(null)))));
		MessageResponseDto messageResponseDto2 = messageService.create(new MessageCreateRequestDto("반갑습니다",
				userResponseDto.id(),
				channelResponseDto.id(),
				new ArrayList<>(Collections.singleton(new BinaryContentRequestDto(null)))));
		System.out.println("메시지 생성");
		System.out.println(messageResponseDto1);
		// 조회
		MessageResponseDto foundMessage = messageService.find(messageResponseDto1.id());
		System.out.println("메시지 조회(단건)");
		System.out.println(foundMessage);
		List<MessageResponseDto> foundMessages = messageService.findAllByChannelId(null);
		System.out.println("메시지 조회(다건): " + foundMessages.size());
		System.out.println(foundMessages);
		foundMessages = messageService.findAllByChannelId(channelResponseDto.id());
		System.out.println("메시지 조회(By ChannelId): " + foundMessages.size());
		System.out.println(foundMessages);
		// 수정
		MessageResponseDto updateMessage = messageService.update(new MessageUpdateRequestDto(messageResponseDto1.id(), "하이요"));
		System.out.println("메시지 수정");
		System.out.println(updateMessage);
		// 삭제
		messageService.delete(messageResponseDto1.id());
		List<MessageResponseDto> foundMessagesAfterDelete = messageService.findAllByChannelId(null);
		System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size());
		// 채널에서 최근 메시지 시간 조회
		System.out.println("채널 최근 메시지 시간 조회");
		System.out.println(channelService.find(channelResponseDto.id()));
	}

	static UserResponseDto setupUser(UserService userService) {
        return userService.create(new UserCreateRequestDto("user", "user@codeit.kr", "1234", null),
				new BinaryContentRequestDto(null));
	}

	static ChannelResponseDto setupPublicChannel(ChannelService channelService) {
		return channelService.create(new ChannelCreateRequestDto(ChannelType.PUBLIC, "channel", "description", null));
	}

	static ChannelResponseDto setupPrivateChannel(ChannelService channelService, UserResponseDto userResponseDto) {
		return channelService.create(new ChannelCreateRequestDto(ChannelType.PRIVATE, "channel", "description",
				new ArrayList<>(Collections.singleton(userResponseDto.id()))));
	}

	 */

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
/*
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
		messageCRUDTest(messageService, channelService, userResponseDto, publicChannelResponseDto);

		// 파일 지우기
//        userService.delete(user.getId());
//        channelService.delete(channel.getId());
 */
	}

}
