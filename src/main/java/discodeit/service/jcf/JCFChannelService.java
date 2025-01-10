package discodeit.service.jcf;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;
import discodeit.service.ChannelService;
import discodeit.service.ServiceFactory;

import java.time.chrono.JapaneseChronology;
import java.util.ArrayList;
import java.util.List;

public class JCFChannelService implements ChannelService {

    private static volatile JCFChannelService instance;

    private JCFUserService jcfUserService;
    private JCFMessageService jcfMessageService;

    private final List<Channel> data;

    private JCFChannelService() {
        this.data = new ArrayList<>();
    }

    public static JCFChannelService getInstance() {
        if(instance == null) {
            synchronized (JCFUserService.class) {
                if(instance == null) {
                    instance = new JCFChannelService();
                }
            }
        }
        return instance;
    }

    public void setJcfUserService(JCFUserService jcfUserService) {
        this.jcfUserService = jcfUserService;
    }

    public void setJcfMessageService(JCFMessageService jcfMessageService) {
        this.jcfMessageService = jcfMessageService;
    }

    @Override
    public Channel createChannel(String name) {
        System.out.print("채널 생성 요청: ");
        try {
            Channel channel = new Channel(name);
            data.add(channel);
            System.out.println("채널: " + name + ", 생성 시간: " + channel.getCreatedAt());
            return channel;    
        } catch (Exception e) {
            System.out.println("채널 생성 중 오류 발생");
            return null;
        }
    }

    @Override
    public void viewAllChannels() {
        System.out.println("--- 전체 채널 조회 ---");
        data.stream()
                .forEach(channel -> {
                    System.out.println("채널: " + channel.getName());
                    System.out.print("참여 중인 사용자: " );
                    channel.getUsers().stream()
                            .forEach(user -> {
                                System.out.print(user.getName() + " ");
                            });
                    System.out.println();
                });
    }

    @Override
    public void viewChannelInfo(Channel channel) {
        if(data.contains(channel)){
            System.out.println("--- 채널 조회 ---");
            System.out.println("채널: " + channel.getName());
            System.out.print("참여 중인 사용자: " );
            channel.getUsers().stream()
                            .forEach(user -> {
                                System.out.print(user.getName() + " ");
                            });
            System.out.println();
            if(channel.getMessages().isEmpty()){
                System.out.println("현재 채널에 작성된 메세지가 없습니다. 채팅을 시작하세요.");
            } else {
                channel.getMessages().stream()
                        .forEach(message -> {
                            if(channel.getUsers().contains(message.getSender()))
                                System.out.println(message.getSender().getName() + ": "+message.getContent());
                            else{
                                System.out.println("(알 수 없는 유저) " + message.getSender().getName() + ": "+message.getContent());
                            }
                        });
            }
        } else {
            System.out.println("존재하지 않는 채널입니다.");
        }
    }

    @Override
    public void updateChannelName(Channel channel, String name) {
        System.out.print("채널 수정 요청: ");
        try {
            String prevName=channel.getName();
            channel.updateName(name);
            System.out.println("채널 '" + prevName+"'이 '" + name + "'으로 변경되었습니다.");
        } catch (Exception e) {
            System.out.println(channel.getName()+" 이름 수정 중 오류 발생");
        }
    }

    @Override
    public void deleteChannel(Channel channel) {
        System.out.print("채널 삭제 요청: ");
        try {
            if(data.contains(channel)){
                data.remove(channel);
                System.out.println("'" + channel.getName()+"' 채널이 삭제되었습니다.");
            } else {
                System.out.println("존재하지 않는 채널입니다.");
            }
        } catch (Exception e) {
            System.out.println(channel.getName() + " 채널 삭제 중 오류 발생");
        }

    }

    @Override
    public void addUserIntoChannel(Channel channel, User user) {
        try {
            if(channel.getUsers().contains(user)){
                System.out.println(channel.getName() + " 채널에 이미 입장한 사용자입니다.");
            } else {
                channel.getUsers().add(user);
                user.getChannels().add(channel);
                System.out.println(user.getName() + "님이 채널 '"
                        + channel.getName() + "'에 입장합니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteUserInChannel(Channel channel, User user) {
        try {
            if (channel.getUsers() == null || user.getChannels() == null) {
                System.out.println("올바르지 않은 데이터입니다.");
                return;
            }
            if(!channel.getUsers().contains(user) || !user.getChannels().contains(channel)){
                System.out.println(channel.getName() + " 채널에 없는 사용자입니다.");
            } else {
                channel.getUsers().remove(user);
                user.getChannels().remove(channel);
                System.out.println(user.getName() + "님이 채널 '"
                        + channel.getName() + "'에서 퇴장합니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
