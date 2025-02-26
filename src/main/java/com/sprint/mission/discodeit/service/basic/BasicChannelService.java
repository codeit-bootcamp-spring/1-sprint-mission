package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
//
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final InputHandler inputHandler;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;

  private final ReadStatusService readStatusService;
  private final ChannelUserService channelUserService;


  @Override
  public Channel createPublicChannel(ChannelPublicRequest channelPublicRequest) {

    Channel channel = new Channel(ChannelType.PUBLIC,
        channelPublicRequest.ownerId(),
        channelPublicRequest.name(),
        channelPublicRequest.description());
    channelRepository.saveChannel(channel);

    ChannelUser channelUser = new ChannelUser(channel.getId(), channelPublicRequest.ownerId());
    channelUserService.addUserToChannel(channelUser);  // owner channel의 사용자로 추가

    /* 스프린트 미션 5 심화 조건 중 API 스펙을 준수
    // channel에서 찾아오는 게 순서적으로 맞을까 고민했는데,
    // 그러면 다시 한번 찾는 작업을 해야하니 바로 request로
    ChannelPublicResponse channelPublicResponse = new ChannelPublicResponse(
        ChannelType.PUBLIC,
        channelPublicRequest.ownerId(),
        channelPublicRequest.channelName(),
        channelPublicRequest.description());

    */

    return channel;
  }

  @Override
  public Channel createPrivateChannel(ChannelPrivateRequest channelPrivateRequest) {
    Channel channel = new Channel(
        channelPrivateRequest.type(),
        channelPrivateRequest.ownerId(),
        "비공개 채널", // name과 description 속성은 생략합니다.
        "설명 없음");
    channelRepository.saveChannel(channel);

    // 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
    ReadStatusCreateRequest readStatusRequest =
        new ReadStatusCreateRequest(channelPrivateRequest.ownerId(), channel.getId(),
            Instant.now());
    readStatusService.createReadStatus(readStatusRequest);

    // 지금은 PRIVATE 채널을 만들 때 사람을 추가하고 시작해야하는지 등에 대한 말이 아무 것도 없기 때문에
    // PRIVATE 채널 조건이 더 확장되면 다른 로직 추가적으로 작성
    ChannelUser channelUser = new ChannelUser(channel.getId(), channelPrivateRequest.ownerId());
    channelUserService.addUserToChannel(channelUser);  // owner channel의 사용자로 추가

    /* 스프린트 미션 5 심화 조건 중 API 스펙을 준수
    ChannelPrivateResponse channelPrivateResponse = new ChannelPrivateResponse(
        channelPrivateRequest.type(),
        channelPrivateRequest.ownerId()
    );
    */

    return channel;
  }

  @Override
  public Collection<ChannelFindAllResponse> findAllByUserId(UUID userId) {
    // 너무 복잡한가요?
    // 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가

    List<ChannelFindAllResponse> responseChannels;

    // (1) User 가 포함돼 있는 PRIVATE 타입의 채널이 있다면
    // (2) PRIVATE 채널을 보여준다

    List<Channel> channels =
        channelUserService.findChannelByUserId(userId).stream()
            .map(channelRepository::findChannelById)
            .flatMap(Optional::stream)
            .toList();
    List<Channel> privateChannels =
        channels.stream()
            .filter(channel -> channel.getType().equals(ChannelType.PRIVATE))
            .toList();

    responseChannels =
        privateChannels.stream().map(channel -> new ChannelFindAllResponse(
            channel.getId(),
            channel.getType(),
            channel.getName(),
            channel.getDescription(),

            channelUserService.findUserByChannelId(channel.getId()),

            messageRepository.findMessagesByChannelId(channel.getId()).stream()
                .max(Comparator.comparing(Message::getCreatedAt))
                .map(Message::getCreatedAt)
                .orElse(null)
        )).toList();

    // (3) PUBLIC 채널을 전부 보여주는 메서드

    List<ChannelFindAllResponse> publicChannels =
        channelRepository.getAllChannels().stream()
            .filter(channel -> channel.getType().equals(ChannelType.PUBLIC))
            .map(channel -> new ChannelFindAllResponse(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                channelUserService.findUserByChannelId(channel.getId()),
                messageRepository.findMessagesByChannelId(channel.getId()).stream()
                    .max(Comparator.comparing(Message::getCreatedAt))
                    .map(Message::getCreatedAt)
                    .orElse(null)
            )).toList();

    return Stream.concat(responseChannels.stream(), publicChannels.stream()).toList();
  }

  @Override
  public ChannelFindResponse getChannelById(UUID id) {
    // 특정 채널을 불러오기
    Channel channel = channelRepository.findChannelById(id)
        .orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다."));

    Optional<Message> latestMessage = messageRepository.findMessagesByChannelId(id).stream()
        .max(Comparator.comparing(Message::getCreatedAt));

    return new ChannelFindResponse(
        channel.getType(),
        channel.getId(),
        channel.getOwnerId(),
        channel.getName(),
        channel.getDescription(),
        channel.getUpdatedAt(),
        channelUserService.findUserByChannelId(channel.getId()),
        latestMessage.map(Message::getCreatedAt).orElse(null)
    );
  }

  @Override
  public Channel updateChannel(UUID id, ChannelUpdateRequest channelUpdateRequest) {
    Optional<Channel> channelOptional = channelRepository.findChannelById(id);

    Channel channel = channelOptional.orElseThrow(
        () -> new NoSuchElementException("채널을 찾을 수 없습니다.")); // 전역 404

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
    } // 전역 400

    // mock은 일단 제거

    channel.updateName(channelUpdateRequest.newName());
    channel.updateDescription(channelUpdateRequest.newDescription());

    // 수정 시간 업데이트
    channel.refreshUpdateAt();

    return channelRepository.saveChannel(channel);
  }

  @Override
  public void deleteChannelById(UUID id) {
    String keyword = inputHandler.getYesNOInput().toLowerCase();
    if (keyword.equals("y")) {

      if (channelRepository.findChannelById(id).isEmpty()) {
        throw new NoSuchElementException("채널(" + id + ")가 존재하지 않습니다.");
      }

      // 채널 메서지 삭제
      List<UUID> messageIds =
          messageRepository.findMessagesByChannelId(id).stream()
              .map(BaseEntity::getId)
              .toList();

      // messageRepository::deleteMessageById 메서드 참조
      messageIds.forEach(messageRepository::deleteMessageById);

      // 채널의 읽음 상태 삭제
      List<UUID> readStatuseIds =
          readStatusService.findAllByChannelId(id).stream()
              .map(ReadStatus::getId)
              .toList();

      readStatuseIds.forEach(readStatusService::deleteReadStatusById);

      // 채널 삭제
      channelRepository.deleteChannelById(id);
    }
  }
}
