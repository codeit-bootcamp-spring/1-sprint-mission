package com.sprint.mission.discodeit.entity.message;

public interface Sender<T, V> {

    void sendMessage(T sender, V receiver, String message);
}
