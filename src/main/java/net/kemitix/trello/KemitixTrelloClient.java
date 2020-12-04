package net.kemitix.trello;

import com.julienvey.trello.TrelloHttpClient;
import com.julienvey.trello.impl.TrelloImpl;

public class KemitixTrelloClient
        extends TrelloImpl
        implements TrelloClient {

    public KemitixTrelloClient(
            String applicationKey,
            String accessToken,
            TrelloHttpClient httpClient
    ) {
        super(applicationKey, accessToken, httpClient);
    }

}
