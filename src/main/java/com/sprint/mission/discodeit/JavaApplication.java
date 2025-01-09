package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.ChannelEntity;
import com.sprint.mission.discodeit.entity.MessageEntity;
import com.sprint.mission.discodeit.entity.UserEntity;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args){

        // 동작을 위한 서비스 선언
        MessageService messageService = new JCFMessageService();
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService(messageService);

        // 테스트를 위한 데이터 선언
        // (UUID가 랜덤값이기에 최초 선언한 유저의 UUID 이용하여 동작을 확인합니다.)
        UserEntity user = new UserEntity("Zelda", "zelda1234@gmail.com");
        ChannelEntity channel = new ChannelEntity("Hyrule's Channel", "This is Hyrule.");
        MessageEntity message = new MessageEntity(user.getId(), channel.getId(), "1st Message.");

        // User CRUD 검증
        // ( CREATE )
        userService.addUser(user);
        // ( READ Single )
        System.out.println("Added User: " + userService.getUserById(user.getId()).getUsername() + ", createdTime: " + userService.getUserById(user.getId()).getCreatedAt());
        // ( READ Multiple )

        // ( UPDATE )
        userService.updateUsername(user.getId(), "Link");
        System.out.println("Updated User: " + userService.getUserById(user.getId()).getUsername() + ", updatedTime: " + userService.getUserById(user.getId()).getUpdatedAt());
        // ( DELETE Flg )
        userService.deleteUserByUserIdWithFlg(user.getId());
        if(userService.getUserByIdWithFlg(user.getId()) == null){
            System.out.println("This user has been deleted. (DelFlg)");
        }
        // ( 유저 삭제 플래그가 1로 설정되어 삭제된 상태이지만, 실제로 데이터는 남아 있는 상태입니다. )
        if(userService.getUserById(user.getId()) != null){
            System.out.println("This user " + userService.getUserById(user.getId()).getUsername() + " has been deleted with DelFlg, But actually data is left.");
        }

        // Channel CRUD 검증
        // ( CREATE )
        channelService.addChannel(channel);
        // ( READ Single )
        System.out.println("Added Channel: " + channelService.getChannelById(channel.getId()).getChannelName() + ", createdTime: " + channelService.getChannelById(channel.getId()).getCreatedAt());
        // ( UPDATE )
        channelService.updateChannelName(channel.getId(), "Ganon's Kingdom");
        System.out.println("Updated Channel: " + channelService.getChannelById(channel.getId()).getChannelName() + ", createdTime: " + channelService.getChannelById(channel.getId()).getUpdatedAt());
        channelService.updateChannelDescription(channel.getId(), "The Hyrule is Ruined.");
        System.out.println("Updated Description: " + channelService.getChannelById(channel.getId()).getDescription() + ", createdTime: " + channelService.getChannelById(channel.getId()).getUpdatedAt());
        // ( DELETE Flg )
        channelService.deleteChannelWithFlg(channel.getId());
        if(channelService.getChannelByIdWithFlg(channel.getId()) == null){
            System.out.println("This channel has been deleted. (DelFlg)");
        }
        // ( 채널 삭제 플래그가 1로 설정되어 삭제된 상태이지만, 실제로 데이터는 남아 있는 상태입니다. )
        if(channelService.getChannelById(channel.getId()) != null){
            System.out.println("This channel " + channelService.getChannelById(channel.getId()).getChannelName() + " has been deleted with DelFlg, But actually data is left.");
        }

        // Message CRUD 검증
        // ( CREATE )
        messageService.addMessage(message);
        // ( READ Single )
        System.out.println("Added Message: " + messageService.getMessageById(message.getId()).getContent() + ", createdTime: " + messageService.getMessageById(message.getId()).getCreatedAt());
        // ( UPDATE )


    }
}
