package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.validation.ValidateBinaryContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class BasicBinaryContentServiceTest {

    @InjectMocks
    BasicBinaryContentService basicBinaryContentService;

    @Mock
    BinaryContentRepository binaryContentRepository;

    @BeforeEach
    void setUp(){
    }

    @Test
    public void create_Success(){
        // given
        UUID userId = UUID.randomUUID();
        UUID messageId = UUID.randomUUID();
        byte[] content = {1, 3, 2};
        BinaryContentCreateRequest request = new BinaryContentCreateRequest(userId, messageId, content);
        BinaryContent binaryContent = new BinaryContent(request.userId(), request.messageId(), request.content());

        // when
        when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(binaryContent);

        BinaryContentDto result = basicBinaryContentService.create(request);

        // then
        assertNotNull(result);
        assertEquals(binaryContent.getUserId(), result.userId());
        assertEquals(binaryContent.getMessageId(), result.messageId());
        assertEquals(binaryContent.getContent(), result.content());

        verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
    }

    @Test
    public void findByContentId_Success(){
        // given
        BinaryContent binaryContent = new BinaryContent(UUID.randomUUID(), UUID.randomUUID(), new byte[]{1, 2, 3});

        // when
        when(binaryContentRepository.findByContentId(any(UUID.class))).thenReturn(Optional.of(binaryContent));

        BinaryContentDto result = basicBinaryContentService.findByContentId(binaryContent.getId());

        // then
        assertNotNull(result);
        assertEquals(binaryContent.getUserId(), result.userId());
        assertEquals(binaryContent.getMessageId(), result.messageId());
        assertEquals(binaryContent.getContent(), result.content());
        verify(binaryContentRepository, times(1)).findByContentId(any(UUID.class));
    }

    @Test
    public void findByContentId_BinaryContentNotFound(){
        // given
        UUID contentId = UUID.randomUUID();

        // when
        when(binaryContentRepository.findByContentId(any(UUID.class))).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicBinaryContentService.findByContentId(contentId));
    }

    @Test
    public void findAllByUserId_Success(){
        // given
        UUID userID = UUID.randomUUID();
        List<BinaryContent> binaryContents = List.of(
                new BinaryContent(userID, UUID.randomUUID(), new byte[]{1, 2, 3}),
                new BinaryContent(userID, UUID.randomUUID(), new byte[]{4, 5, 6})
        );

        // when
        when(binaryContentRepository.findAllByUserId(any(UUID.class))).thenReturn(binaryContents);

        List<BinaryContentDto> results = basicBinaryContentService.findAllByUserId(userID);

        // then
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(results.get(0).contentId(), binaryContents.get(0).getId());
        assertEquals(results.get(1).contentId(), binaryContents.get(1).getId());
        verify(binaryContentRepository, times(1)).findAllByUserId(any(UUID.class));
    }

    @Test
    public void findAllByMessageId_Success(){
        // given
        UUID userId = UUID.randomUUID();
        UUID messageId = UUID.randomUUID();
        List<BinaryContent> binaryContents = List.of(
                new BinaryContent(userId, messageId, new byte[]{1, 2, 3}),
                new BinaryContent(userId, messageId, new byte[]{4, 5, 6})
        );

        // when
        when(binaryContentRepository.findByAllMessageId(any(UUID.class))).thenReturn(binaryContents);

        List<BinaryContentDto> results = basicBinaryContentService.findByAllMessageId(messageId);

        // then
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(binaryContents.get(0).getId(), results.get(0).contentId());
        assertEquals(binaryContents.get(1).getId(), results.get(1).contentId());
        verify(binaryContentRepository, times(1)).findByAllMessageId(any(UUID.class));
    }

    @Test
    public void deleteByContentId_Success(){
        // given
        UUID contentId = UUID.randomUUID();

        // when
        basicBinaryContentService.deleteByContentId(contentId);

        // then
        verify(binaryContentRepository, times(1)).deleteByContentId(contentId);
    }

    @Test
    public void deleteByUserId_Success(){
        // given
        UUID userId = UUID.randomUUID();

        // when
        basicBinaryContentService.deleteByUserId(userId);

        // then
        verify(binaryContentRepository, times(1)).deleteByUserId(userId);
    }

    @Test
    public void deleteByMessageId(){
        // given
        UUID messageId = UUID.randomUUID();

        // when
        basicBinaryContentService.deleteByMessageId(messageId);

        // then
        verify(binaryContentRepository).deleteByMessageId(messageId);
    }
}