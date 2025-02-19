package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    @Autowired
    private BinaryContentRepository binaryContentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public BinaryContent createProfile(BinaryContentCreateRequestDto request) {
        if (!userRepository.existsById(request.getUserId())) {
            throw new NoSuchElementException("User not found");
        }
        BinaryContent binaryContent = null;
        try {
            String contentType=request.getMultipartFile().getContentType();
            binaryContent = new BinaryContent(request.getUserId(), null, request.getMultipartFile().getBytes(),contentType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        binaryContentRepository.save(binaryContent);
        return binaryContent;
    }

    @Override
    public void createMessage(BinaryContentCreateRequestDto request)  {
        if (!messageRepository.existsById(request.getMessageId())) {
            throw new NoSuchElementException("Message not found");
        }

        BinaryContent binaryContent = null;
        String contentType=request.getMultipartFile().getContentType();
        try {
            binaryContent = new BinaryContent(null, request.getMessageId(), request.getMultipartFile().getBytes(), contentType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent find(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent id not found"));
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

    @Override
    public List<BinaryContent> findAll() {
        return binaryContentRepository.findAll() ;
    }
}
