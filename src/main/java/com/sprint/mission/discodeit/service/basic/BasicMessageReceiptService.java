package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageReceiptCreateDTO;
import com.sprint.mission.discodeit.dto.MessageReceiptDTO;
import com.sprint.mission.discodeit.dto.MessageReceiptUpdateDTO;
import com.sprint.mission.discodeit.entity.MessageReceipt;
import com.sprint.mission.discodeit.repository.MessageReceiptRepository;
import com.sprint.mission.discodeit.service.MessageReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BasicMessageReceiptService implements MessageReceiptService {

    private final MessageReceiptRepository receiptRepository;

    @Override
    public MessageReceiptDTO create(MessageReceiptCreateDTO createDTO) {
        MessageReceipt receipt = new MessageReceipt(
                createDTO.getMessageId(),
                createDTO.getReceiverId(),
                createDTO.getChannelId()
        );

        receiptRepository.save(receipt);
        log.info("✅ 메시지 수신 정보 생성 완료: {}", receipt);

        return new MessageReceiptDTO(
                receipt.getId(),
                receipt.getMessageId(),
                receipt.getReceiverId(),
                receipt.getChannelId(),
                receipt.getReceivedAt()
        );
    }

    @Override
    public void update(UUID receiptId, MessageReceiptUpdateDTO updateDTO) {
        Optional<MessageReceipt> optionalReceipt = receiptRepository.findById(receiptId);

        if (optionalReceipt.isPresent()) {
            MessageReceipt receipt = optionalReceipt.get();
            receipt.setReceivedAt(updateDTO.getReceivedAt()); // ✅ 읽음 시간 업데이트
            receiptRepository.save(receipt);
            log.info("✅ 메시지 수신 정보 업데이트 완료: {}", receiptId);
        } else {
            log.warn("❌ 메시지 수신 정보 업데이트 실패 - 존재하지 않음: {}", receiptId);
        }
    }

    @Override
    public List<MessageReceiptDTO> getReceiptsByUser(UUID userId) {
        return receiptRepository.findAllByReceiverId(userId).stream()
                .map(receipt -> new MessageReceiptDTO(receipt.getId(), receipt.getMessageId(), receipt.getReceiverId(), receipt.getChannelId(), receipt.getReceivedAt()))
                .collect(Collectors.toList());
    }
}
