package some_path._1sprintmission.discodeit.service.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import some_path._1sprintmission.discodeit.dto.ChannelDTO;
import some_path._1sprintmission.discodeit.dto.ChannelUpdateDTO;
import some_path._1sprintmission.discodeit.dto.PrivateChannelDTO;
import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.ReadStatus;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.entiry.enums.ChannelType;
import some_path._1sprintmission.discodeit.repository.ChannelRepository;
import some_path._1sprintmission.discodeit.repository.MessageRepository;
import some_path._1sprintmission.discodeit.repository.ReadStatusRepository;
import some_path._1sprintmission.discodeit.repository.UserRepository;
import some_path._1sprintmission.discodeit.service.ChannelService;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;


    public Channel createPrivateChannel(PrivateChannelDTO dto) {
        Channel channel = new Channel(null, ChannelType.PRIVATE);

        // 유저 추가 및 ReadStatus 생성
        for (User user : dto.getUsers()) {
            channel.addMember(user);
            readStatusRepository.save(new ReadStatus(user, channel, Instant.now()));
        }

        return channelRepository.save(channel);
    }

    public Channel createPublicChannel(String channelName) {
        Channel channel = new Channel(channelName, ChannelType.PUBLIC);
        return channelRepository.save(channel);
    }

    @Override
    public ChannelDTO find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        // ChannelDTO로 변환하여 반환
        return ChannelDTO.from(channel);
    }


    @Override
    public List<ChannelDTO> findAllByUserId(UUID userId) {
        // 모든 채널을 조회 (PUBLIC 채널 포함)
        List<Channel> allChannels = channelRepository.findAll();

        // 해당 User가 볼 수 있는 채널 목록 필터링
        List<ChannelDTO> channels = allChannels.stream()
                .filter(channel -> {
                    if (channel.getType() == ChannelType.PUBLIC) {
                        // PUBLIC 채널은 모두 볼 수 있음
                        return true;
                    } else if (channel.getType() == ChannelType.PRIVATE) {
                        // PRIVATE 채널은 해당 User가 참여한 채널만 볼 수 있음
                        return channel.getUsers().stream().anyMatch(user -> user.getId().equals(userId));
                    }
                    return false;
                })
                .map(ChannelDTO::from)  // Channel 객체를 DTO로 변환
                .collect(Collectors.toList());

        return channels;
    }


    @Override
    public ChannelDTO update(ChannelUpdateDTO dto) {
        // 1. 채널 조회
        Channel channel = channelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new RuntimeException("채널을 찾을 수 없습니다."));

        // 2. PRIVATE 채널 수정 불가 검증
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        // 3. 변경 사항 반영
        channel.setName(dto.getName());
        channel.setMaxPerson(dto.getMaxPerson());

        // 4. 저장 후 DTO 변환하여 반환
        return ChannelDTO.from(channel);
    }


    @Override
    public void delete(UUID channelId) {
        // 채널 존재 여부 확인
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));

        // 해당 채널과 연결된 Message 삭제
        messageRepository.deleteByChannelId(channelId);

        // 해당 채널과 연결된 ReadStatus 삭제
        readStatusRepository.deleteByChannelId(channelId);

        // 채널 삭제
        channelRepository.deleteById(channelId);
    }



    @Override
    //초대코드로 채널 입장하기
    public void joinChannelByInviteCode(UUID userId, String inviteCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Channel channel = channelRepository.findByInviteCode(inviteCode) //초대코드로 채널 반환
                .orElseThrow(() -> new RuntimeException("Invalid invite code"));

        user.getChannels().add(channel);
        channel.getUsers().add(user);

        userRepository.save(user);
    }
}
