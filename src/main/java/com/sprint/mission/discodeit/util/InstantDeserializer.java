package com.sprint.mission.discodeit.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;

public class InstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // 숫자 토큰인 경우 바로 처리
        if (p.getCurrentToken() == JsonToken.VALUE_NUMBER_INT) {
            return Instant.ofEpochMilli(p.getLongValue());
        }

        // 문자열 토큰 처리: 공백 제거 후 시도
        String value = p.getText().trim();
        try {
            return Instant.parse(value); // ISO-8601 형식
        } catch (DateTimeParseException e) {
            try {
                return Instant.ofEpochMilli(Long.parseLong(value)); // 에포크 밀리초 형식
            } catch (NumberFormatException ex) {
                throw new IOException("Invalid timestamp format: " + value, ex);
            }
        }
    }
}
