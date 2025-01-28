//
//package com.sprint.mission.discodeit.service.file;
//
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.service.MessageService;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//public class FileMessage implements MessageService {
//  private Path fileDirectory;
//  private FileBasic messageFileBasic;
//
//  public FileMessage(Path directory) {
//    this.fileDirectory = directory;
//    this.messageFileBasic = new FileBasic(fileDirectory);
//  }
//
//  @Override
//  public void creat(Message message) {
//    Path filePath = fileDirectory.resolve(message.getId().toString().concat(".ser"));
//
//    try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//    ) {
//      oos.writeObject(message);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Override
//  public void delete(UUID messageId) {
//    try {
//      Optional<Path> messageDelete = Files.list(fileDirectory)
//          .filter(path -> path.getFileName().toString().equals(messageId + ".ser"))
//          .findFirst();
//      if(messageDelete.isPresent()) {
//        Files.delete(messageDelete.get());
//      }
//      else {
//        System.out.println("File not found for Id");
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Override
//  public void update(UUID messageId, String messageContent) {
//    try {
//      Optional<Path> updateMessage = Files.list(fileDirectory)
//          .filter(path -> path.getFileName().toString().equals(messageId + ".ser"))
//          .findFirst();
//      if(updateMessage.isPresent()) {
//        Message message = (Message) messageFileBasic.deserialization(updateMessage.get());
//        message.updateMessage(messageContent);
//        creat(message);
//      }
//      else {
//        System.out.println("File not found for Id");
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Override
//  public List<Message> write(UUID userId, UUID channelId) {
//    List<Message> messageList = messageFileBasic.load();
//    try {
//      return messageList.stream()
//          .filter(message -> message.isChannelEqual(channelId) && message.isUserEqual(userId))
//          .map(Message::new)
//          .collect(Collectors.toList());
//    }
//    catch (Exception e) {
//      System.out.println("There is no Message with that id.");
//      return null;
//    }
//  }
//
//  @Override
//  public List<Message> getMessage(UUID channelId) {
//    List<Message> messageList = messageFileBasic.load();
//    try {
//      return messageList.stream()
//          .filter(message -> message.isChannelEqual(channelId))
//          .map(Message::new)
//          .collect(Collectors.toList());
//    }
//    catch (Exception e) {
//      System.out.println("There is no Message with that id.");
//      return null;
//    }
//  }
//
//  @Override
//  public void deleteUserMessage(UUID userID) {
//    try {
//      List<Message> messageList = messageFileBasic.load();
//      Files.list(fileDirectory)
//          .filter(path -> messageList.stream()
//              .anyMatch(message -> message.isUserEqual(userID) &&
//                  path.getFileName().toString().equals(message.getId().toString() + ".ser")))
//          .forEach(path -> {
//            try {
//              Files.delete(path);
//            } catch (IOException e) {
//              System.err.println("Deletion failed.");
//              e.printStackTrace();
//            }
//          });
//    } catch (IOException e) {
//      System.err.println("Error listing files in directory: " + fileDirectory);
//      e.printStackTrace();
//    }
//
//  }
//
//  @Override
//  public void deleteChannelMessage(UUID channelId) {
//    try {
//      List<Message> messageList = messageFileBasic.load();
//      Files.list(fileDirectory)
//          .filter(path -> messageList.stream()
//              .anyMatch(message -> message.isChannelEqual(channelId) &&
//                  path.getFileName().toString().equals(message.getId().toString() + ".ser")))
//          .forEach(path -> {
//            try {
//              Files.delete(path);
//            } catch (IOException e) {
//              System.out.println("Deletion failed.");
//              e.printStackTrace();
//            }
//          });
//    } catch (IOException e) {
//      System.err.println("Error listing files in directory: " + fileDirectory);
//      e.printStackTrace();
//    }
//  }
//}