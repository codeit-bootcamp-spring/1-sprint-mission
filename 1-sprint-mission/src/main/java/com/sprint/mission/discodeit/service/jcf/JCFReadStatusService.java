//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.dto.ChannelDto;
//import com.sprint.mission.discodeit.entity.ReadStatus;
//import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//
//
//public class JCFReadStatusService {
//
//    JCFReadStatusRepository repository;
//    JCFChannelRepository channelRepository;
//
//    public void create(ChannelDto dto){
//        if(repository.findStatus(dto.getChannelId()) != null){
//            throw new RuntimeException();
//        }
//
//        repository.createStatus(dto.getChannelId(),channelRepository.joinUserIdList(dto.getChannelId()),dto.getUserId());
//    }
//
//    public ReadStatus find(ChannelDto dto){
//        return repository.findStatus(dto.getChannelId());
//    }
//
//    public List<UUID> findAllByUserId(ChannelDto dto){
//        return repository.findByUserId(dto.getUserId());
//    }
//
//    public void deleteStatus(UUID deleteChannelId){
//        repository.deleteStatus(deleteChannelId);
//    }
//
//    public void addUser(ChannelDto dto) {
//        repository.addUser(dto.getChannelId(),dto.getUserId());
//    }
//}
