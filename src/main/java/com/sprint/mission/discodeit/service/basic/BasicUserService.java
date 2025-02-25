package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor // final/nonnull 이라 값이 반드시 있어야 하는 생성자 자동 생성, @Autowired 안써줘도 생성자가 하나일 때는 자동으로 의존성 주입! (+생성자 있으면 자바단에서 기본생성자 생성 안함)
@Service
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    // TODO : 회원이 입력할 수 있는 값만 따로 dto로 묶어 전달
    public User create(UserCreateRequest request, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        // TODO : 1. 발생 가능한 예외를 먼저 생각하기 -> 예외 처리 : email과 username 검색 => 이존회
        // TODO : 2. dto -> 변수 할당 3. 객체 생성 4. repository.save
        String email = request.email();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
            // TODO : '예외 처리'라는 건 예외를 해결하는 게 아니라, 프로그램이 종료되지 않도록 예외 상황을 출력/상태코드 반환해서 알리는 게 목적임. 예외 해결은 예외 처리 개념이 아니라 그냥 애초에 예외를 방지하는 로직을 쓰는 것으로 수행.
            // TODO : 예외 발생 -> 메인/Controller 단에서 처리 안하면 JVM으로 넘어가서 프로그램 종료될 수 있음 -> ExceptionHandler에서 처리해주기
            // TODO : @ControllerAdive, @ExceptionHandler
        }

        // 프로필 이미지 설정
        // TODO : 이해 1. .map의 기능, 문법 2. Optional 문법 -> ok
        // TODO : 프로필 정보를 받았어 그런데 회원 객체 생성시 프로필 아이디가 필요해 -> 옵셔널을 이용해 프로필 아이디 겟하는 로직을 써야겠네
        UUID nullableProfileId = optionalProfileCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                }) // TODO : 한줄로 끝나는 코드면 자동 반환되므로 {}, return문 필요 없는데, 여러 코드가 필요하고 최종적으로 반환할 값이 있다면 "{}, return문 필수"
                .orElse(null);

        String username = request.username();
        String password = request.password();
        User newUser = new User(username, email, password, nullableProfileId);
        User createdUser = userRepository.save(newUser);

        // 생성된 회원 user status 설정
        Instant lastActiveAt = Instant.now();
        UserStatus userStatus = new UserStatus(createdUser.getId(), lastActiveAt);
        userStatusRepository.save(userStatus);
        return createdUser;
    }

    @Override
    // TODO : 회원을 찾고 반환하는 값으로 password를 주진 않을 거잖아 -> 보안 차원의 DTO
    public UserDto find(UUID userId) {
        // 반환타입이 UserDto니까, mapper을 이용해 Optional의 null이 아닌 경우인 user 변수를 dto로 변경해서 return
        return userRepository.findById(userId)
                .map(user -> toDto(user))
                .orElseThrow(() -> new NoSuchElementException("아이디가" + userId + "인 회원이 존재하지 않습니다."));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream() //TODO : List<User>를 Stream<User>로 변환 -> "User 객체들이 순차적으로 처리될 준비가 된 상태" -> 그 다음 action인 mapper로.
                .map(user -> toDto(user))
                .toList(); // 스트림 메소드. 스트림 상태인 객체들을 리스트로 변환
    }

    @Override
    // TODO : 업뎃할거야. User 엔티티의 모든 필드를 업뎃요소로 줄 필요는 없어 -> DTO 만들자
    public User update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("아이디가 " + userId + "인 회원이 존재하지 않습니다."));

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();
        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("이메일이 " + newEmail + "인 회원이 이미 존재합니다.");
        }
        if (userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("이름이 " + newUsername + "인 회원이 이미 존재합니다.");
        }

        UUID nullableProfileId = optionalProfileCreateRequest
                .map(profileRequest -> {
                    Optional.ofNullable(user.getProfileId())
                            .ifPresent(id -> binaryContentRepository.deleteById(id));
                    // TODO : DB에서 원래 프로필을 제거해주는 로직 (DB 정리)

                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        String newPassword = userUpdateRequest.newPassword();
        user.update(newUsername, newEmail, newPassword, nullableProfileId);

        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("아이디가 " + userId + "인 회원이 존재하지 않습니다."));

        Optional.ofNullable(user.getProfileId())
                .ifPresent(binaryContentRepository::deleteById);
        userStatusRepository.deleteByUserId(userId); // 관련 도메인 삭제 (프로필, 회원상태)

        userRepository.deleteById(userId);
    }

    // DTO 변환 로직 메서드
    // TODO : 이 클래스 내부에서만 쓰일 자체 메서드이니까 private 선언
    // TODO : 인터페이스는 외부와 상호작용할 메서드들(로미오 역할의 특징들-성격, 말투, 목소리 등. 이런거 줄리엣 역할도 알아야 연기를 같이 할 거 아냐~)의 나열(=public)이기 때문에 private은 인터페이스에 포함되지 않는다! 당연.
    private UserDto toDto(User user) {
        Boolean online = userStatusRepository.findByUserId(user.getId())
                .map(userStatus -> userStatus.isOnline())// UserStatus의 Optional 값이 존재하면 isOnline 메서드 호출-> Optional<Boolean> 반환
                .orElse(null);
        // TODO : 이렇게 User 엔티티 말고 외부의 값이 조합된 DTO -> 해당 값 (online)은 위처럼 따로 정의를 해줘야겠지.
        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                online
        );
    }
}