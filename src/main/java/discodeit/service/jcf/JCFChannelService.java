package discodeit.service.jcf;

import discodeit.enity.Channel;
import discodeit.enity.User;
import discodeit.service.ChannelService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private static volatile JCFChannelService instance;
    private final Map<UUID, Channel> data;
    private JCFUserService jcfUserService;
    private JCFMessageService jcfMessageService;

    private JCFChannelService() {
        this.data = new HashMap<>();
    }

    public static JCFChannelService getInstance() {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFChannelService();
                }
            }
        }
        return instance;
    }

    public void setService() {
        this.jcfUserService = jcfUserService.getInstance();
        this.jcfMessageService = jcfMessageService.getInstance();
    }

    public Map<UUID, Channel> getData() {
        return new HashMap<>(data);
    }

    @Override
    public UUID createChannel(String name) {
        System.out.print("채널 생성 요청: ");

        Channel channel = new Channel(name);
        data.put(channel.getId(), channel);
        System.out.println("채널: " + name + ", 생성 시간: " + channel.getCreatedAt());
        return channel.getId();
    }

    @Override
    public void viewAllChannels() {
        System.out.println("--- 전체 채널 조회 ---");
        data.entrySet().stream()
                .sorted(Comparator.comparingLong(entry -> entry.getValue().getCreatedAt())
                )
                .forEach(entry -> {
                    System.out.println("채널: " + entry.getValue().getName());
                    System.out.print("참여 중인 사용자: ");
                    entry.getValue().getUsers().stream()
                            .forEach(user -> {
                                System.out.print(user.getName() + " ");
                            });
                    System.out.println();
                });
    }

    @Override
    public void viewChannelInfo(UUID channelId) {
        if (validateChannel(channelId)) {
            //채널 정보 출력
            System.out.println("--- 채널 조회 ---");

            Channel channel = data.get(channelId);
            System.out.println("채널: " + channel.getName());
            System.out.print("참여 중인 사용자: ");
            channel.getUsers().stream()
                    .forEach(user -> {
                        System.out.print(user.getName() + " ");
                    });
            System.out.println();

            //채널 내 메세지 출력
            if (channel.getMessages().isEmpty()) {
                System.out.println("현재 채널에 작성된 메세지가 없습니다. 채팅을 시작하세요.");
            } else {
                channel.getMessages().stream()
                        .forEach(message -> {
                            if (jcfUserService.validateUser(message.getSender().getId())) {
                                if (channel.getUsers().contains(message.getSender())) {
                                    System.out.println(message.getSender().getName() + ": " + message.getContent());
                                } else {
                                    System.out.println("(이 채널에 더 이상 없는 유저입니다.) " + message.getSender().getName() + ": " + message.getContent());
                                }
                            } else {
                                System.out.println("(탈퇴한 유저입니다.) " + message.getSender().getName() + ": " + message.getContent());
                            }
                        });
            }
        } else {
            System.out.println("존재하지 않는 채널입니다.");
        }
    }

    @Override
    public void updateChannelName(UUID channelId, String name) {
        System.out.print("채널 수정 요청: ");
        if (validateChannel(channelId)) {
            Channel channel = data.get(channelId);
            String prevName = channel.getName();
            channel.updateName(name);
            System.out.println("채널 '" + prevName + "'이 '" + name + "'으로 변경되었습니다.");
        } else {
            System.out.println("존재하지 않는 채널입니다.");
        }
    }

    @Override
    public void deleteChannel(UUID channelId) {
        System.out.print("채널 삭제 요청: ");
        if (validateChannel(channelId)) {
            Channel channel = data.get(channelId);
            //채널-유저, 채널-메세지 관계 삭제
            channel.getUsers().stream()
                    .forEach(user -> user.getChannels().remove(channel));
            channel.getUsers().clear();
            channel.getMessages().clear();
            data.remove(channelId);
            System.out.println("'" + channel.getName() + "' 채널이 삭제되었습니다.");
        } else {
            System.out.println("존재하지 않는 채널입니다.");
        }

    }

    @Override
    public void addUserIntoChannel(UUID channelId, UUID userId) {
        if (!validateChannel(channelId) || !jcfUserService.validateUser(userId)) {
            System.out.println("채널 또는 사용자 데이터가 올바르지 못합니다.");
            return;
        }

        Channel channel = data.get(channelId);
        User user = jcfUserService.getData().get(userId);

        if (channel.getUsers().contains(user)) {
            System.out.println(channel.getName() + "은 채널에 이미 입장한 사용자입니다.");
        } else {
            channel.getUsers().add(user);
            user.getChannels().add(channel);
            System.out.println(user.getName() + "님이 채널 '"
                    + channel.getName() + "'에 입장합니다.");
        }

    }

    @Override
    public void deleteUserInChannel(UUID channelId, UUID userId) {
        if (!validateChannel(channelId) || !jcfUserService.validateUser(userId)) {
            System.out.println("채널 또는 사용자 데이터가 올바르지 못합니다.");
            return;
        }

        Channel channel = data.get(channelId);
        User user = jcfUserService.getData().get(userId);

        if (!channel.getUsers().contains(user) || !user.getChannels().contains(channel)) {
            System.out.println(channel.getName() + "은 채널에 없는 사용자입니다.");
        } else {
            channel.getUsers().remove(user);
            user.getChannels().remove(channel);
            System.out.println(user.getName() + "님이 채널 '"
                    + channel.getName() + "'에서 퇴장합니다.");
        }
    }

    @Override
    public boolean validateChannel(UUID channelId) {
        if (data.containsKey(channelId) && data.get(channelId) != null) return true;
        return false;
    }


}
