package com.sprint.mission.service.file;


import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.file.main.FileChannelRepository;
import com.sprint.mission.repository.file.main.FileUserRepository;
import com.sprint.mission.service.UserService;
import com.sprint.mission.dto.request.UserDtoForRequest;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUserService implements UserService{

    private final FileUserRepository fileUserRepository;
    private final BinaryProfileService profileService;
    private final UserStatusService userStatusService;
    private final UserService userService;
    private final FileChannelRepository fileChannelRepository;

    @Override
    @SneakyThrows
    public void create(UserDtoForRequest userDto) {
        isDuplicateNameEmail(userDto.getUsername(), userDto.getEmail());

        User user = User.createUserByRequestDto(userDto);
        byte[] profileImg = userDto.getProfileImgAsByte();
        if (!(profileImg == null)) {
            // 나중에 파일용으로 바꾸기
            profileService.create(new BinaryProfileContentDto(user, profileImg));
        }

        //userStatusService.create(user.getId());
        //파일용
        fileUserRepository.save(user);
    }

    @SneakyThrows
    @Override
    public void update(UUID userId, UserDtoForRequest updateDto)  {
        isDuplicateNameEmail(updateDto.getUsername(), updateDto.getEmail());

        User updatingUser = fileUserRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));
        updatingUser.updateByRequestDTO(updateDto);

        byte[] profileImg = updateDto.getProfileImgAsByte();
        if (profileImg != null) {
            // file용으로 바꿀 것
            profileService.create(new BinaryProfileContentDto(updatingUser, profileImg));
        }

        fileUserRepository.save(updatingUser);
    }

    @Override
    public User findById(UUID userId){
        return fileUserRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));
    }


    @SneakyThrows
    @Override
    public List<User> findAll(){
        return fileUserRepository.findAll();
    }

    //@Override
    @SneakyThrows
    public void delete(UUID userId) {
        //if (!fileUserRepository.existsById(userId)) throw new NotFoundId();

        User deletingUser = fileUserRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));
        deletingUser.getChannels().forEach((channel) -> {
            channel.removeUser(deletingUser);
            fileChannelRepository.save(channel);
        });

        fileUserRepository.delete(userId);
        userStatusService.delete(userId);
        profileService.delete(userId);
    }

    /**
     * 검증, 디렉토리 생성
     */
    @SneakyThrows
    public void isDuplicateNameEmail(String username, String email) {
        List<User> allUser = fileUserRepository.findAll();
        boolean isDuplicateName = allUser.stream().anyMatch(user -> user.getName().equals(username));
        if (isDuplicateName) throw new CustomException(ErrorCode.ALREADY_EXIST_NAME);

        boolean isDuplicateEmail = allUser.stream().anyMatch(user -> user.getEmail().equals(email));
        if (isDuplicateEmail) throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
    }

    // 디렉토리 생성
    public void createUserDirectory() {
        try {
            fileUserRepository.createUserDirectory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
