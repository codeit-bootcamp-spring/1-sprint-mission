package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {


  List<BinaryContent> findAllByIdIn(List<UUID> uuidList);
//  @Override
//  public List<BinaryContent> findAllByIdIn(List<UUID> uuidList) {
//    List<BinaryContent> findAllByIdList = new ArrayList<>();
//    for (UUID id : uuidList) {
//      BinaryContent binaryContent = findById(id);
//      findAllByIdList.add(binaryContent);
//    }
//    return findAllByIdList;
//  }


}
