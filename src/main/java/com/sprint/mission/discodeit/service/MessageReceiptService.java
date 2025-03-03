package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageReceiptCreateRequest;
import com.sprint.mission.discodeit.dto.MessageReceiptResponse;
import com.sprint.mission.discodeit.dto.MessageReceiptUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface MessageReceiptService {
    MessageReceiptResponse create(MessageReceiptCreateRequest createDTO);
    void update(UUID receiptId, MessageReceiptUpdateRequest updateDTO);
    List<MessageReceiptResponse> getReceiptsByUser(UUID userId);
}
