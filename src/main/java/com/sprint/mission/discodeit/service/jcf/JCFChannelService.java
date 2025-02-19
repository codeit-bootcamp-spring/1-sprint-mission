//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.UserService;
//import com.sprint.mission.discodeit.view.ConsoleView;
//
//import java.util.*;
//
//public class JCFChannelService implements ChannelService {
//
//    private final List<Channel> channels = new ArrayList<>();
//    private final UserService userService;  // UserService 추가
//    private final ConsoleView consoleView;
//
//    public JCFChannelService(UserService userService, ConsoleView consoleView) {
//        this.userService = userService;
//        this.consoleView = consoleView;
//    }
//
//    @Override
//    public Channel createChannel(String name, String description) {
//        Channel channel = new Channel(name, description);
//        channels.add(channel);
//        consoleView.displaySuccess("채널 생성됨: " + channel.getChannelName());
//        return channel;
//    }
//
//    @Override
//    public Channel getChannelById(UUID channelId) {
//        for (Channel channel : channels) {
//            if (channel.getId().equals(channelId)) {
//                consoleView.displayChannel(channel, null);
//                return channel;
//            }
//        }
//        consoleView.displayError("채널을 찾을 수 없습니다");
//        return null;
//    }
//
//    @Override
//    public List<Channel> getAllChannels() {
//        consoleView.displayChannels(channels);
//        return new ArrayList<>(channels);
//    }
//
//    @Override
//    public Channel updateChannelName(UUID channelId, String newName) {
//        for (Channel channel : channels) {
//            if (channel.getId().equals(channelId)) {
//                channel.updateChannelName(newName);
//                consoleView.displaySuccess("채널 이름이 업데이트되었습니다: " + newName);
//                return channel;
//            }
//        }
//        consoleView.displayError("채널을 찾을 수 없습니다");
//        return null;
//    }
//
//    @Override
//    public Channel updateDescription(UUID channelId, String newDescription) {
//        for (Channel channel : channels) {
//            if (channel.getId().equals(channelId)) {
//                channel.updateDescription(newDescription);
//                consoleView.displaySuccess("채널 설명이 업데이트되었습니다: " + newDescription);
//                return channel;
//            }
//        }
//        consoleView.displayError("채널을 찾을 수 없습니다");
//        return null;
//    }
//
//
//    @Override
//    public boolean deleteChannel(UUID channelId) {
//        for (int i = 0; i < channels.size(); i++) {
//            if (channels.get(i).getId().equals(channelId)) {
//                channels.remove(i);
//                consoleView.displaySuccess("채널이 삭제되었습니다");
//                return true;
//            }
//        }
//        consoleView.displayError("채널 삭제에 실패했습니다");
//        return false;
//    }
//
//}
