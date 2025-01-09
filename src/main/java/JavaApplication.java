

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;

import java.util.Date;
import java.util.List;

public class JavaApplication {

    public static void main(String[] args) {


        //채널 도메인
        JCFChannelService jcfChannelService = new JCFChannelService();

        //채널 생성
        Channel channel = new Channel("ch1");
        jcfChannelService.createChannel(channel);
        //System.out.println(jcfChannelService.readChannel(channel.getChId()).getChName());;

        //새로운 채널 추가
        Channel channel2 = new Channel("ch2");
        jcfChannelService.createChannel(channel2);

        //채널 명 변경
        jcfChannelService.modifyChannel(channel.getChId(), "ch12");
        //System.out.println(jcfChannelService.readChannel(channel.getChId()).getChName());;

        //모두 읽기
        List<Channel> channelList = jcfChannelService.readAllChannel();
        System.out.println("모든 채널을 출력합니다.");
        for(Channel ch:channelList){
            System.out.println(ch.getChName());
        }
        
        //채널 삭제
        jcfChannelService.deleteChannel(channel2.getChId());
        //모두 읽기
        System.out.println("모든 채널을 출력합니다.");
        for(Channel ch:channelList){
            System.out.println(ch.getChName());
        }

        //채널에 유저 추가
        jcfChannelService.addUser(channel, new User("효정"));
        jcfChannelService.addUser(channel, new User("효정2"));
        List<User> userList =  channel.getUserList();
        for(User ch:userList){
            System.out.println(ch.getUserName());
        }


    }
}
