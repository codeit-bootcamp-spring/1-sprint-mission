package com.sprint.mission.discodeit.repository.binarycontent;

import com.sprint.mission.discodeit.domain.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.repository.binarycontent.interfaces.BinaryRepository;

public class BinaryInMemoryRepository implements BinaryRepository {

    @Override
    public BinaryContent save(BinaryContent binaryContent) {

        return binaryContent;
    }

}
