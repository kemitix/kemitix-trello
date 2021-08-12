package net.kemitix.trello;

public interface ApiKeyPair {

    String getKey();
    String getToken();

    static ApiKeyPair create(String key, String token) {
        return new ApiKeyPairImpl(key, token);
    }

    static ApiKeyPair none() {
        return ApiKeyPairImpl.NONE;
    }

}
