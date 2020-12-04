package net.kemitix.trello;

import org.apache.camel.Header;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;

@ApplicationScoped
public class LoadCard {

    private final TrelloBoard trelloBoard;

    @Inject
    public LoadCard(TrelloBoard trelloBoard) {
        this.trelloBoard = trelloBoard;
    }

    public TrelloCard loadCard(@Header("SlushyCardId") String cardId) {
        return Objects.requireNonNull(
                trelloBoard.getCard(cardId),
                "Card Not Found"
        );
    }

}
