package com.sprint.mission.service.jcf.main;


import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.request.BinaryContentDto;
import com.sprint.mission.dto.request.UserDtoForUpdate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.jcf.main.JCFChannelRepository;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import com.sprint.mission.service.UserService;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.service.jcf.addOn.BinaryService;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JCFUserService implements UserService {

    private final JCFUserRepository userRepository;
    private final UserStatusService userStatusService;
    private final JCFChannelRepository channelRepository;
    private final BinaryService profileService;

    // 테스트에서 create 후 userId 필요해서 임시적 USER 반환 (할거면 나중에 컨트롤러에서)
    @Override
    public User create(UserDtoForCreate requestDTO, Optional<BinaryContentDto> profileDTO) {

        isDuplicateNameEmail(requestDTO.username(), requestDTO.email());

        User createdUser = requestDTO.toEntity();
        // 선택적 프로필 생성
        if (profileDTO.isPresent()){
            BinaryContent binaryContent = profileService.create(profileDTO.get());
            createdUser.setProfileImgId(binaryContent.getId());
        }

        // UserStatus 생성
        userStatusService.create(createdUser.getId());

        return userRepository.save(createdUser);
    }

    // DTO를 사용해서 온라인 상태정보도 포함해서 보내기
    // 패스워드 정보 제외
    @Override
    public User update(UUID userId, UserDtoForUpdate requestDTO) {
        isDuplicateNameEmail(requestDTO.newName(), requestDTO.newEmail());

        return userRepository.findById(userId).map(user -> {
            User updatedUser = requestDTO.toUpdateEntity(user);
            return userRepository.save(updatedUser);
        }).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));
    }

    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    //관련된 도메인도 같이 삭제 -> BinaryContent(프로필), Userstatus
    @Override
    public void delete(UUID userId) {
        //if (!userRepository.existsById(userId)) throw new NotFoundId();

        userRepository.findById(userId)
            .ifPresentOrElse(user -> {
                profileService.deleteById(user.getProfileImgId());
                userStatusService.deleteByUserId(userId);
                userRepository.delete(userId);
            }, () -> new CustomException(ErrorCode.NO_SUCH_USER));
    }

        //사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현

    @Override
    public void isDuplicateNameEmail(String name, String email) {
        List<User> allUser = userRepository.findAll();
        boolean isDuplicateName = allUser.stream()
            .anyMatch(user -> name.equals(user.getName()));
        if (isDuplicateName) throw new CustomException(ErrorCode.ALREADY_EXIST_NAME);

        boolean isDuplicateEmail = allUser.stream().anyMatch(user -> email.equals(user.getEmail()));
        if (isDuplicateEmail) throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
    }
}
