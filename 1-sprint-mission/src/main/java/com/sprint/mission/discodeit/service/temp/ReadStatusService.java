//package com.sprint.mission.discodeit.service.currentUse;
//
//import com.sprint.mission.discodeit.dto.ChannelDto;
//import com.sprint.mission.discodeit.entity.ReadStatus;
//import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
//import com.sprint.mission.discodeit.repository.file.FileReadStatusRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.UUID;
//
//
//@RequiredArgsConstructor
//@Service
//public class ReadStatusService {
//
//    FileReadStatusRepository repository;
//    FileChannelRepository channelRepository;
//
//    public void create(ChannelDto dto) throws IOException, ClassNotFoundException {
//        if(repository.findStatus(dto.getChannelId()) != null){
//            throw new RuntimeException();
//        }
//
//        repository.createStatus(dto.getChannelId(),channelRepository.joinUserIdList(dto.getChannelId()),dto.getUserId());
//    }
//
//    public ReadStatus find(ChannelDto dto) throws IOException, ClassNotFoundException {
//        return repository.findStatus(dto.getChannelId());
//    }
//
//    public List<UUID> findAllByUserId(ChannelDto dto) throws IOException, ClassNotFoundException {
//        return repository.findByUserId(dto.getUserId());
//    }
//
//    public void deleteStatus(UUID deleteChannelId) throws IOException, ClassNotFoundException {
//        repository.deleteStatus(deleteChannelId);
//    }
//
//    public void addUser(ChannelDto dto) throws IOException, ClassNotFoundException {
//        repository.addUser(dto.getChannelId(),dto.getUserId());
//    }
//}
