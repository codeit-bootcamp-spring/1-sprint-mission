package some_path._1sprintmission.discodeit.repository;

import org.springframework.stereotype.Repository;
import some_path._1sprintmission.discodeit.entiry.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {

    BinaryContent save(BinaryContent binaryContent);
    Optional<BinaryContent> findByMessageId(UUID messageId);
    void deleteByMessageId(UUID messageId);
    Optional<BinaryContent> findById(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> ids);
    void deleteById(UUID id);
}
