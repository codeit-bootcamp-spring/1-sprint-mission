package com.sprint.mission.service.file;


import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.file.FileChannelRepository;
import com.sprint.mission.repository.file.FileUserRepository;
import com.sprint.mission.service.UserService;
import com.sprint.mission.dto.request.BinaryProfileContentDto;
import com.sprint.mission.dto.request.UserDtoForRequest;
import com.sprint.mission.dto.response.FindUserDto;
import com.sprint.mission.service.jcf.addOn.BinaryProfileService;
import com.sprint.mission.service.jcf.addOn.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileUserService {

    private final FileUserRepository fileUserRepository;
    private final BinaryProfileService profileService;
    private final UserStatusService userStatusService;
    private final UserService userService;
    private final FileChannelRepository fileChannelRepository;


//    private FileChannelService getFileChannelService(){
//        if (fileChannelService == null) return fileChannelService = FileChannelService.getInstance();
//        else return fileChannelService;

    //@Override
    @SneakyThrows
    public User create(UserDtoForRequest userDto) {
        User createUser = User.createUserByRequestDto(userDto);
        return fileUserRepository.save(createUser);
        // 수정-생성 I/O오류 구분하기 위해 여기서 잡지 않고 I/O 예외 던지기
    }

    //@Override
    @SneakyThrows
    public void update(UUID userId, UserDtoForRequest dto)  {
        isDuplicateNameEmail(dto.getUsername(), dto.getEmail());

        User updatingUser = fileUserRepository.findById(userId).orElseThrow(RuntimeException::new);
        updatingUser.setAll(dto.getUsername(), dto.getPassword(), dto.getEmail());
        if (dto.getProfileImg() != null) {
            profileService.create(new BinaryProfileContentDto(dto.getProfileImg()));
        }
        fileUserRepository.save(updatingUser);
    }

    //@Override
    public FindUserDto findById(UUID userId){
        return fileUserRepository.findById(userId).map((user -> {
            Boolean isOnline = userStatusService.findById(userId)
                    .map(userStatus -> userStatus.isOnline())
                    .orElse(false);
            return new FindUserDto(user, isOnline);
        })).orElseGet(FindUserDto::new);
    }

    //@Override
    @SneakyThrows
    public List<FindUserDto> findAll(){
        return fileUserRepository.findAll().stream()
                .map(user -> {
                    Boolean isOnline = userStatusService.findById(user.getId())
                            .map(userStatus -> userStatus.isOnline())
                            .orElse(false);
                    return new FindUserDto(user, isOnline);
                }).collect(Collectors.toCollection(ArrayList::new));
    }

    //@Override
    @SneakyThrows
    public void delete(UUID userId) {
        User deletingUser = fileUserRepository.findById(userId).orElseThrow();
        for (Channel channel : deletingUser.getChannels()) {
            channel.removeUser(deletingUser);
            fileChannelRepository.save(channel);
        }
        fileUserRepository.delete(userId);
    }

    /**
     * 검증, 디렉토리 생성
     */
    @SneakyThrows
    public void isDuplicateNameEmail(String username, String email) {
        List<User> allUser = fileUserRepository.findAll();
        boolean isDuplicateName = allUser.stream().anyMatch(user -> user.getName().equals(username));
        if (isDuplicateName)
            throw new IllegalStateException(String.format("Duplicate Name: %s", username));

        boolean isDuplicateEmail = allUser.stream().anyMatch(user -> user.getEmail().equals(email));
        if (isDuplicateEmail)
            throw new IllegalStateException(String.format("Duplicate Email: %s", email));
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
