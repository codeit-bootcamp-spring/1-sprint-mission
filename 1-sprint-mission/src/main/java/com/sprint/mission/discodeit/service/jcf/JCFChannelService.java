package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.jcf.interfacePac.JCFChannelServiceInterface;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class JCFChannelService implements JCFChannelServiceInterface {
    private JCFUserRepository userRepository;
    private JCFMessageRepository messageRepository;
    private JCFChannelRepository channelRepository;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");


    @Override
    public Channel saveChannel(String nickName, User user , boolean checkPrivate) {
        ChannelDto channelDto = new ChannelDto();
        channelDto.setChannelName(nickName);
        channelDto.setUserId(user.getId());
        channelDto.setUserName(user.getName());
        Channel channel = channelRepository.createChannel(channelDto);

        if(channelRepository.checkChannelContains(channel.getChannelId())){
            while(!channelRepository.checkChannelContains(channel.getChannelId())){
                channel.setChannelId(UUID.randomUUID());
            }
        }
        System.out.println("새 채널에 오신 것을 환영합니다!");

        return channel;
    }

    public void joinChannel(Channel channel, User user) {
        if (channelRepository.getChannelId(channel.getChannelId())) {
            ChannelDto channelDto = new ChannelDto();
            channelDto.setChannelName(channel.getName());
            channelDto.setUserId(user.getId());
            channelDto.setUserName(user.getName());

            channelRepository.joinChannel(channelDto);
            System.out.println("새 채널에 오신 것을 환영합니다!");
        }

        System.out.println("존재하지 않는 채널입니다");
    }

    @Override
    public void deleteChannel(Channel channel) {
        if (channelRepository.checkChannelContains(channel.getChannelId())) {
            System.out.println("존재하지 않는 채널입니다");
            return;
        }

        channelRepository.deleteChannel(channel.getChannelId());
        messageRepository.deletedChannelMessage(channel.getChannelId());
        System.out.println("채널 삭제에 성공하였습니다");
    }

    @Override
    public void modifyChannel(Channel channel, String newName) {
        if (channelRepository.getChannelId(channel.getChannelId())) {
            if(!channelRepository.modifyChannel(channel.getChannelId(), newName)) {
                System.out.println("이름을 변경할 수 없는 채널입니다");
                return;
            }
            messageRepository.modifyChannelName(channel.getChannelId(),newName);
            System.out.println("채널명을 수정하였습니다");
            return;
        }

        System.out.println("존재하지 않는 채널입니다");
    }

    @Override
    public void showInfoChannel(Channel channel) {
        System.out.println(channel.getName() + "(" + channel.getChannelId() + ")"
                + "\n생성 일자 : " + format.format(channel.getCreatedAt())
                + "\n최종 수정 일자 : " + format.format(channel.getUpdatedAt()));
        System.out.println("참여 중인 유저 "+ channelRepository.joinUserList(channel.getChannelId()));
    }

    public void findAllByUserId(ChannelDto channelDto){ // or uuid
        System.out.println((channelRepository.findAllByUserId(channelDto)));
    }
}
