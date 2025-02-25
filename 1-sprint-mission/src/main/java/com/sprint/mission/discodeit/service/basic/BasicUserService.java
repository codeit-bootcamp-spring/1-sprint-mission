package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;


@Service
public class BasicUserService implements UserService {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");
    private final BasicUserStatusService statusService;
    private final BasicBinaryContentService contentService;
    private final FileUserRepository userRepository;
    private final FileChannelRepository channelRepository;
    private final FileMessageRepository messageRepository;

    @Autowired
    public BasicUserService(BasicUserStatusService statusService, BasicBinaryContentService contentService, FileUserRepository userRepository, FileChannelRepository channelRepository, FileMessageRepository messageRepository) {
        this.statusService = statusService;
        this.contentService = contentService;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void creatUser(UserDto userDto) throws IOException, ClassNotFoundException {
        userRepository.createUser(userDto);
        userDto.setId(userRepository.find(userDto).getId());
        statusService.create(userDto);
        if(userDto.getContainContent()) {
            contentService.createUserContent(userDto);
        }
    }

    @Override
    public void modifyUser(UserDto user) throws IOException, ClassNotFoundException {
        if (userRepository.checkUserId(user.getId())) {
            userRepository.update(user.getId(), user.getNewName());
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            statusService.updateByUserId(dto);
            channelRepository.checkJoinChannel(user.getId(), user.getNewName());
            messageRepository.modifyUserName(user.getId(), user.getNewName());
            System.out.println("성공적으로 수정하였습니다");
            return;
        }

        System.out.println("존재하지 않는 계정입니다");
    }

    @Override
    public void deleteUser(UserDto user) throws IOException, ClassNotFoundException {
        if (userRepository.checkUserId(user.getId())) {
            userRepository.deleteUser(user.getId());
            contentService.deleteUserContent(user);
            statusService.delete(user);
            channelRepository.deleteUser(user.getId());
            messageRepository.modifyUserName(user.getId(),"UNDEFINED");
            System.out.println("성공적으로 삭제하였습니다");
            return;
        }

        System.out.println("존재하지 않는 계정입니다");
    }

    @Override
    public void showInfoUser(UserDto userDto) throws IOException, ClassNotFoundException {
       userRepository.find(userDto);
    }

    public void showAllUser() throws IOException, ClassNotFoundException {
        userRepository.findAll();
    }
}
