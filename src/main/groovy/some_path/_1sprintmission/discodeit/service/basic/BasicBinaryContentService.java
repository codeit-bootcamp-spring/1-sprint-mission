package some_path._1sprintmission.discodeit.service.basic;

import some_path._1sprintmission.discodeit.dto.BinaryContentDTO;
import some_path._1sprintmission.discodeit.entiry.BinaryContent;
import some_path._1sprintmission.discodeit.entiry.Message;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.repository.BinaryContentRepository;
import some_path._1sprintmission.discodeit.repository.MessageRepository;
import some_path._1sprintmission.discodeit.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicBinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public BasicBinaryContentService(BinaryContentRepository binaryContentRepository, UserRepository userRepository, MessageRepository messageRepository) {
        this.binaryContentRepository = binaryContentRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    // ✅ 생성
    public BinaryContent create(BinaryContentDTO dto) {
        User user = userRepository.findById(dto.getUploadedById())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = messageRepository.findById(dto.getMessageId())
                .orElseThrow(() -> new RuntimeException("Message not found"));

        BinaryContent binaryContent = new BinaryContent(dto.getData(), dto.getContentType(), user, message);
        return binaryContentRepository.save(binaryContent);
    }

    // ✅ id로 조회
    public Optional<BinaryContent> findById(UUID id) {
        return binaryContentRepository.findById(id);
    }

    // ✅ id 목록으로 조회
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids);
    }

    // ✅ id로 삭제
    public void delete(UUID id) {
        binaryContentRepository.deleteById(id);
    }
}

