package mission.service.jcf;


import mission.entity.Channel;
import mission.entity.User;
import mission.repository.jcf.JCFChannelRepository;
import mission.service.ChannelService;
import mission.service.exception.DuplicateName;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {

    private final JCFChannelRepository channelRepository = new JCFChannelRepository();

    @Override
    public Channel create(Channel channel) {
        return channelRepository.register(channel);
    }

    @Override
    public Set<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel findByName(String channelName) {
        // 변환후 조회 vs filter => 어떤게 빠를까
        Channel findChannel = findAll().stream()
                .collect(Collectors.toMap(
                        Channel::getName,
                        Function.identity()
                ))
                .get(channelName);

        if (findChannel == null){
            System.out.printf("%s는 없는 채널명입니다.", channelName);
            System.out.println();
        }
        return findChannel;
    }

    @Override
    public Channel findById(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public Channel update(Channel channel) {
        return create(channel);  // 클래스 내 메서드 활용(Map 덮어쓰기 가능)
    }

    @Override
    public void deleteById(UUID id) {
        Channel deletingChannel = findById(id);
        deletingChannel.removeAllUser(); // 채널이 데이터에서 사라지면 user도 사라진다면 이 코드는 필요 없음
        de

        for (User user : deletingChannel.getUserList()) {
            user.getChannels().remove(deletingChannel);
        }
        // 그 채널에서 생겼던 메시지도 삭제해야될지, 그래도 기록으로 보관하니 삭제하지 말지
        //List<Message> messagesInChannel = findMessageInChannel(channelId);
        channelRepository.deleteById(id);
    }

    /**
     * 중복 검증
     */
    @Override
    public void validateDuplicateName(String name){
        Set<Channel> findChannel = findAll().stream().filter(channel -> channel.getName().equals(name))
                .collect(Collectors.toSet());
        if (!findChannel.isEmpty()){
            throw new DuplicateName(
                    String.format("%s는 이미 존재하는 이름의 채널명입니다", name));
        }
    }
}

