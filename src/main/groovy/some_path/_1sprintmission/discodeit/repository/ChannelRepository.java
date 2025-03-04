package some_path._1sprintmission.discodeit.repository;

import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.entiry.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    Optional<Channel> findById(UUID id);
    List<Channel> findAll();
    boolean existsById(UUID id);
    void deleteById(UUID id);

    Optional<Channel> findByInviteCode(String inviteCode);

    List<Channel> findAllByUsersContaining(User user);
}