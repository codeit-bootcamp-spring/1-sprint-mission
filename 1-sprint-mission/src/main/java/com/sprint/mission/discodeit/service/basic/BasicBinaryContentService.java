package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.file.FileBinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class BasicBinaryContentService {
    FileBinaryContentRepository repository;

    @Autowired
    public BasicBinaryContentService(FileBinaryContentRepository repository) {
        this.repository = repository;
    }

    public void createMessageContent(MessageDto messageDto) throws IOException, ClassNotFoundException {
        repository.saveMessageContent(messageDto);
    }

    public void createUserContent(UserDto userDto) throws IOException, ClassNotFoundException {
        repository.saveUserContent(userDto);
    }

    public BinaryContent findByMessage (MessageDto messageDto) throws IOException, ClassNotFoundException {
        return repository.findMessageContent(messageDto.getMessageId());
    }

    public BinaryContent findByUser(UserDto userDto) throws IOException, ClassNotFoundException {
        return repository.findUserContent(userDto.getId());
    }

    public List<BinaryContent> findAll() throws IOException, ClassNotFoundException {
        return repository.findAll();
    }

    public void deleteUserContent(UserDto userDto) throws IOException, ClassNotFoundException {
        repository.deleteUser(userDto.getId());
    }

    public void deleteMessageContent(MessageDto messageDto) throws IOException, ClassNotFoundException {
        repository.deleteMessage(messageDto.getMessageId());
    }
}
