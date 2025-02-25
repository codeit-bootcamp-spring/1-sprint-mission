package discodeit.service.basic;

import discodeit.dto.userstatus.CreateUserStatusRequest;
import discodeit.dto.userstatus.UpdateUserStatusRequest;
import discodeit.entity.UserStatus;
import discodeit.repository.UserRepository;
import discodeit.repository.UserStatusRepository;
import discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepo;
    private final UserRepository userRepo;

    @Override
    public UserStatus create(CreateUserStatusRequest request) {
        UUID userId = request.userId();
        if (!userRepo.existsById(userId)) { throw new NoSuchElementException("[error] 존재하지 않는 User ID입니다."); }
        if (userStatusRepo.findByUserId(userId).isPresent()) { throw new IllegalArgumentException("[error] 이미 존재하는 User Status입니다."); }

        UserStatus userStatus = new UserStatus(userId, request.lastActiveAt());
        return userStatusRepo.save(userStatus);
    }

    @Override
    public UserStatus find(UUID userStatusId) {
        return userStatusRepo.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("[error] 존재하지 않는 User Status ID입니다."));
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return userStatusRepo.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("[error] 존재하지 않는 User ID입니다."));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepo.findAll().stream().toList();
    }

    @Override
    public UserStatus update(UUID userStatusId, UpdateUserStatusRequest request) {
        UserStatus userStatus = userStatusRepo.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("[error] 존재하지 않는 User Status ID입니다."));
        Instant newLastActiveAt = request.newLastActiveAt();
        userStatus.update(newLastActiveAt);
        return userStatusRepo.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UpdateUserStatusRequest request) {
        if (!userRepo.existsById(userId)) { throw new NoSuchElementException("[error] 존재하지 않는 User ID입니다."); }

        UserStatus userStatus = userStatusRepo.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("[error] 존재하지 않는 User Status ID입니다."));
        Instant newLastActiveAt = request.newLastActiveAt();
        userStatus.update(newLastActiveAt);
        return userStatusRepo.save(userStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        if (!userStatusRepo.existsById(userStatusId)) { throw new NoSuchElementException("[error] 존재하지 않는 User Status ID입니다."); }
        userStatusRepo.deleteById(userStatusId);
    }
}
