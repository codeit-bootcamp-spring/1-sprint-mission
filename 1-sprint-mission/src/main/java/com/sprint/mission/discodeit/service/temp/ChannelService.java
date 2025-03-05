//package com.sprint.mission.discodeit.service.currentUse;
//
//import com.sprint.mission.discodeit.dto.ChannelDto;
//import com.sprint.mission.discodeit.dto.UserDto;
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
//import com.sprint.mission.discodeit.service.interfaces.ChannelServiceInterface;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.List;
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@Service
//public class ChannelService implements ChannelServiceInterface {
//    private FileUserRepository userRepository;
//    private FileMessageRepository messageRepository;
//    private FileChannelRepository channelRepository;
//    private ReadStatusService readStatusService;
//    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");
//
//
//    @Override
//    public void saveChannel(ChannelDto channelDto, UserDto user ) throws IOException, ClassNotFoundException {
//        channelRepository.createChannel(channelDto.getChannelName(),userRepository.find(user));
//        if(channelDto.isChannelPrivate()) readStatusService.create(channelDto);
//
//
//        System.out.println("새 채널에 오신 것을 환영합니다!");
//    }
//
//    public void joinChannel(ChannelDto dto, User user) throws IOException, ClassNotFoundException {
//
//        if(dto.isChannelPrivate()) readStatusService.addUser(dto);
//
//
//        if (channelRepository.getChannelId(dto.getChannelId())) {
//
//            dto.setUserName(user.getName());
//
//            channelRepository.joinChannel(dto);
//            System.out.println("새 채널에 오신 것을 환영합니다!");
//        }
//
//        System.out.println("존재하지 않는 채널입니다");
//    }
//
//    @Override
//    public void deleteChannel(ChannelDto channel) throws IOException, ClassNotFoundException {
//        if (channelRepository.checkChannelContains(channel.getChannelId())) {
//            System.out.println("존재하지 않는 채널입니다");
//            return;
//        }
//
//        channelRepository.deleteChannel(channel.getChannelId());
//        readStatusService.deleteStatus(channel.getChannelId());
//        messageRepository.deletedChannelMessage(channel.getChannelId());
//        System.out.println("채널 삭제에 성공하였습니다");
//    }
//
//    @Override
//    public void modifyChannel(ChannelDto channel) throws IOException, ClassNotFoundException {
//        if (channelRepository.getChannelId(channel.getChannelId())) {
//            try{
//                channelRepository.modifyChannel(channel.getChannelId(), channel.getNewChannelName());
//                messageRepository.modifyChannelName(channel.getChannelId(), channel.getNewChannelName());
//                System.out.println("채널명을 수정하였습니다");
//                return;
//
//            } catch (IOException | ClassNotFoundException e) {
//                System.out.println("이름을 변경할 수 없는 채널입니다");
//                return;
//            }
//        }
//
//        System.out.println("존재하지 않는 채널입니다");
//    }
//
//    @Override
//    public void showInfoChannel(ChannelDto channelDto) throws IOException, ClassNotFoundException {
//        Channel channel = channelRepository.findChannel(channelDto);
//        System.out.println(channel.getName() + "(" + channel.getChannelId() + ")"
//                + "\n생성 일자 : " + format.format(channel.getCreatedAt())
//                + "\n최종 수정 일자 : " + format.format(channel.getUpdatedAt()));
//        System.out.println("참여 중인 유저 "+ channelRepository.joinUserList(channel.getChannelId()));
//    }
//
//    public void findAllByUserId(UserDto user) throws IOException, ClassNotFoundException {
//        ChannelDto channelDto = new ChannelDto();
//        channelDto.setUserId(user.getId());
//        List<Channel> list = channelRepository.findAllByUserId(channelDto);
//        List<UUID> listPrivateChannelId = readStatusService.findAllByUserId(channelDto);//uuid
//        for(UUID uid : listPrivateChannelId){
//            ChannelDto dto = new ChannelDto();
//            dto.setChannelId(uid);
//
//            list.add(channelRepository.findChannel(dto));
//        }
//
//        for(Channel channel : list){
//            System.out.println(channel + "\n");
//            System.out.println("최종 활성화 일자 : " + messageRepository.currentMessageOfChannel(channel.getChannelId()));
//        }
//    }
//}
