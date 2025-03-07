package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;

import java.time.Instant;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {


  //생성
  MessageDto create(CreateMessageDto createMessageDto);

  MessageDto create(CreateMessageDto createMessageDto, List<MultipartFile> files);

  //모두 읽기
  List<MessageDto> findAll();

  //읽기
  MessageDto findById(String messageId);

  //다건 조회 - 내용
  List<MessageDto> findAllContainsContent(String content);

  //다건 조회 - 작성자
  List<MessageDto> findAllBySenderId(String senderId);

  //다건 조회 - 날짜
  List<MessageDto> findAllByCreatedAt(Instant createdAt);

  //다건 조회 - 특정 채널
  List<MessageDto> findAllByChannelId(String channelId, String userId);

  //수정
  MessageDto updateMessage(String messageId, UpdateMessageDto updateMessageDto);

  //삭제
  boolean delete(String messageId, String userId);
}
