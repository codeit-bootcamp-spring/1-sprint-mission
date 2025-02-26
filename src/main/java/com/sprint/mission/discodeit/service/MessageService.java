package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;

import java.time.Instant;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {


  //생성
  MessageResponseDto create(CreateMessageDto createMessageDto);

  MessageResponseDto create(CreateMessageDto createMessageDto, List<MultipartFile> files);

  //모두 읽기
  List<MessageResponseDto> findAll();

  //읽기
  MessageResponseDto findById(String messageId);

  //다건 조회 - 내용
  List<MessageResponseDto> findAllContainsContent(String content);

  //다건 조회 - 작성자
  List<MessageResponseDto> findAllBySenderId(String senderId);

  //다건 조회 - 날짜
  List<MessageResponseDto> findAllByCreatedAt(Instant createdAt);

  //다건 조회 - 특정 채널
  List<MessageResponseDto> findAllByChannelId(String channelId);

  //수정
  MessageResponseDto updateMessage(String messageId, UpdateMessageDto updateMessageDto);

  //삭제
  boolean delete(String messageId, String userId);
}
