//package com.sprint.mission.discodeit.jcf;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.service.ChannelService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class JCFChannelService implements ChannelService {
//    private final ChannelRepository channelRepository;
//
//    @Override
//    public Channel createChannel(User user, String channelName) {
//        Channel existingChannel = channelRepository.findByChannelName(channelName);
//        if (existingChannel == null) {
//            Channel newChannel = new Channel(user, channelName);
//            channelRepository.saveChannel(newChannel);
//            System.out.println("\n*** 채널 생성 성공 ***");
//            return newChannel;
//        } else {
//            System.out.println("**이미 존재하는 채널입니다. 기존 채널을 반환합니다.**");
//            return existingChannel;
//        }
//    }
//
//    @Override
//    public void updateChannel(User user, String channelName, String modifiedChannelName) {
//        Channel channel = channelRepository.findByChannelName(channelName);
//        if (channel != null && channel.getUser().equals(user)
//                && !channel.getChannelName().equals(modifiedChannelName)
//                && channelRepository.findByChannelName(modifiedChannelName) == null) {
//            channel.setChannelName(modifiedChannelName);
//            channelRepository.saveChannel(channel);
//            System.out.println("\n*** 채널명 변경 성공 ***");
//            System.out.println(channel);
//        } else {
//            throw new IllegalArgumentException("**채널을 찾을 수 없거나 중복된 이름입니다.**");
//        }
//    }
//
//    @Override
//    public List<Channel> findAllChannels() {
//        System.out.println("\n*** 채널 전체 조회 ***");
//        List<Channel> channels = channelRepository.findAllChannels();
//        channels.forEach(System.out::println);
//        return channels;
//    }
//
//    @Override
//    public void findByUser(User user) {
//        List<Channel> userChannels = channelRepository.findByUser(user);
//        if (userChannels.isEmpty()) {
//            System.out.println("**유저가 생성한 채널이 없습니다.**");
//        } else {
//            System.out.println("\n*** [채널 목록] ***");
//            userChannels.forEach(System.out::println);
//        }
//    }
//
//    @Override
//    public void deleteChannel(String channelName) {
//        Channel channelToDelete = channelRepository.findByChannelName(channelName);
//        if (channelToDelete != null) {
//            channelRepository.deleteChannel(channelToDelete);
//            System.out.println("\n*** 채널 삭제 성공 ***");
//        } else {
//            System.out.println("**채널을 찾을 수 없습니다.**");
//        }
//    }
//}