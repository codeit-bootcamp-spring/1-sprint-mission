package some_path._1sprintmission.discodeit.service.basic;

import org.springframework.stereotype.Service;
import some_path._1sprintmission.discodeit.dto.ReadStatusCreateDTO;
import some_path._1sprintmission.discodeit.dto.ReadStatusUpdateDTO;
import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.ReadStatus;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.repository.ChannelRepository;
import some_path._1sprintmission.discodeit.repository.ReadStatusRepository;
import some_path._1sprintmission.discodeit.repository.UserRepository;
import some_path._1sprintmission.discodeit.service.ReadStatusService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public BasicReadStatusService(ReadStatusRepository readStatusRepository,
                             UserRepository userRepository,
                             ChannelRepository channelRepository) {
        this.readStatusRepository = readStatusRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    // ✅ id로 ReadStatus 조회
    public ReadStatus find(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ReadStatus를 찾을 수 없습니다."));
    }

    // ✅ 특정 userId의 ReadStatus 목록 조회
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    // ✅ ReadStatus 업데이트

    public ReadStatus update(ReadStatusUpdateDTO dto) {
        ReadStatus readStatus = readStatusRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("ReadStatus를 찾을 수 없습니다."));

        readStatus.updateLastReadAt(dto.getLastReadAt()); // 새 값으로 업데이트
        return readStatusRepository.update(readStatus);
    }

    // ✅ id로 ReadStatus 삭제
    public void delete(UUID id) {
        if (!readStatusRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("삭제할 ReadStatus가 존재하지 않습니다.");
        }
        readStatusRepository.deleteById(id);
    }

    @Override
    public ReadStatus create(ReadStatusCreateDTO dto) {
        // 1. User 존재 여부 확인
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. Channel 존재 여부 확인
        Channel channel = channelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));

        // 3. 같은 User + Channel 조합이 이미 존재하면 예외 발생
        Optional<ReadStatus> existingReadStatus = readStatusRepository.findByUserAndChannel(user.getId(), channel.getId());
        if (existingReadStatus.isPresent()) {
            throw new IllegalStateException("이미 존재하는 ReadStatus입니다.");
        }

        // 4. 새로운 ReadStatus 생성 및 저장
        ReadStatus readStatus = new ReadStatus(user, channel, dto.getLastReadAt());
        return readStatusRepository.save(readStatus);
    }
}
