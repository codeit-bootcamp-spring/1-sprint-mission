package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {
//
//    Message save(Message message);
//    Message findById(UUID id);
//    Map<UUID, Message> load();
//    void delete(UUID id);

  void deleteByChannelId(UUID id);

  List<Message> findByChannelId(UUID channelId); //채널에 해당하는 메시지 리스트 반환

//  @Override
//  public List<Message> findByChannelId(UUID channelId) {
//    List<Message> messageFindByChannelList = messageList.values().stream()
//        .filter(message -> message.getChannelId().equals(channelId))
//        .toList();
//    return messageFindByChannelList;
//  }
//  @Override
//  public void deleteByChannelId(UUID id) {
//    List<Message> toDeleteMessageList = findByChannelId(id);
//    for (Message toDeleteMessage : toDeleteMessageList) {
//      messageList.remove(id);
//    }
//  }


}
