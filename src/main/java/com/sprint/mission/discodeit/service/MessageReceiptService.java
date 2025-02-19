package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageReceiptCreateDTO;
import com.sprint.mission.discodeit.dto.MessageReceiptDTO;
import com.sprint.mission.discodeit.dto.MessageReceiptUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface MessageReceiptService {
    MessageReceiptDTO create(MessageReceiptCreateDTO createDTO);
    void update(UUID receiptId, MessageReceiptUpdateDTO updateDTO);
    List<MessageReceiptDTO> getReceiptsByUser(UUID userId);
}
