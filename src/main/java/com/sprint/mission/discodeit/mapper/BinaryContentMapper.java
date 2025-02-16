package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.util.BinaryContentUtil;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

//@Slf4j
//@Component
//public class BinaryContentMapper {
//  public BinaryContent toProfilePicture(MultipartFile mFile, String userId) throws IOException {
//    return new BinaryContent.BinaryContentBuilder(
//        userId,
//        mFile.getOriginalFilename(),
//        mFile.getContentType(),
//        mFile.getSize(),
//        mFile.getBytes())
//        .isProfilePicture()
//        .build();
//  }
//
//}

@Mapper(componentModel = "spring", imports = BinaryContentUtil.class)
public interface BinaryContentMapper {
  @Mapping(source = "mFile.originalFilename", target = "fileName")
  @Mapping(source = "mFile.contentType", target = "fileType")
  @Mapping(source = "mFile.size", target = "fileSize")
  @Mapping(target = "data", expression = "java(BinaryContentUtil.convertToBytes(mFile))")
  @Mapping(source = "userId", target = "userId")
  @Mapping(target = "isProfilePicture", expression = "java(true)")
  @Mapping(target = "messageId", ignore = true)
  @Mapping(target = "channelId", ignore = true)
  BinaryContent toProfilePicture(MultipartFile mFile, String userId);


  @Mapping(source = "mFile.originalFilename", target = "fileName")
  @Mapping(source = "mFile.contentType", target = "fileType")
  @Mapping(source = "mFile.size", target = "fileSize")
  @Mapping(target = "data", expression = "java(BinaryContentUtil.convertToBytes(mFile))")
  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "channelId", target = "channelId")
  @Mapping(source = "messageId", target = "messageId")
  @Mapping(target = "isProfilePicture", constant = "false")
  BinaryContent toBinaryContent(MultipartFile mFile, String userId, String channelId, String messageId);

  @IterableMapping(qualifiedByName = "toBinaryContent")
  default List<BinaryContent> fromMessageDto(CreateMessageDto dto, String messageId) {
    if (dto.getMultipart() == null || dto.getMultipart().isEmpty()) {
      return Collections.emptyList();
    }

    return dto.getMultipart().stream()
        .map(file -> toBinaryContent(file, dto.getUserId(), dto.getChannelId(), messageId))
        .toList();
  }

}
