package com.sprint.mission.discodeit.util;

import java.util.Map;

public class EntryUtils {
    public static <K, V> Map.Entry<K, V> of(K key, V value) {
        return Map.entry(key, value);
    }
}
