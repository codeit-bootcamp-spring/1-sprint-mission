//package com.sprint.mission.discodeit.service.currentUse;
//
//import com.sprint.mission.discodeit.dto.UserDto;
//import com.sprint.mission.discodeit.entity.UserStatus;
//import com.sprint.mission.discodeit.repository.file.FileUserStatusRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Service
//public class UserStatusService {
//    FileUserStatusRepository repository;
//
//    public void create(UserDto userDto) throws IOException, ClassNotFoundException {
//        repository.saveStatus(userDto.getId());
//    }
//
//    public UserStatus find(UserDto userDto) throws IOException, ClassNotFoundException {
//        return repository.findByUserId(userDto);
//    }
//
//    public List<UserStatus> findAll() throws IOException, ClassNotFoundException {
//        return repository.findAll();
//    }
//
//    public void delete(UserDto userDto) throws IOException, ClassNotFoundException {
//        repository.delete(userDto.getId());
//    }
//
//    public void updateByUserId(UserDto userDto) throws IOException, ClassNotFoundException {
//        repository.updateStatus(userDto.getId());
//    }
//
//
//}
