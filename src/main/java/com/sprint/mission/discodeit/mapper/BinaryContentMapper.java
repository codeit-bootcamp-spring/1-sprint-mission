package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.util.BinaryContentUtil;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
    ,imports = BinaryContentUtil.class, builder = @Builder(disableBuilder = false))
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


  @Mapping(target = "id", source = "id")
  @Mapping(target = "size", source = "fileSize")
  @Mapping(target = "contentType", source = "fileType")
  @Mapping(target = "bytes", expression = "java(BinaryContentUtil.convertToBase64(content))")
  BinaryContentDto toDto(BinaryContent content);

  List<BinaryContentDto> toDtoList(List<BinaryContent> contents);
}
