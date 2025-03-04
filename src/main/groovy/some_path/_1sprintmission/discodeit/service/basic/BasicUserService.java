package some_path._1sprintmission.discodeit.service.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import some_path._1sprintmission.discodeit.dto.ProfileImageRequestDTO;
import some_path._1sprintmission.discodeit.dto.UserCreateRequestDTO;
import some_path._1sprintmission.discodeit.dto.UserDTO;
import some_path._1sprintmission.discodeit.dto.UserUpdateRequestDTO;
import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.entiry.UserStatus;
import some_path._1sprintmission.discodeit.entiry.validation.Email;
import some_path._1sprintmission.discodeit.entiry.validation.Phone;
import some_path._1sprintmission.discodeit.repository.*;
import some_path._1sprintmission.discodeit.service.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final Random random = new Random();

    @Override
    public User create(UserCreateRequestDTO userCreateRequest) {
        // username 중복 검사
        if (userRepository.existsByUsername(userCreateRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // email 중복 검사
        if (userRepository.existsByEmail(userCreateRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Email과 Phone 객체 생성
        Email userEmail = new Email(userCreateRequest.getEmail());
        Phone userPhone = new Phone(userCreateRequest.getPhone());

        // User 객체 생성
        User newUser = new User(
                userCreateRequest.getUsername(),
                userCreateRequest.getPassword(),
                userEmail,
                userPhone
        );

        //UserStatus 객체 생성 -> Repository 생성 필요
        UserStatus userStatus = new UserStatus(newUser, Instant.now());

        // 프로필 이미지가 제공된 경우 설정
        if (userCreateRequest.getProfileImageRequest() != null) {
            newUser.setProfileImageUrl(userCreateRequest.getProfileImageRequest().getProfileImageUrl());
        }

        // 유저 저장
        return userRepository.save(newUser);
    }

    @Override
    public UserDTO find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        boolean isOnline = userStatusRepository.findById(userId)
                .map(UserStatus::isOnline)
                .orElse(false);

        return UserDTO.from(user, isOnline);
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    boolean isOnline = userStatusRepository.findById(user.getId())
                            .map(UserStatus::isOnline)
                            .orElse(false);
                    return UserDTO.from(user, isOnline);
                })
                .collect(Collectors.toList());
    }


    @Override
    public UserDTO update(UserUpdateRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + request.getUserId() + " not found"));


        request.getUsername().ifPresent(user::setUsername);
        request.getPassword().ifPresent(user::setPassword);
        request.getEmail().ifPresent(email -> user.setUserEmail(new Email(email)));
        request.getPhone().ifPresent(phone -> user.setUserPhone(new Phone(phone)));
        request.getIntroduce().ifPresent(user::setIntroduce);
        request.getUserType().ifPresent(user::setUserType);
        request.getDiscodeStatus().ifPresent(user::setDiscodeStatus);

        userRepository.save(user);

        boolean isOnline = userStatusRepository.findById(user.getId())
                .map(UserStatus::isOnline)
                .orElse(false);

        return UserDTO.from(user, isOnline);
    }


    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        // 1. ReadStatus 삭제 (해당 유저와 관련된 모든 ReadStatus 제거)
        readStatusRepository.deleteByUser(user);

        // 2. UserStatus 삭제 (유저의 온라인 상태 관리 객체 삭제)
        userStatusRepository.deleteById(userId);

        // 3. 채널에서 유저 제거 (유저가 속한 모든 채널에서 제거)
        List<Channel> channels = channelRepository.findAllByUsersContaining(user);
        for (Channel channel : channels) {
            channel.removeMember(user);
            channelRepository.save(channel); // 변경사항 저장
        }

        // 4. 유저 삭제
        userRepository.deleteById(user.getId());
    }


    @Override
    public int makeDiscriminator(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        }

        String username = userOptional.get().getUsername();

        int sureNumber;
        while (true) {
            int notSureNumber = random.nextInt(10000); // 0~9999 범위
            if (!userRepository.isDiscriminatorDuplicate(username, notSureNumber)) {
                sureNumber = notSureNumber;
                break;
            }
        }
        return sureNumber;
    }

}
