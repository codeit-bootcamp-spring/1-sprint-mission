package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.Channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Channel.ChannelDto;
import com.sprint.mission.discodeit.dto.Channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.Message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Message.MessageDto;
import com.sprint.mission.discodeit.dto.Message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
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
		User createdUser = userService.create(userCreateRequest);
		System.out.println("유저 생성: " + createdUser.getId());

		// 유저 조회 (단건)
		UserDto foundUser = userService.findByUserId(createdUser.getId());
		System.out.println("유저 조회(단건): " + foundUser);

		// 유저 조회 (다건)
		List<UserDto> foundUsers = userService.findAll();
		System.out.println("유저 조회(다건): " + foundUsers.size());

		// 유저 수정 요청 DTO
		UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
				createdUser.getId(),
				"홍길동",
				"example@gmail.com",
				"01056785678",
				"asdf12#$",
				null // 프로필 이미지
		);

		// 유저 수정
		User updatedUser = userService.update(userUpdateRequest);
		System.out.println("유저 수정: 이름: " + updatedUser.getUsername() + ", 이메일: " + updatedUser.getEmail() + ", 번호: " + updatedUser.getPhoneNumber());

		// 유저 삭제
		userService.delete(createdUser.getId());
		List<UserDto> foundUsersAfterDelete = userService.findAll();
		System.out.println("유저 삭제(남은 유저): " + foundUsersAfterDelete.size());
	}

//	static void channelCRUDTest(ChannelService channelService) {
//		// 채널 생성 요청 DTO
//		ChannelCreateRequest channelCreateRequest = new ChannelCreateRequest(
//				"코드잇",
//				"코드잇 스프린트",
//				ChannelType.PUBLIC,
//				List.of(UUID.randomUUID()) // 예시로 하나의 참가자 ID
//		);
//
//		// 채널 생성
//		Channel createdChannel = channelService.create(channelCreateRequest);
//		System.out.println("채널 생성: " + createdChannel.getId());
//
//		// 채널 조회 (단건)
//		ChannelDto foundChannel = channelService.findById(createdChannel.getId());
//		System.out.println("채널 조회(단건): " + foundChannel.id());
//
//		// 채널 조회 (다건)
//		List<ChannelDto> foundChannels = channelService.findAllByUserId(UUID.randomUUID()); // 예시로 랜덤 userId
//		System.out.println("채널 조회(다건): " + foundChannels.size());
//
//		// 채널 수정 요청 DTO
//		ChannelUpdateRequest channelUpdateRequest = new ChannelUpdateRequest(
//				createdChannel.getName(),
//				"스프링",
//				ChannelType.PRIVATE
//		);
//
//		// 채널 수정
//		ChannelDto updatedChannel = channelService.update(createdChannel.getId(), channelUpdateRequest);
//		System.out.println("채널 수정: 이름: " + updatedChannel.name() + ", 설명: " + updatedChannel.description() + ", 타입: " + updatedChannel.channelType());
//
//		// 채널 삭제
//		channelService.delete(createdChannel.getId());
//		List<ChannelDto> foundChannelsAfterDelete = channelService.findAllByUserId(UUID.randomUUID());
//		System.out.println("채널 삭제: " + foundChannelsAfterDelete.size());
//	}

//	static void messageCRUDTest(MessageService messageService) {
//		// 사용자 생성 요청 DTO
//		UserCreateRequest userCreateRequest = new UserCreateRequest(
//				"이병규",
//				"buzzin2426@gmail.com",
//				"01012341234",
//				"qwer!@34",
//				null // 프로필 이미지가 없을 경우
//		);
//		User createdUser = userService.create(userCreateRequest);
//		// 메시지 생성 요청 DTO
//		MessageCreateRequest messageCreateRequest = new MessageCreateRequest(
//				createdUser.getId(),
//				"안녕하세요.",
//				List.of(UUID.randomUUID())
//		);
//
//		// 메시지 생성
//		MessageDto createdMessage = messageService.create(messageCreateRequest, UUID.randomUUID());
//		System.out.println("메시지 생성: " + createdMessage.id());
//
//		// 메시지 조회 (단건)
//		MessageDto foundMessage = messageService.findAllByChannelId(createdMessage.id());
//		System.out.println("메시지 조회(단건): " + foundMessage.id());
//
//		// 메시지 조회 (다건)
//		List<MessageDto> foundMessages = messageService.findAllByChannelId(UUID.randomUUID());
//		System.out.println("메시지 조회(다건): " + foundMessages.size());
//
//		// 메시지 수정 요청 DTO
//		MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest(
//				createdMessage.id(),
//				"반갑습니다.",
//				List.of(UUID.randomUUID()) // 예시로 하나의 첨부 파일 ID
//		);
//
//		// 메시지 수정
//		MessageDto updatedMessage = messageService.update(messageUpdateRequest);
//		System.out.println("메시지 수정: " + updatedMessage.content());
//
//		// 메시지 삭제
//		messageService.delete(createdMessage.id());
//		List<MessageDto> foundMessagesAfterDelete = messageService.findAllByChannelId(UUID.randomUUID());
//		System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size());
//	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

//		// CRUD 테스트 수행
		userCRUDTest(userService);
//		System.out.println();
//		channelCRUDTest(channelService);
//		System.out.println();
//		messageCRUDTest(messageService);
	}
}


