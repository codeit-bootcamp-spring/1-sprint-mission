package com.sprint.mission.discodeit.service.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelListResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

public class BasicMessageService implements MessageService {
	private final MessageRepository messageRepository;
	private final UserService userService;
	private final ChannelService channelService;
	private final BinaryContentService binaryContentService;

	public BasicMessageService(MessageRepository messageRepository, UserService userService,
		ChannelService channelService,
		BinaryContentService binaryContentService) {
		this.messageRepository = messageRepository;
		this.userService = userService;
		this.channelService = channelService;
		this.binaryContentService = binaryContentService;
	}

	/**
	 * 새로운 메시지를 생성합니다.
	 * @param request 메시지 생성 요청 정보 (작성자 ID, 채널 ID, 내용, 첨부파일)
	 * @return 생성된 메시지 응답
	 */
	@Override
	public MessageResponse create(CreateMessageRequest request) {
		// 사용자와 채널 존재 여부 확인
		UserResponse author = userService.findUser(request.authorId());
		ChannelResponse channel = channelService.find(request.channelId());
		UUID channelId = request.channelId();
		// 메시지 작성자가 채널 참여자인지 확인
		if (!channel.participants().containsKey(request.authorId())) {
			throw new IllegalArgumentException(
				"Author is not a participant of the channel: " + request.channelId());
		}

		// Private Channel 생성 로직 추가 (디스코드도 private은 메세지를 상대방에게 보내야지 channel 추가 public은 초대를 먼저 한 후 메시지를 보낸다)
		// findPrivateChannelBetweenUsers는 나중에 구현...
		if (channelId == null) {
			UUID recipientId = request.receiverId(); // 메시지 수신자 ID 가져오기

			// 모든 채널 조회 후 PRIVATE 채널 찾기
			List<ChannelListResponse> allChannels = channelService.findAllByUserId(request.authorId());
			for (ChannelListResponse existChannel : allChannels) {
				if (existChannel.channelType() == ChannelType.PRIVATE &&
					existChannel.participants().containsKey(author.id()) &&
					existChannel.participants().containsKey(recipientId)) {
					channelId = existChannel.id(); // 기존 채널 사용
					break;
				}
			}

			// 기존 채널이 없으면 새 Private 채널 생성
			if (channelId == null) {
				CreatePrivateChannelRequest privateChannelRequest = new CreatePrivateChannelRequest(author.id(),
					recipientId, request.createdAt());
				Channel privateChannel = channelService.createPrivateChannel(privateChannelRequest);
				channelId = privateChannel.getId();
			}
		}

		// 메시지 기본 정보 생성 및 저장
		Message message = new Message(request.content(), request.authorId(), request.channelId());
		Message savedMessage = messageRepository.save(message);

		// 첨부파일이 있는 경우 처리
		List<BinaryContentResponse> attachments = new ArrayList<>();
		if (request.attachments() != null && !request.attachments().isEmpty()) {
			for (MultipartFile file : request.attachments()) {
				CreateBinaryContentRequest binaryRequest = new CreateBinaryContentRequest(
					request.authorId(),
					savedMessage.getId(),
					file,
					BinaryContentType.MESSAGE_ATTACHMENT
				);
				attachments.add(binaryContentService.create(binaryRequest));
			}
		}

		return createMessageResponse(savedMessage, attachments);
	}

	/**
	 * 메시지 ID로 메시지를 조회합니다.
	 * @param messageId 메시지 ID
	 * @return 메시지 응답
	 */
	@Override
	public MessageResponse findById(UUID messageId) {
		// 메시지 조회
		Message message = messageRepository.findById(messageId)
			.orElseThrow(() -> new IllegalArgumentException("Message not found"));

		// 메시지의 첨부파일 목록 조회
		List<BinaryContentResponse> attachments = binaryContentService
			.findAllByMessageId(messageId);

		return createMessageResponse(message, attachments);
	}

	/**
	 * 채널 ID로 해당 채널의 모든 메시지를 조회합니다.
	 * @param channelId 채널 ID
	 * @return 메시지 응답 목록
	 */
	@Override
	public List<MessageResponse> findAllByChannelId(UUID channelId) {
		List<Message> messages = messageRepository.findAllByChannelId(channelId);
		List<MessageResponse> responses = new ArrayList<>();

		// 각 메시지의 첨부파일 정보를 포함하여 응답 생성 캐싱을 사용해야될까...?
		for (Message message : messages) {
			List<BinaryContentResponse> attachments = binaryContentService
				.findAllByMessageId(message.getId());
			responses.add(createMessageResponse(message, attachments));
		}

		return responses;
	}

	/**
	 * 메시지를 수정합니다.
	 * @param messageId 수정할 메시지 ID
	 * @param request 메시지 수정 요청 정보 (내용, 새로운 첨부파일)
	 * @return 수정된 메시지 응답
	 */
	@Override
	public MessageResponse update(UUID messageId, UpdateMessageRequest request) {
		// 메시지 조회 및 내용 업데이트
		Message message = messageRepository.findById(messageId)
			.orElseThrow(() -> new IllegalArgumentException("Message not found"));
		message.updateContent(request.content());
		Message updatedMessage = messageRepository.save(message);

		// 기존 메시지에 연결된 첨부파일 목록 조회
		List<BinaryContentResponse> currentAttachments = binaryContentService.findAllByMessageId(messageId);

		// 삭제할 첨부파일 ID가 존재하면 해당 첨부파일 제거
		if (request.attachmentsToRemove() != null && !request.attachmentsToRemove().isEmpty()) {
			for (UUID attachmentId : request.attachmentsToRemove()) {
				binaryContentService.delete(attachmentId);
			}
			// 다시 조회하여 현재 첨부파일 목록을 갱신
			currentAttachments = binaryContentService.findAllByMessageId(messageId);
		}

		// 새 첨부파일 추가
		if (request.attachmentsToAdd() != null && !request.attachmentsToAdd().isEmpty()) {
			for (MultipartFile file : request.attachmentsToAdd()) {
				CreateBinaryContentRequest binaryRequest = new CreateBinaryContentRequest(
					message.getAuthorId(),
					messageId,
					file,
					BinaryContentType.MESSAGE_ATTACHMENT
				);
				currentAttachments.add(binaryContentService.create(binaryRequest));
			}
		}

		return createMessageResponse(updatedMessage, currentAttachments);
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
		List<BinaryContentResponse> attachments = binaryContentService.findAllByMessageId(messageId);
		for (BinaryContentResponse attachment : attachments) {
			binaryContentService.delete(attachment.id());
		}

		// 메시지 삭제
		messageRepository.delete(messageId);
	}

	/**
	 * 메시지 엔티티와 첨부파일 목록으로 메시지 응답을 생성합니다.
	 * @param message 메시지 엔티티
	 * @param attachments 첨부파일 응답 목록
	 * @return 메시지 응답
	 */
	private MessageResponse createMessageResponse(Message message, List<BinaryContentResponse> attachments) {
		return new MessageResponse(message.getId(), message.getContent(), message.getAuthorId(), message.getChannelId(),
			attachments, message.getCreatedAt(), message.getUpdatedAt());
	}
}