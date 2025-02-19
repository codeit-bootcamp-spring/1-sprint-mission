package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserCreateDTO;
import com.sprint.mission.discodeit.dto.UserReadDTO;
import com.sprint.mission.discodeit.dto.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("basicUserService")
@Primary
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // ✅ 비밀번호 암호화 추가

    @Override
    public void create(UserCreateDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent() ||
                userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 username 또는 email입니다.");
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword()); // ✅ 비밀번호 암호화
        User user = new User(UUID.randomUUID(), userDTO.getUsername(), userDTO.getEmail(), userDTO.getProfileImageId(), encodedPassword);
        userRepository.save(user);

        // ✅ 디버깅 로그 추가
        System.out.println("✅ 사용자 저장됨: " + user.getId());
    }

    @Override
    public Optional<UserReadDTO> read(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);

        // ✅ 사용자 조회 디버깅 로그
        if (userOptional.isEmpty()) {
            System.out.println("❌ 사용자 조회 실패: " + id);
        } else {
            System.out.println("✅ 사용자 조회 성공: " + userOptional.get().getId());
        }

        return userOptional.map(user ->
                new UserReadDTO(user.getId(), user.getUsername(), user.getEmail(), user.getProfileImageId(), false, user.getLastActive()));
    }

    @Override
    public List<UserReadDTO> readAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserReadDTO(user.getId(), user.getUsername(), user.getEmail(), user.getProfileImageId(), false, user.getLastActive()))
                .toList();
    }

    @Override
    public void update(UUID id, UserUpdateDTO userDTO) {
        userRepository.findById(id).ifPresent(user -> {
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setProfileImageId(userDTO.getProfileImageId());

            // ✅ 비밀번호 변경 가능하도록 수정
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }

            userRepository.save(user);
        });
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean updateLastSeen(UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setLastActive(Instant.now()); // ✅ 마지막 활동 시간 업데이트
            user.setOnline(true); // ✅ 온라인 상태를 true로 변경
            userRepository.save(user); // ✅ 변경된 상태 저장
            return true; // ✅ 업데이트 성공
        } else {
            return false; // ✅ 사용자가 없을 경우 404 반환
        }
    }

    // ✅ 프로필 이미지 업데이트 메서드 추가
    @Override
    public void updateProfileImage(UUID userId, UUID imageId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setProfileImageId(imageId);
            userRepository.save(user);
            System.out.println("✅ 프로필 이미지 업데이트 완료: " + imageId);
        } else {
            System.out.println("❌ 사용자 ID를 찾을 수 없음: " + userId);
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }
}
