package discodeit.service.jcf;

import discodeit.entity.Channel;
import discodeit.entity.User;
import discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    @Override
    public void createChannel(String name, String introduction, User owner) {

    }

    @Override
    public UUID getId(Channel channel) {
        return null;
    }

    @Override
    public long getCreatedAt(Channel channel) {
        return 0;
    }

    @Override
    public long getUpdatedAt(Channel channel) {
        return 0;
    }

    @Override
    public void getInfo(Channel channel) {

    }

    @Override
    public String getName(Channel channel) {
        return "";
    }

    @Override
    public String getIntroduction(Channel channel) {
        return "";
    }

    @Override
    public User getOwner(Channel channel) {
        return null;
    }

    @Override
    public List<User> getParticipants(Channel channel) {
        return List.of();
    }

    @Override
    public void updateName(Channel channel, String name) {

    }

    @Override
    public void updateIntroduction(Channel channel, String introduction) {

    }

    @Override
    public void updateParticipants(Channel channel, User user) {

    }

    @Override
    public void deleteChannel(Channel channel) {

    }

    @Override
    public void deleteParticipant(Channel channel, User user) {

    }
}
