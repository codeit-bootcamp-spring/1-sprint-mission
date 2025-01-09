package sprint.mission.discodeit.constant;

public enum Constants {
    NOT_FOUND("-1", true),
    EMPTY_TIME("0", true),
    EMPTY_STRING("", false),
    EMPTY_UUID("00000000-0000-0000-0000-000000000000", false),
    MAX_NAME_LENGTH("10", true),
    EMAIL_FORMAT("UserName@DomainName.TopLevelDomain", false),
    PHONE_NUMBER_FORMAT("000-0000-0000", false),
    PHONE_NUMBER_LENGTH("11", true),
    MAX_CONTENT_LENGTH("20", true);

    private final String value;
    private final boolean canParseToInteger;

    Constants(String value, boolean canParseToInteger) {
        this.value = value;
        this.canParseToInteger = canParseToInteger;
    }

    public String getAsString() {
        return value;
    }
    public int getAsInteger() throws UnsupportedOperationException {
        if (canParseToInteger)
            return Integer.parseInt(value);

        throw new UnsupportedOperationException();
    }
}
