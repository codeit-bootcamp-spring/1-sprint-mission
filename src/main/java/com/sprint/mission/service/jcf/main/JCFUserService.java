package com.sprint.mission.service.jcf.main;


import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.BinaryContentMapper;
import com.sprint.mission.dto.UserMapper;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.dto.request.UserDtoForUpdate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.service.UserService;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.service.jcf.addOn.BinaryServiceImpl;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j @Service
@RequiredArgsConstructor
@Transactional
public class JCFUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusService userStatusService;
    private final UserServiceSupporter userServiceSupporter;

    @Override
    public User create(UserDtoForCreate requestDTO, MultipartFile profile) {
        userServiceSupporter.isDuplicateNameEmail(findAll(), requestDTO.username(), requestDTO.email());
        User createdUser = userServiceSupporter.createUser(requestDTO, profile);
        User savedUser = userRepository.save(createdUser);// SAVE해야 UUID 생성
        UserStatus userStatus = userStatusService.create(savedUser);
        return savedUser.assignStatus(userStatus);
    }


    @Override
    public User update(UUID userId, UserDtoForUpdate requestDTO) {
        userServiceSupporter.isDuplicateNameEmail(findAll(), requestDTO.username(), requestDTO.email());
        User updatingUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));

        return updatingUser.update(requestDTO.username(), requestDTO.password(), requestDTO.email());
    }

    // DTO를 사용해서 온라인 상태정보도 포함해서 보내기
    // 패스워드 정보 제외
    @Transactional(readOnly = true)
    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAllWithRelations();
    }

    //관련된 도메인(userstatus)도 같이 삭제 테스트 in UserUserStatusCascadeTest
    @Override
    public void delete(UUID userId) {
        //if (!userRepository.existsById(userId)) throw new NotFoundId();
        User deletingUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));
        userRepository.delete(deletingUser);
    }
}
