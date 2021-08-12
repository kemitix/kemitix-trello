package net.kemitix.trello;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiKeyPairImpl implements ApiKeyPair {

    public static final ApiKeyPair NONE = new ApiKeyPairImpl("", "");

    private final String key;
    private final String token;

}
