package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService extends BinaryContentMapper implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  /*
  설계 :
  1. BinaryContent 엔티티 : 실제 byte 객체 x
  2. 로컬 디렉토리 파일 : 실제 byte 객체 o
  3. 클라이언트에게 반환할 때는 실제 byte 객체를 줘야되잖아 -> BinaryContentDto로 감싸고, 여기에는 실제 byte 객체 있음
   */
  @Transactional
  @Override
  public BinaryContentDto create(UUID contentId, MultipartFile file) {

    if (!binaryContentRepository.existsById(contentId)) {
      throw new NoSuchElementException("파일이 첨부되지 않았습니다. 파일을 첨부해주세요");
    }

    byte[] data = null;
    try {
      data = file.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    binaryContentStorage.put(contentId, data);

    BinaryContent binaryContent = BinaryContent.builder()
        .fileName(file.getOriginalFilename()) // id는 @GeneratedValue -> 자동생성되기 때문에 안적어도 됨
        .size((int) file.getSize())
        .contentType(file.getContentType())
        .build();
    return toDto(binaryContentRepository.save(binaryContent));
  }

  @Override
  public BinaryContentDto find(UUID contentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(contentId)
        .orElseThrow(() -> new NoSuchElementException("해당 파일이 존재하지 않습니다."));
    return toDto(binaryContent);
  }


  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> contentIds) {
    List<BinaryContent> list = binaryContentRepository.findAllByIdIn(contentIds);
    List<BinaryContentDto> dtoList = new ArrayList<>();
    for (BinaryContent binaryContent : list) {
      toDto(binaryContent);
      dtoList.add(toDto(binaryContent));
    }
    return dtoList;
  }

  @Transactional
  @Override
  public void delete(UUID contentId) {
    if (!binaryContentRepository.existsById(contentId)) {
      throw new NoSuchElementException("해당 파일이 존재하지 않습니다.");
    }
    binaryContentRepository.deleteById(contentId);
  }
}
