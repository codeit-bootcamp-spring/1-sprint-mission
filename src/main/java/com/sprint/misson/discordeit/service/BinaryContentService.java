package com.sprint.misson.discordeit.service;

import com.sprint.misson.discordeit.entity.BinaryContent;

import java.util.List;

public interface BinaryContentService {

    BinaryContent create(BinaryContent content);

    BinaryContent findById(String contentId);

    List<BinaryContent> findAllByIdIn(List<String> contentIds);

    boolean deleteById(String contentId);

}
