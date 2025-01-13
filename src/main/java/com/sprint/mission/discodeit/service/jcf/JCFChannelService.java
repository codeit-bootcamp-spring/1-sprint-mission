package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.HashMap;

public class JCFChannelService implements ChannelService {
    //Scanner sc = new Scanner(System.in);
    // HashMap(channelName - ChannelS)
    private final HashMap<String, Channel> Channels;
    // mocking 이용으로 추가
    private InputHandler inputHandler;

    public JCFChannelService(InputHandler inputHandler){
        // 생성자에서 Channels 데이터 초기화
        this.Channels = new HashMap<>();
        // mocking 이용으로 추가
        this.inputHandler = inputHandler;
    }

    // mocking 이용으로 추가
    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    // 의존성 주입을 위한 Channel 반환
    public Channel getChannel(String channelName){
        return Channels.get(channelName);
    }

    public  void Create(User user, String channelName){
        Channels.put(channelName, new Channel(user, channelName));
    }

    public int ReadAll(){
        //Channel ChannelName -  User NickName
        if (Channels.isEmpty()) {
            System.out.println("No Channel exists.\n");
        }else {
            for (Channel channel : Channels.values()) {
                System.out.println("Channel Name : " + channel.getChannelName() + " made by  " + channel.getUser().getNickname());
            }
        }
        return Channels.size();
    }
    public Channel ReadChannel(String channelName){
        // 특정 채널을 불러오기
        if(Channels.get(channelName) == null ){
            System.out.println(channelName + " does not exist\n");
        }else{
            System.out.println("Channel Name : " + channelName + " made by  " + Channels.get(channelName).getUser().getNickname());
        }
        return Channels.get(channelName);
        // + 가진 message 개수 되나?
    }

    public void Update(String channelName){
        // 채널 이름 수정 및 수정 시간 업데이트
        // delete 과정을 거친 후 생성
        System.out.println("new ChannelName :");
        // String newChannelName = sc.next();
        // 채널에서 해당 사항을 알았어서 여기 작성
        // JCF의 해시코드 key랑 Channel 객체 내부 name을 같이 변경해야 한다.
        Channels.put(inputHandler.getNewInput(), Channels.get(channelName));
        Channels.get(inputHandler.getNewInput()).setChannelName(inputHandler.getNewInput());
        Channels.remove(channelName);
        // 수정 시간 업데이트를 위해
        Channels.get(inputHandler.getNewInput()).setUpdateAt((System.currentTimeMillis()));
    }
    public void DeleteAll(){
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            Channels.clear();
        }
    }
    public void DeleteChannel(String channelName){
        String keyword = inputHandler.getYesNOInput().toLowerCase();
        if(keyword.equals("y")){
            Channels.remove(channelName);
        }
    }
}
