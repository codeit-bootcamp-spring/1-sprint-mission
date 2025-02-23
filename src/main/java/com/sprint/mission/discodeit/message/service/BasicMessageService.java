package com.sprint.mission.discodeit.message.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.sprint.mission.discodeit.channel.dto.request.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.channel.service.ChannelService;
import com.sprint.mission.discodeit.message.dto.request.binaryContent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.message.dto.request.message.CreateMessageRequest;
import com.sprint.mission.discodeit.message.dto.request.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.message.entity.BinaryContent;
import com.sprint.mission.discodeit.message.entity.BinaryContentType;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.user.dto.request.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.user.service.UserStatusService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasicMessageService implements MessageService {
	private final MessageRepository messageRepository;
	private final UserService userService;
	private final UserStatusService userStatusService;
	private final ChannelService channelService;
	private final BinaryContentService binaryContentService;

	public BasicMessageService(MessageRepository messageRepository, UserService userService,
		UserStatusService userStatusService, ChannelService channelService,
		BinaryContentService binaryContentService) {
		this.messageRepository = messageRepository;
		this.userService = userService;
		this.userStatusService = userStatusService;
		this.channelService = channelService;
		this.binaryContentService = binaryContentService;
	}

	/**
	 * 새로운 메시지를 생성합니다.
	 * @param request 메시지 생성 요청 정보 (작성자 ID, 채널 ID, 내용, 첨부파일)
	 * @return 생성된 메시지 응답
	 */
	@Override
	public Message create(CreateMessageRequest request) {
		// 사용자와 채널 존재 여부 확인
		User author = userService.findUser(request.authorId());
		// 사용자 online 처리
		userStatusService.updateByUserId(new UpdateUserStatusRequest(author.getId(), request.createdAt()));
		UUID channelId = request.channelId();

		// Private Channel 생성 로직 추가 (디스코드도 private은 메세지를 상대방에게 보내야지 channel 추가 public은 초대를 먼저 한 후 메시지를 보낸다)
		// findPrivateChannelBetweenUsers는 나중에 구현...
		if (channelId == null) {
			UUID recipientId = request.receiverId(); // 메시지 수신자 ID 가져오기

			// 모든 채널 조회 후 PRIVATE 채널 찾기
			List<Channel> allChannels = channelService.findAllByUserId(request.authorId());
			for (Channel existChannel : allChannels) {
				if (existChannel.getChannelType() == ChannelType.PRIVATE &&
					existChannel.getParticipants().containsKey(author.getId()) &&
					existChannel.getParticipants().containsKey(recipientId)) {
					channelId = existChannel.getId(); // 기존 채널 사용
					break;
				}
			}

			// 기존 채널이 없으면 새 Private 채널 생성
			if (channelId == null) {
				CreatePrivateChannelRequest privateChannelRequest = new CreatePrivateChannelRequest(author.getId(),
					recipientId, request.createdAt());
				Channel privateChannel = channelService.createPrivateChannel(privateChannelRequest);
				channelId = privateChannel.getId();
				log.info("private channel 생성완료" + channelId);
			}
		}

		// 기존에는 private channel로 meesage를 보내면 이 코드가 조건문 앞에 들어가 있어서 무조건 에러가 발생해서 조건문 끝난 뒤로 수정
		Channel channel = channelService.find(channelId);

		// 메시지 작성자가 채널 참여자인지 확인
		if (!channel.getParticipants().containsKey(request.authorId())) {
			throw new IllegalArgumentException(
				"Author is not a participant of the channel: " + request.channelId());
		}

		// 메시지 기본 정보 생성 및 저장
		Message message = new Message(request.content(), request.authorId(), channelId);
		Message savedMessage = messageRepository.save(message);

		// 첨부파일이 있는 경우 처리
		// Todo 한 메시지에 첨부파일 보내는 개수 정하는게 좋을것 같다
		if (request.attachments() != null && !request.attachments().isEmpty()) {
			for (MultipartFile file : request.attachments()) {
				CreateBinaryContentRequest binaryRequest = new CreateBinaryContentRequest(
					request.authorId(),
					savedMessage.getId(),
					file,
					BinaryContentType.MESSAGE_ATTACHMENT
				);
				binaryContentService.create(binaryRequest);
			}
		}

		// 메시지를 채널에 추가하는 로직을 `ChannelService`에게 위임 message는 그동안 저장을 하지 않아서 안됐다
		channelService.addMessageToChannel(channelId, savedMessage);

		return savedMessage;
	}

	/**
	 * 메시지 ID로 메시지를 조회합니다.
	 * @param messageId 메시지 ID
	 * @return 메시지 응답
	 */
	@Override
	public Message findById(UUID messageId) {
		// 메시지 조회
		Message message = messageRepository.findById(messageId)
			.orElseThrow(() -> new IllegalArgumentException("Message not found"));

		return message;
	}

	/**
	 * 채널 ID로 해당 채널의 모든 메시지를 조회합니다.
	 * @param channelId 채널 ID
	 * @return 메시지 응답 목록
	 */
	@Override
	public List<Message> findAllByChannelId(UUID channelId) {
		List<Message> messages = messageRepository.findAllByChannelId(channelId);

		return messages;
	}

	/**
	 * 메시지를 수정합니다.
	 * @param messageId 수정할 메시지 ID
	 * @param request 메시지 수정 요청 정보 (내용, 새로운 첨부파일)
	 * @return 수정된 메시지 응답
	 */
	@Override
	public Message update(UUID messageId, UpdateMessageRequest request) {
		// 메시지 조회 및 내용 업데이트
		Message message = messageRepository.findById(messageId)
			.orElseThrow(() -> new IllegalArgumentException("Message not found"));
		message.updateContent(request.content());
		Message updatedMessage = messageRepository.save(message);

		// 기존 첨부파일 모두 삭제
		// Todo 해당 메시지에서 기존 첨부파일을 모두 삭제한 뒤에 업데이트 된 첨부파일들을 전부 다시 넣게 된다면 삭제되지 않아야 하는 첨부파일들에 대해서도
		// Todo 삭제를 하니까 비효율적인게 아닐까...? 처음처럼 삭제할 file, 추가할 file을 따로 받아서 적용하는게 더 나은 방법일까...?
		List<BinaryContent> currentAttachments = binaryContentService.findAllByMessageId(messageId);
		for (BinaryContent attachment : currentAttachments) {
			binaryContentService.delete(attachment.getId());
		}

		// 새 첨부파일 추가
		if (request.attachments() != null && !request.attachments().isEmpty()) {
			for (MultipartFile file : request.attachments()) {
				CreateBinaryContentRequest binaryRequest = new CreateBinaryContentRequest(
					message.getAuthorId(),
					messageId,
					file,
					BinaryContentType.MESSAGE_ATTACHMENT
				);
				binaryContentService.create(binaryRequest);
			}
		}

		return updatedMessage;
	}

	/**
	 * 메시지를 삭제합니다.
	 * @param messageId 삭제할 메시지 ID
	 */
	@Override
	public void delete(UUID messageId) {
		Message message = messageRepository.findById(messageId)
			.orElseThrow(() -> new IllegalArgumentException("Message not found"));

		// 메시지에 연결된 모든 첨부파일 삭제
		List<BinaryContent> attachments = binaryContentService.findAllByMessageId(messageId);
		for (BinaryContent attachment : attachments) {
			binaryContentService.delete(attachment.getId());
		}

		// 메시지 삭제
		messageRepository.delete(message.getId());
	}

}