package com.sprint.mission.discodeit.entity.channel;

import com.google.common.base.Preconditions;
import com.sprint.mission.discodeit.common.error.ErrorMessage;
import com.sprint.mission.discodeit.common.error.channel.ChannelException;
import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import com.sprint.mission.discodeit.entity.user.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Channel extends AbstractUUIDEntity {

    @NotNull
    @Size(
            min = 3, max = 50,
            message = "create channel must be between {min} and {max} : reject channel name `${validatedValue}`"
    )
    private String channelName;

    private final User creator;

    /**
     *  ==> 코드 리뷰 받고 싶은 부분
     *  필드에 참여한 유저의 객체를 참조하고 있는 자료구조를 추가하고 싶습니다. ex) private ParticipatedUser participatedUsers
     *  생각되는 문제는 순환참조로 어떻게 끊어줄 수 있을까입니다
     *  1. userId 만 저장하고 있는 자료구조 -> 데이터베이스식은 객체지향 코딩에서 거리가 먼것 같아 저는 지양합니다.
     *  2. User 객체를 담은 자료구조 -> 유저가 소유한 채널 중 해당 채널을 찾아, 채널에 대한 정보를 참조한다.
     *                              -> 채널에 참가한 유저 객체를 참조한다 .. 무한루프..
     *  3. 생각되는 문제점 : 디비는 스프링부트로 기능이 있는것으로 알지만, 파일 시스템으로 직접 구현할 경우 막막함,
     *      일단 해보지 않았지만, 어떤 방식으로 생각해야하는지 ?
     */


    private Channel(String channelName, User creator) {
        this.channelName = channelName;
        this.creator = creator;
    }

    public static Channel createOfChannelNameAndUser(
            String channelName,
            User creator
    ) {

        return new Channel(channelName, creator);
    }

    public static Channel createDefaultNameAndUser(User user) {

        return new Channel("Default Channel Name", user);
    }

    public void changeName(String newName, User user) {
        checkCreatorEqualsOrThrow(user);

        channelName = newName;
        updateStatusAndUpdateAt();
    }

    public void deleteChannel(User user) {
        checkCreatorEqualsOrThrow(user);

        updateUnregistered();
    }

    public boolean isStatusNotUnregisteredAndEqualsTo(String channelName) {
        return isNotUnregistered() && this.channelName.equals(channelName);
    }

    public String getChannelName() {
        return channelName;
    }

    private void checkCreatorEqualsOrThrow(User user) {
        var isNotCreator = isNotCreator(user);

        if (isNotCreator) {
            throw ChannelException.ofErrorMessageAndCreatorName(
                    ErrorMessage.CHANNEL_NOT_EQUAL_CREATOR,
                    user.getName()
            );
        }
    }

    private boolean isNotCreator(User user) {
        Preconditions.checkNotNull(user);
        return !creator.equals(user);
    }

    public String getCreator() {
        return creator.getName();
    }

    @Override
    public String toString() {
        var format =
                String.format(
                "channel info = [channel name = %s creator = %s, createAt = %d, updateAt = %d, status = %s]",
                        channelName,
                        creator.getName(),
                        getCreateAt(),
                        getUpdateAt().orElse(0L),
                        getStatus().toString()
        );
        return format;
    }
}
