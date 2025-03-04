package some_path._1sprintmission.discodeit.service;

import some_path._1sprintmission.discodeit.dto.ReadStatusCreateDTO;
import some_path._1sprintmission.discodeit.dto.ReadStatusUpdateDTO;
import some_path._1sprintmission.discodeit.entiry.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateDTO dto);
    ReadStatus find(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus update(ReadStatusUpdateDTO dto);
    void delete(UUID id);
}
