package net.kemitix.trello;

import com.julienvey.trello.Trello;
import com.julienvey.trello.TrelloHttpClient;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.impl.http.JDKTrelloHttpClient;


public class TrelloProducers {

    public TrelloHttpClient trelloHttpClient() {
        return new JDKTrelloHttpClient();
    }

    public TrelloClient trello(
            TrelloConfig config,
            TrelloHttpClient httpClient
    ) {
        return new KemitixTrelloClient(
                config.getTrelloKey(),
                config.getTrelloSecret(),
                httpClient);
    }

    public Member member(Trello trello, TrelloConfig trelloConfig) {
        return trello.getMemberInformation(trelloConfig.getUserName());
    }

}
