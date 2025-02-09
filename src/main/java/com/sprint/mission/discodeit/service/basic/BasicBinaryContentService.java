package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final UserService userService;
    private final MessageService messageService;

    @Override
    public UUID create(BinaryContentCreateDTO binaryContentCreateDTO) {
        User findUser = userService.read(binaryContentCreateDTO.getUserId());
        Message findMessage = messageService.read(binaryContentCreateDTO.getMessageId());

        BinaryContent binaryContent = new BinaryContent(findUser.getId(), findMessage.getId(),
                binaryContentCreateDTO.getData(),
                binaryContentCreateDTO.getContentType(),
                binaryContentCreateDTO.getSize());

        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent find(UUID id) {

        BinaryContent findBinaryContent = binaryContentRepository.findOne(id);
        Optional.ofNullable(findBinaryContent)
                .orElseThrow(() -> new NoSuchElementException("해당 BinaryContent 가 없습니다."));
        return findBinaryContent;
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids);
    }

    @Override
    public UUID delete(UUID id) {
        return binaryContentRepository.delete(id);
    }
}
