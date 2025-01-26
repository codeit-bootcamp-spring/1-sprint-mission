package some_path._1sprintmission


import org.springframework.boot.autoconfigure.SpringBootApplication
import some_path._1sprintmission.discodeit.entiry.Channel
import some_path._1sprintmission.discodeit.entiry.Message
import some_path._1sprintmission.discodeit.entiry.User
import some_path._1sprintmission.discodeit.entiry.enums.DiscodeStatus
import some_path._1sprintmission.discodeit.service.UserService
import some_path._1sprintmission.discodeit.service.file.FileChannelService
import some_path._1sprintmission.discodeit.service.file.FileUserService
import some_path._1sprintmission.discodeit.service.jcf.JCFChannelService
import some_path._1sprintmission.discodeit.service.jcf.JCFMessageService
import some_path._1sprintmission.discodeit.service.jcf.JCFUserService

@SpringBootApplication
public class Application {
    private static final String CHANNEL_FILE_PATH  = "src/main/resources/tmp/channel.txt";
    public static void main(String[] args) {
        testSaveAndLoadSingleChannel();
        System.out.println("====================================");
        testSaveAndLoadMultipleChannels();
        System.out.println("====================================");
        testFileNotExists();
        System.out.println("====================================");
        testEmptyFile();
    }

    // Test 1: Save and load a single channel
    public static void testSaveAndLoadSingleChannel() {
        System.out.println("Running Test 1: Save and Load a Single Channel");

        Channel channel = new Channel("General");
        User testUser = new User("Alice", "alice@example.com", "010-1234-5678")
        channel.addMember(testUser);
        channel.addMessage(new Message(testUser, "Hello Random!"));

        User testUser2 = new User("Random", "Random@example.com", "010-1234-5678")
        channel.addMember(testUser2);
        channel.addMessage(new Message(testUser2, "Hello Alice!"));

        // Save the channel
        FileChannelService.saveChannelListToFile(CHANNEL_FILE_PATH, List.of(channel));

        // Load channels from the file
        List<Channel> loadedChannels = FileChannelService.loadChannelListFromFile(CHANNEL_FILE_PATH);

        // Validate the loaded data
        if (loadedChannels.size() == 1) {
            Channel loadedChannel = loadedChannels.get(0);
            System.out.println("Loaded Channel: " + loadedChannel);
            System.out.println("Users in Channel:");
            for (User user : loadedChannel.getMembers()) {
                System.out.println("- " + user);
            }

            // Check and print the Messages
            System.out.println("Messages in Channel:");
            for (Message message : loadedChannel.getMessages()) {
                System.out.println("- Sender: " + message.getSender().getUsername() + ", Content: " + message.getContent());
            }


            if ("General".equals(loadedChannel.getName())) {
                System.out.println("Test 1 Passed: Channel loaded successfully.");
            } else {
                System.out.println("Test 1 Failed: Channel data mismatch.");
            }
        } else {
            System.out.println("Test 1 Failed: Incorrect number of channels loaded.");
        }
    }

    // Test 2: Save and load multiple channels
    public static void testSaveAndLoadMultipleChannels() {
        System.out.println("Running Test 2: Save and Load Multiple Channels");

        Channel channel1 = new Channel("General");
        User testUser = new User("Alice", "alice@example.com", "010-1234-5678");
        testUser.joinChannel(channel1);
        channel1.addMessage(new Message(testUser, "Hello, World!"));


        Channel channel2 = new Channel("Random");
        User testUser2 = new User("Random", "Random@example.com", "010-1234-5678");
        testUser2.joinChannel(channel2);
        channel2.addMessage(new Message(testUser2, "Hello, Random!"));

        // Save the channels
        FileChannelService.saveChannelListToFile(CHANNEL_FILE_PATH, List.of(channel1, channel2));

        // Load channels from the file
        List<Channel> loadedChannels = FileChannelService.loadChannelListFromFile(CHANNEL_FILE_PATH);

        // Validate the loaded data
        if (loadedChannels.size() == 2) {
            System.out.println("Loaded Channels: " + loadedChannels);
            System.out.println("Test 2 Passed: Multiple channels loaded successfully.");
        } else {
            System.out.println("Test 2 Failed: Incorrect number of channels loaded.");
        }
    }

    // Test 3: Handle non-existent file
    public static void testFileNotExists() {
        System.out.println("Running Test 3: Handle Non-Existent File");

        // Ensure the file does not exist
        File file = new File(CHANNEL_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }

        List<Channel> channels = FileChannelService.loadChannelListFromFile(CHANNEL_FILE_PATH);
        if (channels.isEmpty()) {
            System.out.println("Test 3 Passed: No channels loaded from non-existent file.");
        } else {
            System.out.println("Test 3 Failed: Channels loaded from non-existent file.");
        }
    }

    // Test 4: Handle empty file
    public static void testEmptyFile() {
        System.out.println("Running Test 4: Handle Empty File");

        // Create an empty file
        File file = new File(CHANNEL_FILE_PATH);
        try {
            if (file.createNewFile()) {
                System.out.println("Empty file created for test.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Channel> channels = FileChannelService.loadChannelListFromFile(CHANNEL_FILE_PATH);
        if (channels.isEmpty()) {
            System.out.println("Test 4 Passed: No channels loaded from empty file.");
        } else {
            System.out.println("Test 4 Failed: Channels loaded from empty file.");
        }
    }
}
