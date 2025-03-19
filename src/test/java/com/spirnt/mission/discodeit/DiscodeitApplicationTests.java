package com.spirnt.mission.discodeit;

import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.service.MessageService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DiscodeitApplicationTests {

  @Autowired
  private MessageService messageService;

  @Test
  void contextLoads() {
  }

  @Test
  void pagingTest() {
    for (int i = 0; i < 300; i++) {
      this.messageService.create(
          new MessageCreateRequest(String.valueOf(i),
              UUID.fromString("54020981-7a63-4c64-a735-85852294dcde"),
              UUID.fromString("7de55284-3908-41e3-bf46-6d20334b5505")), null);
    }
  }

}
