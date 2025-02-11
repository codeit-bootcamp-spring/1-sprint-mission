package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFBinaryContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {



		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
		UserService userService = context.getBean("BasicUserService", UserService.class);
		MessageService messageService=context.getBean("BasicMessageService",MessageService.class);
		ChannelService channelService=context.getBean("BasicChannelService",ChannelService.class);

		//System.out.println(userService.sendMessageToUser("감자전","고구마","감자튀김"));
		//messageService.createNewMessage("ff","afdas");

		char[] a={'c','g','d'};
		char[] b={'f','q','g'};

		List<char[]> ab=new ArrayList<>();
		ab.add(a);
		ab.add(b);
		userService.updateUserSelfImg("감자전",a);
		//userService.createNewUser("감자전","지짐그","이메일");
		//userService.createNewUser("고구마","전","이메일");
		//System.out.println(userService.readUserAll());
		//System.out.println(userService.readUser("감자전"));
		//userService.deleteUser("감자전","지짐그그");
		//userService.deleteUser("감자전","지짐그");
		//System.out.println(userService.readUser("감자전"));
		//String rawUuid = "7e81196f-1e57-44f0-826b-d6054e69110e";  // 앞뒤 공백 포함
		//UUID id = UUID.fromString(rawUuid.replaceAll("\"", ""));  // 공백 제거 후 변환

		//channelService.createPrivateChannel();
		channelService.deleteUserToChannel("탄수화물 패밀리","고구마");
		//channelService.createNewChannel("탄수화물 패밀리");
		//channelService.addUserToChannel("탄수화물 패밀리","감자전");
//		channelService.addUserToChannel("탄수화물 패밀리","고구마");
//		System.out.println(channelService.readChannelInUser("탄수화물 패밀리"));
//		System.out.println(channelService.readChannel("탄수화물 패밀리"));
//		System.out.println(channelService.readChannelAll());
//		channelService.deleteChannel("탄수화물 패밀리");
//		System.out.println(channelService.readChannelAll());



		messageService.createNewMessagetoImg("감자튀김","마요네즈 맛있음 아마",ab);
		System.out.println(userService.sendMessageToUser("감자전","고구마","감자튀김"));
		//System.out.println(messageService.readMessage("감자튀김"));
//		System.out.println(messageService.readMessageAll());
//		messageService.deleteMessage("감자튀김");
//		System.out.println(messageService.readMessage("감자튀김"));

	}

}
