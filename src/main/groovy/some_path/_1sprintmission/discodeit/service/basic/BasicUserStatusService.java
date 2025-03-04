package some_path._1sprintmission.discodeit.service.basic;

import org.springframework.stereotype.Service;
import some_path._1sprintmission.discodeit.dto.UserStatusDTO;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.entiry.UserStatus;
import some_path._1sprintmission.discodeit.repository.UserRepository;
import some_path._1sprintmission.discodeit.repository.UserStatusRepository;
import some_path._1sprintmission.discodeit.service.UserStatusService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository; // User 존재 여부 확인을 위해 사용

    public BasicUserStatusService(UserStatusRepository userStatusRepository, UserRepository userRepository) {
        this.userStatusRepository = userStatusRepository;
        this.userRepository = userRepository;
    }

    // ✅ 생성
    public UserStatus create(UserStatusDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userStatusRepository.findById(dto.getUserId()).isPresent()) {
            throw new RuntimeException("UserStatus already exists for userId: " + dto.getUserId());
        }

        UserStatus userStatus = new UserStatus(user, dto.getLastSeenAt());
        return userStatusRepository.save(userStatus);
    }

    // ✅ id로 조회
    public Optional<UserStatus> findById(UUID userId) {
        return userStatusRepository.findById(userId);
    }

    // ✅ 모든 UserStatus 조회
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    // ✅ UserStatus 업데이트
    public UserStatus update(UUID id, UserStatusDTO dto) {
        return userStatusRepository.updateByUserId(id, dto.getLastSeenAt());
    }

    // ✅ userId로 특정 User의 상태 업데이트
    public UserStatus updateByUserId(UUID userId, Instant newLastSeenAt) {
        return userStatusRepository.updateByUserId(userId, newLastSeenAt);
    }

    // ✅ 삭제
    public void delete(UUID userId) {
        userStatusRepository.deleteById(userId);
    }
}

