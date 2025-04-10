package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface MessageService extends CRUDService<MessageRequest, MessageResponse> {
    MessageResponse messageCreate(MessageRequest request, MultipartFile[] files) throws IOException;
    List<MessageResponse> channelMessageReadAll(UUID channelId);
}
