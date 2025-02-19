package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Override
    public BinaryContentResponse create(BinaryContentCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Message message = messageRepository.findById(request.getMessageId())
                .orElseThrow(() -> new NoSuchElementException("Message not found"));

        MultipartFile file = request.getFile();
        BinaryContent binaryContent;
        try {
            binaryContent = new BinaryContent(user, message, file.getBytes(), file.getContentType());
            binaryContent = binaryContentRepository.save(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }

        return new BinaryContentResponse(binaryContent);
    }

    @Override
    public BinaryContentResponse find(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent not found"));
        return new BinaryContentResponse(binaryContent);
    }

    @Override
    public List<BinaryContentResponse> findAllByIdIn(List<UUID> ids) {
        List<BinaryContent> binaryContents = binaryContentRepository.findAllByIdIn(ids);
        return binaryContents.stream().map(BinaryContentResponse::new).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) {
            throw new NoSuchElementException("BinaryContent not found");
        }
        binaryContentRepository.deleteById(id);
    }
}
