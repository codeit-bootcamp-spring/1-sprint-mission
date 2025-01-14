package some_path._1sprintmission.Channel;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.service.jcf.JCFChannelService;

@SpringBootApplication
public class CreateChannel {
    public static void main(String[] args) {

        JCFChannelService channelService = new JCFChannelService();

        Channel channel = new Channel("Channel1");
        channelService.createChannel(channel);
        System.out.println(channelService.getChannel(channel.getId()));
        channel.toString();
    }
}
