package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class BasicReadStatusService {

    FileReadStatusRepository repository;
    FileChannelRepository channelRepository;

    @Autowired
    public BasicReadStatusService(FileReadStatusRepository repository, FileChannelRepository channelRepository) {
        this.repository = repository;
        this.channelRepository = channelRepository;
    }

    public void create(ChannelDto dto) throws IOException, ClassNotFoundException {
        if(!repository.findStatus(dto.getChannelId()).isEmpty()){
            throw new RuntimeException();
        }

        repository.createStatus(dto.getChannelId(),channelRepository.joinUserIdList(dto.getChannelId()),dto.getUserId());
    }

    public Map<UUID , Boolean> findMessageStatus(MessageDto dto) throws IOException, ClassNotFoundException {
        return repository.findMessageStatus(dto.getMessageId(),dto.getChannelId());
    }

    public List<UUID> findAllByUserId(UUID uid) throws IOException, ClassNotFoundException {
        return repository.findByUserId(uid);
    }

    public void deleteStatus(ChannelDto dto) throws IOException, ClassNotFoundException {
        repository.deleteStatus(dto);
    }

    public void addUser(ChannelDto dto) throws IOException, ClassNotFoundException {
        repository.addUser(dto.getChannelId(),dto.getUserId());
    }

    public void saveMessage(MessageDto messageDto) throws IOException, ClassNotFoundException {
        repository.saveMessage(messageDto);
    }

    public void deleteMessage(MessageDto messageDto) throws IOException, ClassNotFoundException {
        repository.deleteMessage(messageDto);
    }

    public void modifyStatus(ChannelDto channelDto) throws IOException, ClassNotFoundException {
        repository.update(channelDto);
    }
}
