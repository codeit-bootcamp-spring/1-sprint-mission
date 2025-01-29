package com.sprint.mission.discodeit.exception;

public class SendException extends NetworkException {
    private final String sendMessage;
    public SendException(String sendMessage,String message) {
        super(message);
        this.sendMessage = sendMessage;
    }

    public String getSendMessage() {
        return sendMessage;
    }

}
