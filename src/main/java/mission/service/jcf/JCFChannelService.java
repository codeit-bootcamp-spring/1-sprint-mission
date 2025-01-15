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
    public Channel update(Channel channel) {
        return create(channel);  // 클래스 내 메서드 활용(Map 덮어쓰기 가능)
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
    public void deleteById(UUID id) {
        channelRepository.deleteById(id);
    }

    /**
     * 중복 검증
     */
    @Override
    public void validateDuplicateName(String name){
        boolean isDuplicate = findAll().stream()
                .anyMatch(channel -> channel.getName().equals(name));
        if (isDuplicate){
            throw new DuplicateName(
                    String.format("%s는 이미 존재하는 이름의 채널명입니다", name));
        }
    }
}

