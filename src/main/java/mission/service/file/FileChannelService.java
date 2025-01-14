package mission.service.file;

import mission.entity.Channel;
import mission.repository.file.FileChannelRepository;
import mission.service.ChannelService;
import mission.service.exception.DuplicateName;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {


    private static final FileChannelRepository fileChannelRepository = new FileChannelRepository();

    @Override
    public Channel create(Channel channel){
        try {
            return fileChannelRepository.register(channel);
        } catch (IOException e) {
            throw new RuntimeException("채널 등록 실패" + e.getMessage());
        }
    }

    @Override
    public Set<Channel> findAll() {
        try {
            return fileChannelRepository.findAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Channel findByName(String channelName) {
        Channel findChannel = findAll().stream().collect(Collectors.toMap(
                Channel::getName, Function.identity())).get(channelName);

        if (findChannel == null){
            System.out.printf("%s는 없는 채널명입니다.", channelName);
            System.out.println();
        }
        return findChannel;
    }

    @Override
    public Channel findById(UUID id) {
        try {
            return fileChannelRepository.findById(id);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 파일(id) 찾는데 오류 발생했습니다" + e.getMessage());
        }
    }

    @Override
    public Channel update(Channel channel) {
        // channel 존재 검증은 앞단에서 입력할 때 완료
        return create(channel);
    }

    @Override
    public void deleteById(UUID id) {
        fileChannelRepository.deleteById(id);
    }
    // 무슨 인자를 받든 상관 없음
//    @Override
//    public void delete(Channel channel) {
//        deleteById(channel.getId());
//    }

    /**
     * 검증
     */
    public void validateDuplicateName(String validatingName){
        Map<String, Channel> channelMap = findAll().stream().collect(Collectors.toMap(
                Channel::getName,
                Function.identity()
        ));

        if (!(channelMap.get(validatingName) == null)){
            throw new DuplicateName("채널명 중복");
        }
        System.out.println("중복되지 않은 채널 이름입니다");
    }

    /**
     * 세팅
     */

    public void createChannelDirect(){
        try {
            fileChannelRepository.createChannelDirectory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
