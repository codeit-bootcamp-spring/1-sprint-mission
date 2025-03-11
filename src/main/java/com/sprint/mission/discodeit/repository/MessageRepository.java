package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {


  //channelId로 message삭제
  void deleteByChannelId(UUID channelId);

  //채널에 해당하는 메시지 리스트 반환
  List<Message> findByChannelId(UUID channelId);


}
