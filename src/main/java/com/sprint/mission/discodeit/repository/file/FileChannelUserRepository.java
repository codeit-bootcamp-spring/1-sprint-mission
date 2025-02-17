package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ChannelUser;
import com.sprint.mission.discodeit.repository.ChannelUserRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileChannelUserRepository implements ChannelUserRepository {
    private final Path CHANNEL_USER_DIR;
    private final Path USER_CHANNEL_DIR;
    private final String EXTENSION = ".ser";

    public FileChannelUserRepository(){
        this.CHANNEL_USER_DIR = Paths.get(System.getProperty("user.dir"),"file-data-map", "channel-users");
        this.USER_CHANNEL_DIR = Paths.get(System.getProperty("user.dir"),"file-data-map", "user-channels");

        if(Files.notExists(CHANNEL_USER_DIR)){
            try{
                Files.createDirectories(CHANNEL_USER_DIR);
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        if(Files.notExists(USER_CHANNEL_DIR)){
            try{
                Files.createDirectories(USER_CHANNEL_DIR);
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    // /channelUser-data/channel-users/{channelId}/user-{userId}.ser
    private Path resolveChannelUsersPath(UUID userId){
        return CHANNEL_USER_DIR.resolve( userId.toString() + EXTENSION);
    }
    // /channelUser-data/user-channels/{userId}/channel-{channelId}.ser
    private Path resolveUserChannelsPath(UUID channelId){
        return USER_CHANNEL_DIR.resolve(channelId.toString() + EXTENSION);
    }

    @Override
    public ChannelUser save(ChannelUser channelUser) {

        Path channelUsersPath = resolveChannelUsersPath(channelUser.getUserId());
        Path userChannelsPath = resolveUserChannelsPath(channelUser.getChannelId());

        try (
                FileOutputStream fosChannelUsers = new FileOutputStream(channelUsersPath.toFile());
                ObjectOutputStream oosChannelUsers = new ObjectOutputStream(fosChannelUsers);

                FileOutputStream fosUserChannels = new FileOutputStream(userChannelsPath.toFile());
                ObjectOutputStream oosUserChannels = new ObjectOutputStream(fosUserChannels);

        ) {
            oosChannelUsers.writeObject(channelUser);
            oosUserChannels.writeObject(channelUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return channelUser;
    }

    @Override
    public List<UUID> findUserByChannelId(UUID channelId) {
        try{
            return Files.list(CHANNEL_USER_DIR)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path->{
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)

                        ){
                            return (ChannelUser) ois.readObject();
                        }catch (IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    })
                    .filter( channelUser -> channelUser.getChannelId().equals(channelId))
                    .map(ChannelUser::getUserId)
                    .toList();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UUID> findChannelByUserId(UUID userId) {
        try{
            return Files.list(USER_CHANNEL_DIR)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path->{
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)

                                ){
                            return (ChannelUser) ois.readObject();
                        }catch (IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    })
                    .filter( channelUser -> channelUser.getUserId().equals(userId))
                    .map(ChannelUser::getChannelId)
                    .toList();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        Path channelUsersPath = CHANNEL_USER_DIR.resolve(channelId.toString());
        try{
            Files.delete(channelUsersPath);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByUserId(UUID userId) {
        Path userChannelsPath = USER_CHANNEL_DIR.resolve(userId.toString());
        try{
            Files.delete(userChannelsPath);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
