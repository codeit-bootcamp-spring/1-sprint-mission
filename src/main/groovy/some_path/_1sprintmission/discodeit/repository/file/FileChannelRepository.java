package some_path._1sprintmission.discodeit.repository.file;

import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.Message;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileChannelRepository implements ChannelRepository {

    private final String filePath;

    public FileChannelRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveAll(List<Channel> channels) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Channel channel : channels) {
                writer.write(serializeChannel(channel));
                writer.newLine();
            }
            System.out.println("Channels saved to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving channels to file.");
        }
    }

    @Override
    public List<Channel> findAll() {
        List<Channel> channels = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            System.out.println("File does not exist or is empty. Returning empty channel list.");
            return channels;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                channels.add(parseChannel(line));
            }
            System.out.println("Channels loaded from file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading channels from file.");
        }

        return channels;
    }

    private String serializeChannel(Channel channel) {
        StringBuilder sb = new StringBuilder();
        sb.append(channel.getName()).append("|");

        String userIds = channel.getMembers().stream()
                .map(user -> user.getUsername() + ":" + user.getUserEmail() + ":" + user.getUserPhone())
                .collect(Collectors.joining(","));
        sb.append(userIds).append("|");

        String messageContents = channel.getMessages().stream()
                .map(message -> message.getSender().getUsername() + ":" + message.getSender().getUserEmail() + ":" + message.getSender().getUserPhone() + ":" + message.getContent())
                .collect(Collectors.joining(","));
        sb.append(messageContents);

        return sb.toString();
    }

    private Channel parseChannel(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid channel format in file.");
        }

        String name = parts[0];
        Channel channel = new Channel(name);

        if (!parts[1].isEmpty()) {
            String[] userDetails = parts[1].split(",");
            for (String userDetail : userDetails) {
                String[] userParts = userDetail.split(":");
                if (userParts.length != 3) {
                    throw new IllegalArgumentException("Invalid user format in channel data.");
                }

                String username = userParts[0];
                String email = userParts[1];
                String phone = userParts[2];

                User user = new User(username, email, phone);
                user.joinChannel(channel);
            }
        }

        if (!parts[2].isEmpty()) {
            String[] messageContents = parts[2].split(",");
            for (String messageDetail : messageContents) {
                String[] messageParts = messageDetail.split(":");
                String senderUsername = messageParts.length > 0 ? messageParts[0] : "UnknownSender";
                String senderEmail = messageParts.length > 1 ? messageParts[1] : "unknown@gmail.com";
                String senderPhone = messageParts.length > 2 ? messageParts[2] : "010-0000-0000";
                String content = messageParts.length > 3 ? messageParts[3] : "No content";

                User sender = new User(senderUsername, senderEmail, senderPhone);
                Message message = new Message(sender, content);
                channel.addMessage(message);
            }
        }

        return channel;
    }
}
