package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.interfaces.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Service
public class BasicChannelService implements ChannelService {

    private final FileMessageRepository messageRepository;
    private final FileChannelRepository channelRepository;
    private final BasicReadStatusService readStatusService;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");

    @Autowired
    public BasicChannelService(FileMessageRepository messageRepository, FileChannelRepository channelRepository, BasicReadStatusService readStatusService) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.readStatusService = readStatusService;
    }

    @Override
    public void saveChannel(ChannelDto channelDto) throws IOException, ClassNotFoundException {
        channelRepository.createChannel(channelDto);
        channelDto.setChannelId(channelRepository.getChannel(channelDto).getChannelId());
        if(channelDto.getChannelPrivate()) readStatusService.create(channelDto);


        System.out.println("새 채널에 오신 것을 환영합니다!");
    }

    public void joinChannel(ChannelDto dto, User user) throws IOException, ClassNotFoundException {

        if(dto.getChannelPrivate()) readStatusService.addUser(dto);


        if (channelRepository.getChannelId(dto.getChannelId())) {

            dto.setUserName(user.getName());

            channelRepository.joinChannel(dto);
            System.out.println("새 채널에 오신 것을 환영합니다!");
        }

        System.out.println("존재하지 않는 채널입니다");
    }

    @Override
    public void deleteChannel(ChannelDto channel) throws IOException, ClassNotFoundException {
        if (!channelRepository.checkChannelContains(channel.getChannelId())) {
            System.out.println("존재하지 않는 채널입니다");
            return;
        }

        channelRepository.deleteChannel(channel.getChannelId());
        readStatusService.deleteStatus(channel);
        messageRepository.deletedChannelMessage(channel.getChannelId());
        System.out.println("채널 삭제에 성공하였습니다");
    }

    @Override
    public void modifyChannel(ChannelDto channel) throws IOException, ClassNotFoundException {
        if (channelRepository.getChannelId(channel.getChannelId())) {
            try{
                channelRepository.modifyChannel(channel.getChannelId(), channel.getNewChannelName());
                messageRepository.modifyChannelName(channel.getChannelId(), channel.getNewChannelName());
                System.out.println("채널명을 수정하였습니다");
                return;

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("이름을 변경할 수 없는 채널입니다");
                return;
            }
        }

        System.out.println("존재하지 않는 채널입니다");
    }

    @Override
    public void showInfoChannel(ChannelDto channelDto) throws IOException, ClassNotFoundException {
        Channel channel = channelRepository.getChannel(channelDto);

        if(channel == null) throw new IOException();

        System.out.println(channel.getName() + "(" + channel.getChannelId() + ")"
                + "\n생성 일자 : " + (channel.getCreatedAt())
                + "\n최종 수정 일자 : " + (channel.getUpdatedAt()));
        System.out.println("참여 중인 유저 "+ channelRepository.joinUserList(channel.getChannelId()));
    }

    public void findAllByUserId(UserDto user) throws IOException, ClassNotFoundException {
        ChannelDto channelDto = new ChannelDto();
        channelDto.setUserId(user.getId());
        List<Channel> list = channelRepository.findAllByUserId(channelDto);
        List<UUID> listPrivateChannelId = readStatusService.findAllByUserId(channelDto.getUserId());//uuid
        for(UUID uid : listPrivateChannelId){
            ChannelDto dto = new ChannelDto();
            dto.setChannelId(uid);

            list.add(channelRepository.getChannel(dto));
        }

        for(Channel channel : list){
            System.out.println(channel + "\n");
            if(channel.getCheckPrivateChannel()){
                System.out.println("최종 활성화 일자 : " + messageRepository.currentMessageOfChannel(channel.getChannelId()));
            }
        }
    }

    public void showInfoChannelOfChannel(ChannelDto channelDto) throws IOException, ClassNotFoundException {
        List<Channel> list = channelRepository.findAllByUserId(channelDto);

        List<UUID> listPrivateChannelId = readStatusService.findAllByUserId(channelDto.getUserId());//uuid

        for(UUID uid : listPrivateChannelId){
            ChannelDto dto = new ChannelDto();
            dto.setChannelId(uid);

            list.add(channelRepository.findChannel(dto));
        }

        for(Channel channel : list){
            System.out.println(channel);
            if(messageRepository.currentMessageOfChannel(channel.getChannelId()) == null){
                System.out.println("최종 활성화 일자 : "  + channel.getCreatedAt());
            }
            else{
                System.out.println("최종 활성화 일자 : " + messageRepository.currentMessageOfChannel(channel.getChannelId()));
            }
        }
    }
}
