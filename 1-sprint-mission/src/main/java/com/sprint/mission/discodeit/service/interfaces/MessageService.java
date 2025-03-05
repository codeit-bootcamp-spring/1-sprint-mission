package com.sprint.mission.discodeit.service.interfaces;
import com.sprint.mission.discodeit.dto.MessageDto;

import java.io.IOException;

public interface MessageService {
    public void showInfoMessage(MessageDto message) throws IOException, ClassNotFoundException;
    public void saveMessage(MessageDto messageDto) throws IOException, ClassNotFoundException;
    public void deleteMessage(MessageDto message) throws IOException, ClassNotFoundException;
    public void modifyMessage(MessageDto message) throws IOException, ClassNotFoundException;
}
