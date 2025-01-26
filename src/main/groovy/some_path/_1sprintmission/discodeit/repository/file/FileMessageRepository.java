package some_path._1sprintmission.discodeit.repository.file;

import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.Message;
import some_path._1sprintmission.discodeit.entiry.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileMessageRepository {

    private final String filePath;

    public FileMessageRepository(String filePath) {
        this.filePath = filePath;
    }

    // Save a message to a file
    public void saveMessage(Channel channel, Message message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(serializeMessage(channel, message));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Find all messages for a specific channel
    public List<Message> findMessagesByChannel(Channel channel) {
        List<Message> messages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Message message = parseMessage(line, channel);
                if (message != null) {
                    messages.add(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // Update a message content
    public void updateMessage(Channel channel, Message oldMessage, String newContent) {
        List<String> updatedLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Message message = parseMessage(line, channel);
                if (message != null && message.equals(oldMessage)) {
                    updatedLines.add(serializeMessage(channel, message));
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Delete a specific message
    public void deleteMessage(Channel channel, Message messageToDelete) {
        List<String> updatedLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Message message = parseMessage(line, channel);
                if (message != null && !message.equals(messageToDelete)) {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Serialize a message to a string for file storage
    private String serializeMessage(Channel channel, Message message) {
        return channel.getName() + "|" +
                message.getSender().getUsername() + "," +
                message.getSender().getUserEmail() + "," +
                message.getSender().getUserPhone() + "|" +
                message.getContent() + "|" +
                message.getSentAt();
    }

    // Parse a message string back to a Message object
    private Message parseMessage(String line, Channel channel) {
        String[] parts = line.split("\\|");
        if (parts.length < 4) return null;

        String channelName = parts[0];
        if (!channel.getName().equals(channelName)) return null;

        String[] senderParts = parts[1].split(",");
        if (senderParts.length < 3) return null;

        User sender = new User(senderParts[0], senderParts[1], senderParts[2]);
        String content = parts[2];
        long sentAt = Long.parseLong(parts[3]);

        Message message = new Message(sender, content);
        return message;
    }
}

