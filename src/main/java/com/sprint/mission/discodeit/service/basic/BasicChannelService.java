package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService { //서비스 계층에서 꼭 모든 오류를 잡지 않아도 됨 -> 윗 계층에서 오류를 해결하자!
    private ChannelRepository channelRepository;
    private UserService userService;
    private MessageService messageService;

    @Autowired
    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }
    public void setService(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void createChannel(Channel channel) {
        Channel newChannel = new Channel(channel);
        channelRepository.save(channel);
    }
    @Override
    public Channel createChannel(String title, User owner) {
        if(! iscorrectTitle(title)){
            return  null;
        }
        Channel newChannel = new Channel(title, owner);
        channelRepository.save(newChannel);
        System.out.println(owner.getUserName() + "님께서 새로운 채널을 생성했습니다.");
        return newChannel;
    }

    @Override
    public List<Channel> getAllChannelList() {
        return channelRepository.findAll().values().stream().collect(Collectors.toList());
    }

    @Override
    public Channel searchById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);
        if(channel == null) {
            System.out.println("해당 채널이 없습니다.");
        }
        return channel;
    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        try {
            Channel channel = searchById(channelId);
            String channelName = channel.getChannelName();
            channel.setChannelName(newChannelName);
            channelRepository.save(channel);
            System.out.println(channelName + "의 채널 이름이" + newChannelName + "으로 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("해당 채널이 존재하지 않습니다.");
            e.printStackTrace();
        }

    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = searchById(channelId);
        if(channel == null) {
            System.out.println("해당 채널이 존재하지 않습니다.");
        } else  {
            channelRepository.delete(channelId);
            System.out.println("해당 채널이 삭제되었습니다.");
        }

    }

    private boolean iscorrectTitle(String channelName) {
        if (channelName.isBlank()) {
            System.out.println("제목을 입력해주세요");
            return false;
        }
        return true;
    }
}