package mission.service.file;

import mission.entity.Channel;
import mission.repository.file.FileChannelRepository;
import mission.service.ChannelService;
import mission.service.exception.DuplicateName;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {


    private static final FileChannelRepository fileChannelRepository = new FileChannelRepository();

    @Override
    public Channel create(Channel channel){
        // 이름 검증은 이 앞단에서
        try {
            return fileChannelRepository.register(channel);
        } catch (IOException e) {
            throw new RuntimeException("채널 등록 실패" + e.getMessage());
        }
    }

    @Override
    public Channel findByName(String channelName) {
        Set<Channel> channels = findAll();
        Map<String, Channel> channelMap = channels.stream().collect(
                Collectors.toMap(Channel::getName, Function.identity()));

        return channelMap.get(channelName);  // 없으면 null 반환
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

    public void delete(Channel channel) {
        fileChannelRepository.delete(channel);
    }


    /**
     * 검증
     */
    public void validateDuplicateName(String validatingName){
        List<Channel> channels = findAll();
        Map<String, Channel> channelMap = channels.stream().collect(Collectors.toMap(
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
