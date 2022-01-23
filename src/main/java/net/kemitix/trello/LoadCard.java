package net.kemitix.trello;

import org.apache.camel.Header;

import java.util.Objects;

public class LoadCard {

    private final TrelloBoard trelloBoard;

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
