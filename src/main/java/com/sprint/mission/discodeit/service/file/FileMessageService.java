package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {

    private static final FileMessageService fileMessageService = new FileMessageService();
    private static final Path filePath;
    private final ChannelService channelService;

    static {
        filePath = Path.of(System.getProperty("user.dir"), "messages");
        createDirectory();
    }

    private FileMessageService() {
        channelService = FileChannelService.getInstance();
    }

    public static FileMessageService getInstance() {
        return fileMessageService;
    }

    private static void createDirectory() {
        if (!Files.exists(FileMessageService.filePath)) {
            try {
                Files.createDirectories(FileMessageService.filePath);
            } catch (IOException e) {
                throw new FileIOException("저장 디렉토리 생성 실패: " + filePath);
            }
        }
    }

    @Override
    public Message createMessage(MessageDto messageDto) {
        return writeMessageToFile(Message.of(messageDto.getWriter(), messageDto.getContent(), messageDto.getChannel()));
    }

    private Message writeMessageToFile(Message message) {
        Path path = filePath.resolve(message.getId().toString().concat(".ser"));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(message);
            return message;
        } catch (IOException e) {
            throw new FileIOException("message 저장 실패");
        }
    }

    @Override
    public Message readMessage(UUID messageId) {
        Path path = filePath.resolve(messageId.toString().concat(".ser"));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NotFoundException("등록되지 않은 message입니다.");
        }
    }

    @Override
    public List<Message> readAll() {
        try {
            return Files.list(filePath)
                    .map(path -> {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            Object data = ois.readObject();
                            return (Message) data;
                        } catch (IOException | ClassNotFoundException e) {
                            throw new FileIOException("messages 읽기 실패");
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new FileIOException("messages 읽기 실패");
        }
    }

    @Override
    public void updateMessage(UUID messageId, String content) {
        Message message = readMessage(messageId);
        message.updateContent(content);
        writeMessageToFile(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Path path = filePath.resolve(messageId.toString().concat(".ser"));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("message 삭제 실패");
            }
        }
    }
}
