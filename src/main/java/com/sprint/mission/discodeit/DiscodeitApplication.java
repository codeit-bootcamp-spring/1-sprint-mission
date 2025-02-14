package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserInfoDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BinaryContentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = (UserService) context.getBean("basicUserService");
		ChannelService channelService = (ChannelService) context.getBean("basicChannelService");
		MessageService messageService = (MessageService) context.getBean("basicMessageService");
		BinaryContentService binaryContentService = (BinaryContentService) context.getBean("binaryContentService");

		System.out.println("===== userServiceTest ====");
		System.out.println();

		System.out.println("===== user 생성 =====");
		User user1 = userService.createUser(UserDto.of("test1", "test1", "허원재"));
		User user2 = userService.createUser(UserDto.of("test2", "test2", "이규석"));
		User user3 = userService.createUser(UserDto.of("test3", "test3", "임수빈"));

		System.out.println("===== 등록된 user =====");
		userService.readAll().stream().forEach(System.out::println);
		System.out.println();

		System.out.println("===== user1의 정보 =====");
		System.out.println(userService.readUser(user1.getId()));
		System.out.println();

		System.out.println("===== user1의 loginId와 password를 변경 =====");
		userService.updateUser(user1.getId(), UserDto.of("asdf", "qwer", "허원재"));
		UserInfoDto userInfoDto = userService.readUser(user1.getId());
		System.out.println(user1);
		System.out.println();

		System.out.println("===== user2 삭제 =====");
		userService.deleteUser(user2.getId());
		System.out.println("===== 등록된 user =====");
		userService.readAll().stream().forEach(System.out::println);
		System.out.println();




		System.out.println("===== channelServiceTest =====\n");

		System.out.println("===== channel 생성 =====");
		Channel codeit = channelService.createPublicChannel(ChannelDto.of("codeit", "codeit channel"));
		Channel spring = channelService.createPrivateChannel(user1.getId());

		System.out.println("===== 등록된 channel =====");
		channelService.readAllByUserId(user1.getId()).stream().forEach(System.out::println);
		System.out.println();

		System.out.println("===== codeit channel 이름 변경 =====");
		channelService.updateChannel(codeit.getId(), ChannelDto.of("newCodeit", "codeit channel"));
		System.out.println(channelService.readChannel(codeit.getId()));
		System.out.println();

		System.out.println("===== spring channel에 user 추가 =====");
		channelService.addUser(spring.getId(), user3.getId());
		channelService.addUser(spring.getId(), user1.getId());
		System.out.println(channelService.readChannel(spring.getId()));
		System.out.println();

		System.out.println("===== spring channel의 user 삭제 =====");
		channelService.deleteUser(spring.getId(), user3.getId());
		System.out.println(channelService.readChannel(spring.getId()));
		System.out.println();

		System.out.println("===== codeit channel 삭제 =====");
		channelService.deleteChannel(codeit.getId());
		System.out.println("===== 등록된 channel 리스트 =====");
		channelService.readAllByUserId(user1.getId()).stream().forEach(System.out::println);
		System.out.println();



		System.out.println("===== messageServiceTest =====\n");

		System.out.println("===== message 생성 =====");
		Message message1 = messageService.createMessage(MessageDto.of(user1, "hi", spring));
		Message message2 = messageService.createMessage(MessageDto.of(user1, "world", spring));
		System.out.println("===== 등록된 message =====");
		messageService.readAllByChannelId(spring.getId()).stream().forEach(System.out::println);
		System.out.println();

		System.out.println("===== 등록되지 않은 user거나 보내는 channel에 등록되지 않은 유저면 exception 발생 =====");
		System.out.println("===== createMessage(new MessageDto(user2, \"hello\", spring)) =====");
		try {
			Message message3 = messageService.createMessage(MessageDto.of(user2, "hello", spring));
		} catch (NotFoundException e) {
			System.out.println(e);
		}
		System.out.println();

		System.out.println("===== 메세지 내용 수정 =====");
		messageService.updateMessage(message1.getId(), "hello");
		System.out.println(messageService.readMessage(message1.getId()));
		System.out.println();

		System.out.println("===== 메세지 삭제 =====");
		messageService.deleteMessage(message2.getId());
		System.out.println("===== 등록된 message =====");
		messageService.readAllByChannelId(spring.getId()).stream().forEach(System.out::println);
		System.out.println();

		String fileName = "test.jpg";
		Path path = Path.of(System.getProperty("user.dir"), fileName);

		byte[] data = Files.readAllBytes(path);
		BinaryContent content = binaryContentService.create(BinaryContentDto.of(fileName, BelongType.MESSAGE, message1.getId(), data));

		BinaryContentDto contentDto = binaryContentService.find(content.getId());
		messageService.deleteMessage(message1.getId());
	}
}
