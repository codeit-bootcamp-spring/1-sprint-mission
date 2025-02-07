package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RepositoryMapConfig {

  private final BinaryContentRepository binaryContentRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  @Bean
  public Map<Class<?>, BaseRepository<?, ?>> baseRepositoryMap() {
    Map<Class<?>, BaseRepository<?, ?>> map = new HashMap<>();

    // 엔티티 클래스를 key로, 해당 엔티티를 담당하는 Repository를 value로 등록

    map.put(BinaryContent.class, binaryContentRepository);
    map.put(Channel.class, channelRepository);
    map.put(Message.class, messageRepository);
    map.put(ReadStatus.class, readStatusRepository);
    map.put(User.class, userRepository);
    map.put(UserStatus.class, userStatusRepository);

    return map;
  }
}
