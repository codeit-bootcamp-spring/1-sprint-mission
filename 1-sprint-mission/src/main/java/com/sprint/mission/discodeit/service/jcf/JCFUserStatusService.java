//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.dto.UserDto;
//import com.sprint.mission.discodeit.entity.UserStatus;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
//
//import java.util.List;
//
//public class JCFUserStatusService {
//    JCFUserStatusRepository repository;
//
//    public void create(UserDto userDto){
//        repository.saveStatus(userDto.getId());
//    }
//
//    public UserStatus find(UserDto userDto){
//        return repository.findByUserId(userDto);
//    }
//
//    public List<UserStatus> findAll(){
//        return repository.findAll();
//    }
//
//    public void delete(UserDto userDto){
//        repository.delete(userDto.getId());
//    }
//
//    public void updateByUserId(UserDto userDto){
//        repository.updateStatus(userDto.getId());
//    }
//
//
//}
