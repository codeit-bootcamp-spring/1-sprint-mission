package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

	static void userCRUDTest(UserService userService) {
		// 사용자 생성 요청 DTO
		UserCreateRequest userCreateRequest = new UserCreateRequest(
				"이병규",
				"buzzin2426@gmail.com",
				"01012341234",
				"qwer!@34",
				null // 프로필 이미지가 없을 경우
		);

		// 유저 생성
		UserDto createdUser = userService.create(userCreateRequest);
		System.out.println("유저 생성: " + createdUser.userId());

		// 유저 조회 (단건)
		UserDto foundUser = userService.findByUserId(createdUser.userId());
		System.out.println("유저 조회(단건): " + foundUser);

		// 유저 조회 (다건)
		List<UserDto> foundUsers = userService.findAll();
		System.out.println("유저 조회(다건): " + foundUsers.size());

		// 유저 수정 요청 DTO
		UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
				createdUser.userId(),
				"홍길동",
				"example@gmail.com",
				"01056785678",
				"asdf12#$",
				null // 프로필 이미지
		);

		// 유저 수정
		UserDto updatedUser = userService.update(userUpdateRequest);
		System.out.println("유저 수정: 이름: " + updatedUser.username() + ", 이메일: " + updatedUser.email() + ", 번호: " + updatedUser.phoneNumber());

		// 유저 삭제
		userService.delete(createdUser.userId());
		List<UserDto> foundUsersAfterDelete = userService.findAll();
		System.out.println("유저 삭제(남은 유저): " + foundUsersAfterDelete.size());
	}

	static void channelCRUDTest(ChannelService channelService, UserService userService) {
		// 사용자 생성 요청 DTO
		UserCreateRequest userCreateRequest = new UserCreateRequest(
				"이병규",
				"buzzin2426@gmail.com",
				"01012341234",
				"qwer!@34",
				null // 프로필 이미지가 없을 경우
		);

		// 유저 생성
		UserDto createdUser = userService.create(userCreateRequest);

		// 채널 생성 요청 DTO
		PrivateChannelCreateRequest privatechannelCreateRequest = new PrivateChannelCreateRequest(
				ChannelType.PRIVATE,
				List.of(createdUser.userId())
		);

		PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(
				"코드잇",
				"스프린트 미션 3",
				ChannelType.PUBLIC,
				List.of(createdUser.userId())
		);

		// 채널 생성
		ChannelDto createdPrivateChannel = channelService.createPrivateChannel(privatechannelCreateRequest);
		ChannelDto createdPublicChannel = channelService.createPublicChannel(publicChannelCreateRequest);
		System.out.println("채널 생성: " + createdPrivateChannel.channelId());
		System.out.println("채널 생성: " + createdPublicChannel.channelId());

		// 채널 조회 (단건)
		ChannelDto foundChannel = channelService.findById(createdPrivateChannel.channelId());
		System.out.println("채널 조회(단건): " + foundChannel);

		// 채널 조회 (다건)
		List<ChannelDto> foundChannels = channelService.findAllByUserId(createdUser.userId()); // 예시로 랜덤 userId
		System.out.println("채널 조회(다건): " + foundChannels);

		// 채널 수정 요청 DTO
		ChannelUpdateRequest channelUpdateRequest = new ChannelUpdateRequest(
				createdPublicChannel.channelId(),
				"스프링",
				"modify 스프린트 미션 3",
				ChannelType.PUBLIC,
				List.of(createdUser.userId())
		);

		// 채널 수정
		ChannelDto updatedChannel = channelService.update( channelUpdateRequest);
		System.out.println("채널 수정: 이름: " + updatedChannel.name() + ", 설명: " + updatedChannel.description() + ", 타입: " + updatedChannel.channelType());

		// 채널 삭제
		channelService.delete(createdPrivateChannel.channelId());
		channelService.delete(createdPublicChannel.channelId());
		// 유저 삭제
		userService.delete(createdUser.userId());

		// 남아있는 채널 조회
		List<ChannelDto> remainingChannels = channelService.findAllByUserId(createdUser.userId());
		System.out.println("남아있는 채널 목록: " + remainingChannels);
	}

	static void messageCRUDTest(MessageService messageService, UserService userService, ChannelService channelService) {

		// 사용자 생성 요청 DTO
		UserCreateRequest userCreateRequest = new UserCreateRequest(
				"이병규",
				"buzzin2426@gmail.com",
				"01012341234",
				"qwer!@34",
				null // 프로필 이미지가 없을 경우
		);

		// 유저 생성
		UserDto createdUser = userService.create(userCreateRequest);
		// 채널 생성
		PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(
				"코드잇",
				"스프린트 미션 3",
				ChannelType.PUBLIC,
				List.of(createdUser.userId())
		);

		// 채널 생성
		ChannelDto createdPublicChannel = channelService.createPublicChannel(publicChannelCreateRequest);
		// 메시지 생성 요청 DTO
		MessageCreateRequest messageCreateRequest = new MessageCreateRequest(
				createdUser.userId(),
				createdPublicChannel.channelId(),
				"안녕하세요.",
				List.of(
				new byte[]{1, 2, 3},
				new byte[]{4, 5, 6}
				)
		);

		// 메시지 생성
		MessageDto createdMessage = messageService.create(messageCreateRequest);
		System.out.println("메시지 생성: " + createdMessage.messageId());

		// 메시지 조회 (단건)
		List<MessageDto> foundMessage = messageService.findAllByChannelId(createdPublicChannel.channelId());
		System.out.println("메시지 조회(단건): " + foundMessage);

		// 메시지 조회 (다건)
		List<MessageDto> foundMessages = messageService.findAllByChannelId(createdPublicChannel.channelId());
		System.out.println("메시지 조회(다건): " + foundMessages.size());

		// 메시지 수정 요청 DTO
		MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest(
				createdMessage.messageId(),
				createdUser.userId(),
				createdPublicChannel.channelId(),
				"반갑습니다.",
				List.of(
						new byte[]{5, 5, 5},
						new byte[]{1}
				)
		);

		// 메시지 수정
		MessageDto updatedMessage = messageService.update(messageUpdateRequest);
		System.out.println("메시지 수정: " + updatedMessage);

		// 메시지 삭제
		messageService.delete(createdMessage.messageId());
		List<MessageDto> foundMessagesAfterDelete = messageService.findAllByChannelId(UUID.randomUUID());
		System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size());

		// 채널 삭제
		channelService.delete(createdPublicChannel.channelId());

		// 유저 삭제
		userService.delete(createdUser.userId());
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		// CRUD 테스트 수행
		userCRUDTest(userService);
		System.out.println();
		channelCRUDTest(channelService, userService);
		System.out.println();
		messageCRUDTest(messageService, userService, channelService);
	}
}


