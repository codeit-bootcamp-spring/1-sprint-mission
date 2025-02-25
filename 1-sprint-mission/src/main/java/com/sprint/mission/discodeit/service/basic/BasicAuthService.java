package com.sprint.mission.discodeit.service.basic;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BasicAuthService{

    FileUserRepository userRepository;
    FileUserStatusRepository userStatusRepository;

    @Autowired
    public BasicAuthService(FileUserRepository userRepository, FileUserStatusRepository userStatusRepository) {
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
    }

    public void login(UserDto userDto) throws IOException, ClassNotFoundException {
       if(userRepository.find(userDto) == null || userStatusRepository.findByUserId(userDto) == null){
           throw new RuntimeException();
       }

       userStatusRepository.updateStatus(userDto);
    }
}
