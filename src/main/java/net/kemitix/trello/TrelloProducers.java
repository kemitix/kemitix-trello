package net.kemitix.trello;

import com.julienvey.trello.Trello;
import com.julienvey.trello.TrelloHttpClient;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.impl.http.JDKTrelloHttpClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class TrelloProducers {

    @Produces
    @ApplicationScoped
    TrelloHttpClient trelloHttpClient() {
        return new JDKTrelloHttpClient();
    }

    @Produces
    @ApplicationScoped
    TrelloClient trello(
            TrelloConfig config,
            TrelloHttpClient httpClient
    ) {
        return new KemitixTrelloClient(
                config.getTrelloKey(),
                config.getTrelloSecret(),
                httpClient);
    }

    @Produces
    @ApplicationScoped
    Member member(Trello trello, TrelloConfig trelloConfig) {
        return trello.getMemberInformation(trelloConfig.getUserName());
    }

}
