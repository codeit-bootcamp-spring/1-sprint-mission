package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.HashMap;
import java.util.Scanner;

public class JCFChannelService implements ChannelService {
    Scanner sc = new Scanner(System.in);
    // HashMap(channelName - ChannelS)
    private final HashMap<String, Channel> Channels;
    public JCFChannelService(){
        // 생성자에서 Channels 데이터 초기화
        this.Channels = new HashMap<>();
    }

    // 의존성 주입을 위한 Channel 반환
    public Channel getChannel(String channelName){
        return Channels.get(channelName);
    }

    public  void Create(User user, String channelName){
        Channels.put(channelName, new Channel(user, channelName));
    }

    public void ReadAll(){
        //Channel ChannelName -  User NickName
        if (Channels.isEmpty()) {
            System.out.println("No Channel exists.\n");
        }else {
            for (Channel channel : Channels.values()) {
                System.out.println("Channel Name : " + channel.getChannelName() + " made by  " + channel.getUser().getNickname());
            }
        }
    }
    public void ReadChannel(String channelName){
        // 특정 채널을 불러오기
        if(Channels.get(channelName) == null ){
            System.out.println(channelName + " does not exist\n");
        }else{
            System.out.println("Channel Name : " + channelName + " made by  " + Channels.get(channelName).getUser().getNickname());
        }
        // + 가진 message 개수 되나?
    }

    public void Update(String channelName){
        // 채널 이름 수정 및 수정 시간 업데이트
        // delete 과정을 거친 후 생성
        System.out.println("new Nickname :");
        String newChannelName = sc.next();
        // 채널에서 해당 사항을 알았어서 여기 작성
        // JCF의 해시코드 key랑 Channel 객체 내부 name을 같이 변경해야 한다.
        // 번거롭다는 생각이 들었다...
        Channels.put(newChannelName, Channels.get(channelName));
        Channels.get(newChannelName).setChannelName(newChannelName);
        Channels.remove(channelName);
        // 수정 시간 업데이트를 위해
        Channels.get(newChannelName).setUpdateAt((System.currentTimeMillis()));
    }
    public void DeleteAll(){
        System.out.println("Do you really delete everything?\n");
        System.out.println("                 [y/n]");
        String keyword = sc.next().toLowerCase();
        if(keyword.equals("y")){
            Channels.clear();
        }
    }
    public void DeleteChannel(String channelName){
        System.out.println("Do you really delete channel?\n");
        System.out.println("                 [y/n]");
        String keyword = sc.next().toLowerCase();
        if(keyword.equals("y")){
            Channels.remove(channelName);
        }
    }
}
