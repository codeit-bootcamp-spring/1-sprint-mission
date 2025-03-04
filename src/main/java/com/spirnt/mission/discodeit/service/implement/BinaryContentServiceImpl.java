package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;


  @Override
  public BinaryContent create(BinaryContentCreateRequest binaryContentCreateRequest) {
    String fileName = binaryContentCreateRequest.fileName();
    byte[] bytes = binaryContentCreateRequest.bytes();
    String contentType = binaryContentCreateRequest.contentType();
    BinaryContent binaryContent = new BinaryContent(
        fileName,
        (long) bytes.length,
        contentType,
        bytes
    );
    binaryContentRepository.save(binaryContent);
    return binaryContent;
  }

  @Override
  public BinaryContent find(UUID id) {
    return binaryContentRepository.findById(id)
        .orElseThrow(
            () -> new NoSuchElementException("BinaryContent with id " + id + " not found"));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> uuids) {
    List<BinaryContent> list = new ArrayList<>();
    for (UUID id : uuids) {
      list.add(find(id));
    }
    return list;
  }

  @Override
  public void delete(UUID id) {
    binaryContentRepository.delete(id);
  }


}
