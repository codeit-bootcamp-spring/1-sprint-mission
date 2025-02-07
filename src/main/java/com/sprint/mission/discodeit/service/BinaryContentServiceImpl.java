package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {

    @Autowired
    private BinaryContentRepository binaryContentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    //UUID userId, byte[] data
    @Override
    public void createProfile(BinaryContentCreateRequestDto request) {
        if (!userRepository.existsById(request.getUserId())) {
            throw new NoSuchElementException("User not found");
        }
        BinaryContent binaryContent = new BinaryContent(request.getUserId(), null, request.getData());
        binaryContentRepository.save(binaryContent);
    }

    @Override
    public void createMessage(BinaryContentCreateRequestDto request) {
        if (!messageRepository.existsById(request.getMessageId())) {
            throw new NoSuchElementException("Message not found");
        }

        BinaryContent binaryContent = new BinaryContent(null, request.getMessageId(), request.getData());
        binaryContentRepository.save(binaryContent);
    }

    @Override
    public Optional<BinaryContent> find(UUID id) {
        return binaryContentRepository.findById(id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids);
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.deleteById(id);
    }

    @Override
    public void deleteByMessageId(UUID id) {
        binaryContentRepository.deleteByMessageId(id);
    }

    @Override
    public void deleteByUserId(UUID id) {
        binaryContentRepository.deleteByUserId(id);
    }
}
