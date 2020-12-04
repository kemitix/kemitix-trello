package net.kemitix.trello;

import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.Card;

import java.util.stream.Stream;

public class CardWithAttachments {
    private final com.julienvey.trello.domain.Card tcard;
    private final Trello trello;
    private AttachmentDirectory attachmentDir;

    private CardWithAttachments(
            Card tcard,
            Trello trello,
            AttachmentDirectory attachmentDir
    ) {
        this.tcard = tcard;
        this.trello = trello;
        this.attachmentDir = attachmentDir;
    }

    public static CardWithAttachments create(
            Card tcard,
            Trello trello,
            AttachmentDirectory attachmentDir
    ) {
        return new CardWithAttachments(tcard, trello, attachmentDir);
    }

    public Stream<Attachment> findAttachments() {
        return trello.getCardAttachments(tcard.getId()).stream()
                .map(attachment -> TrelloAttachment
                        .create(attachment, tcard, attachmentDir));
    }

    public String getName() {
        return tcard.getName();
    }
}
