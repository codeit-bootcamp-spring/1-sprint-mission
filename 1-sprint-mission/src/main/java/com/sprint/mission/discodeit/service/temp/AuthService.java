//package com.sprint.mission.discodeit.service.currentUse;
//
//import com.sprint.mission.discodeit.dto.UserDto;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.repository.file.FileUserStatusRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//@Service
//public class AuthService{
//
//    FileUserRepository userRepository;
//    FileUserStatusRepository userStatusRepository;
//
//
//    public User login(UserDto userDto) throws IOException, ClassNotFoundException {
//       if(userRepository.find(userDto) == null){
//           throw new RuntimeException();
//       }
//       User user = userRepository.find(userDto);
//       userStatusRepository.find(userDto);
//
//        return userRepository.find(userDto);
//    }
//}
