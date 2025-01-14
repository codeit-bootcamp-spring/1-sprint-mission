package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.UUID;

public class JCFChannelService implements ChannelService {
    //Scanner sc = new Scanner(System.in);
    // private final HashMap<String, Channel> Channels;
    private final ChannelRepository channelRepository;
    // mocking 이용으로 추가
    private InputHandler inputHandler;

    public JCFChannelService(ChannelRepository channelRepository, InputHandler inputHandler){
        // 생성자에서 Channels 데이터 초기화
        this.channelRepository = channelRepository;
        // mocking 이용으로 추가
        this.inputHandler = inputHandler;
    }

    // mocking 이용으로 추가
    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    // 의존성 주입을 위한 Channel 반환
//    public Channel getChannel(String channelName){
//        return Channels.get(channelName);
//    }

    public UUID createChannel(User user, String channelName){
        // 채널 생성
        Channel channel = new Channel(user, channelName);
        // 데이터 처리는 다른 레이어에서
        channelRepository.saveChannel(channel);
        return channel.getId();
    }

    public int showAllChannels(){
        //Channel ChannelName -  User NickName
        if (channelRepository.getAllChannels().isEmpty()) {
            System.out.println("No Channels exists.\n");
        }else{
            System.out.println(channelRepository.getAllChannels().toString());
        }
        return channelRepository.getAllChannels().size();
    }
    public Channel getChannelById(UUID id){
        // 특정 채널을 불러오기
        if(channelRepository.findChannelById(id) == null ){
            System.out.println("Channel does not exist\n");
            return null;
        }else{
            System.out.println(channelRepository.findChannelById(id).toString());
            return channelRepository.findChannelById(id);
        }
    }

    public void updateChannelName(UUID id){
        System.out.println("new ChannelName :");
        channelRepository.findChannelById(id).setChannelName(inputHandler.getNewInput());
        // 수정 시간 업데이트를 위해
        channelRepository.findChannelById(id).setUpdateAt(System.currentTimeMillis());
    }

    public void deleteAllChannels(){
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            channelRepository.deleteAllChannels();
        }
    }
    public void deleteChannelById(UUID id){
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            channelRepository.deleteChannelById(id);
        }
    }
}
