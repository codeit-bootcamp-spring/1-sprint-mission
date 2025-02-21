package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.dto.binary_content.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BinaryContentService {
  BinaryContent create(CreateBinaryContentDto dto);
  BinaryContent find(String id);
  List<BinaryContent> findByMessageId(String messageId);
  List<BinaryContent> findAllByIdIn(List<String> ids);
  Map<String, BinaryContent> mapUserToBinaryContent(Set<String> userIds);
  void delete(String id);
  void deleteByMessageId(String messageId);
  List<BinaryContentDto> saveBinaryContentsForMessage(CreateMessageDto messageDto, String messageId);
  List<BinaryContent> updateBinaryContentForMessage(Message message, String userId, List<BinaryContentDto> binaryContentDtos);
  Map<String, List<BinaryContent>> getBinaryContentsFilteredByChannelAndGroupedByMessage(String channelId);
}
