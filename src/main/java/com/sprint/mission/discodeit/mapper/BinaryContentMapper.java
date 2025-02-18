package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.util.BinaryContentUtil;
import lombok.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = BinaryContentUtil.class)
public interface BinaryContentMapper {

  @Mapping(source = "file.originalFilename", target = "fileName")
  @Mapping(source = "file.contentType", target = "fileType")
  @Mapping(source = "file.size", target = "fileSize")
  @Mapping(target = "data", expression = "java(BinaryContentUtil.convertToBytes(file))")
  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "channelId", target = "channelId")
  @Mapping(source = "messageId", target = "messageId")
  @Mapping(target = "isProfilePicture", constant = "false")
  BinaryContent toMessageBinaryContent(MultipartFile file, String userId, String channelId, String messageId);

  @Mapping(source = "file.originalFilename", target = "fileName")
  @Mapping(source = "file.contentType", target = "fileType")
  @Mapping(source = "file.size", target = "fileSize")
  @Mapping(target = "data", expression = "java(BinaryContentUtil.convertToBytes(file))")
  @Mapping(source = "userId", target = "userId")
  @Mapping(target = "isProfilePicture", constant = "true")
  @Mapping(target = "messageId", ignore = true)
  @Mapping(target = "channelId", ignore = true)
  BinaryContent toProfileBinaryContent(MultipartFile file, String userId);

  default List<BinaryContent> fromMessageFiles(List<MultipartFile> files, String userId, String channelId, String messageId) {
    return files == null || files.isEmpty() ? Collections.emptyList()
        : files.stream()
        .map(file -> toMessageBinaryContent(file, userId, channelId, messageId))
        .collect(Collectors.toList());
  }
}
