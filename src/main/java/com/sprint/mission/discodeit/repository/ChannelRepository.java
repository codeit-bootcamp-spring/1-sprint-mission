package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.jcf.JCF_Message;
import com.sprint.mission.discodeit.repository.jcf.JCF_channel;
import com.sprint.mission.discodeit.repository.jcf.JCF_user;
import java.util.List;
import java.util.UUID;

public interface ChannelRepository {

  void creat(Channel channel);

  void delete(UUID channelId);

  void update(UUID channelId, String title);

  UUID findByTitle(String title);

  List<Channel> findByAll();
}
