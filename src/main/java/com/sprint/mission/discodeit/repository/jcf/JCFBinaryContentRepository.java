//package com.sprint.mission.discodeit.repository.jcf;
//
//import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
//import com.sprint.mission.discodeit.exception.binary.BinaryContentException;
//import com.sprint.mission.discodeit.repository.BinaryContentRepository;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Repository;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Repository
//@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
//public class JCFBinaryContentRepository implements BinaryContentRepository {
//
//    private final Map<UUID, BinaryContent> storage = new HashMap<>();
//
//    @Override
//    public BinaryContent save(BinaryContent binaryContent) {
//        if (binaryContent == null) {
//            throw new BinaryContentException("binaryContent가 null입니다.");
//        }
//        storage.put(binaryContent.getId(), binaryContent);
//        return binaryContent;
//    }
//
//    @Override
//    public BinaryContent findByMessageId(UUID messageId) {
//        messageIdChecker(messageId);
//
//        return storage.values().stream()
//                .filter(content -> content.getMessageId().equals(messageId))
//                .findFirst()
//                .orElseThrow(() -> new BinaryContentException("BinaryContent not found for messageId: " + messageId));
//    }
//
//    @Override
//    public Map<UUID, BinaryContent> findByUserId(UUID userId) {
//        userIdChecker(userId);
//
//        Map<UUID, BinaryContent> result = storage.values().stream()
//                .filter(content -> content.getUserId().equals(userId))
//                .collect(Collectors.toMap(
//                        BinaryContent::getId,
//                        content -> content
//                ));
//
//        if (result.isEmpty()) {
//            throw new IllegalStateException("No BinaryContent found for userId: " + userId);
//        }
//
//        return Collections.unmodifiableMap(result);
//    }
//
//    @Override
//    public Map<UUID, BinaryContent> findAll() {
//        return Collections.unmodifiableMap(new HashMap<>(storage));
//    }
//
//    @Override
//    public BinaryContent findById(UUID id) {
//        userIdChecker(id);
//
//        return storage.values().stream()
//                .filter(v -> v.getUserId().equals(id))
//                .findFirst()
//                .orElseThrow(BinaryContentException::new);
//    }
//
//    @Override
//    public void remove(UUID id) {
//        userIdChecker(id);
//
//        storage.remove(id);
//    }
//
//    @Override
//    public void removeAllContentOfUser(UUID userId) {
//        userIdChecker(userId);
//
//        // userId에 해당하는 모든 content의 id를 수집
//        List<UUID> contentIdsToRemove = storage.values().stream()
//                .filter(content -> content.getUserId().equals(userId))
//                .map(BinaryContent::getId)
//                .collect(Collectors.toList());
//
//        if (contentIdsToRemove.isEmpty()) {
//            throw new IllegalStateException("No BinaryContent found for userId: " + userId);
//        }
//
//        // 수집된 id들을 이용해 content 삭제
//        contentIdsToRemove.forEach(storage::remove);
//    }
//
//    @Override
//    public Map<UUID, BinaryContent> removeContent(UUID messageId) {
//        messageIdChecker(messageId);
//
//        // messageId에 해당하는 모든 content를 찾아서 맵으로 저장
//        Map<UUID, BinaryContent> removedContents = storage.values().stream()
//                .filter(content -> content.getMessageId().equals(messageId))
//                .collect(Collectors.toMap(
//                        BinaryContent::getId,
//                        content -> content
//                ));
//
//        // 찾은 content들을 저장소에서 삭제
//        removedContents.keySet().forEach(storage::remove);
//
//        return Collections.unmodifiableMap(removedContents);
//    }
//
//    private static void userIdChecker(UUID userId) {
//        if (userId == null) {
//            throw new BinaryContentException("userId가 null입니다.");
//        }
//    }
//
//    private static void messageIdChecker(UUID messageId) {
//        if (messageId == null) {
//            throw new BinaryContentException("messageId가 null입니다.");
//        }
//    }
//}