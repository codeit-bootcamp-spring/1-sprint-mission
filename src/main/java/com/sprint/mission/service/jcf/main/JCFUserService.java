package com.sprint.mission.service.jcf.main;


import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.request.BinaryContentDto;
import com.sprint.mission.dto.request.UserDtoForUpdate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.repository.jcf.main.JCFChannelRepository;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import com.sprint.mission.service.UserService;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.service.jcf.addOn.BinaryService;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class JCFUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusService userStatusService;
    private final BinaryService profileService;
    private final ExecutorService ves;

    @Override
    public User create(UserDtoForCreate requestDTO, MultipartFile profile) {

        isDuplicateNameEmail(requestDTO.username(), requestDTO.email());
        User createdUser = requestDTO.toEntity();

        Optional<BinaryContentDto> profileDto = BinaryContentDto.convertToBinaryContentDto(profile);
        // 선택적 프로필 생성
        profileDto.ifPresent((dto) -> {
            BinaryContent binaryContent = profileService.create(dto);
            createdUser.setProfile(binaryContent);
        });

        userRepository.save(createdUser);
        // UserStatus 생성
        userStatusService.create(createdUser.getId());
        return createdUser;
    }

    // DTO를 사용해서 온라인 상태정보도 포함해서 보내기
    // 패스워드 정보 제외
    @Override
    public User update(UUID userId, UserDtoForUpdate requestDTO) {
        isDuplicateNameEmail(requestDTO.newName(), requestDTO.newEmail());
        User updatingUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));
        updatingUser.update(requestDTO.newName(), requestDTO.newEmail(), requestDTO.newPassword());
        return updatingUser;
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

        User deletingUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));

//        ves.submit(() -> profileService.deleteById(deletingUser.()));
//        ves.submit(() -> userStatusService.deleteByUserId(userId));
//        ves.submit(() -> userRepository.delete(userId));
//        // delete관련된건 실패해도 오류나지 않으니
    }

    //사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현

    @Override
    public void isDuplicateNameEmail(String name, String email) {
        List<User> allUser = userRepository.findAll();

        Future<?> isDuplicateNameF = ves.submit(() -> {
            boolean isDuplicateName = allUser.stream()
                    .anyMatch(user -> name.equals(user.getName()));
            if (isDuplicateName) throw new CustomException(ErrorCode.ALREADY_EXIST_NAME);
        });

        Future<?> isDuplicateEmailF = ves.submit(() -> {
            boolean isDuplicateEmail = allUser.stream().anyMatch(user -> email.equals(user.getEmail()));
            if (isDuplicateEmail) throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
        });

        try {
            isDuplicateNameF.get();
            isDuplicateEmailF.get();
        } catch (ExecutionException e) {
            throw e.getCause() instanceof CustomException
                    ? (CustomException) e.getCause()
                    : new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        boolean isDuplicateName = allUser.stream()
//                .anyMatch(user -> name.equals(user.getName()));
//        if (isDuplicateName) throw new CustomException(ErrorCode.ALREADY_EXIST_NAME);
//
//        boolean isDuplicateEmail = allUser.stream().anyMatch(user -> email.equals(user.getEmail()));
//        if (isDuplicateEmail) throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
    }
}
