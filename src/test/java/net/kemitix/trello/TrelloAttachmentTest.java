package net.kemitix.trello;

import com.julienvey.trello.domain.Attachment;
import com.julienvey.trello.domain.Card;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class TrelloAttachmentTest
        implements WithAssertions {

    @Test
    void regressionExtensionTruncated() {
        //given
        Attachment attachment = new Attachment();
        attachment.setUrl("card-url.extension");
        Card card = new Card();
        card.setIdShort("123");
        card.setName("card-name");
        AttachmentDirectory dir = new AttachmentDirectoryImpl();
        var trelloAttachment = TrelloAttachment.create(
                attachment, card, dir
        );
        //when
        File filename = trelloAttachment.getFilename();
        //then
        assertThat(filename.getName()).endsWith(".extension");
    }

    @Test
    void convertsUnsafeFilename() {
        //given
        Attachment attachment = new Attachment();
        attachment.setUrl("file.txt");
        Card card = new Card();
        card.setIdShort("123");
        card.setName("Card name Á");
        AttachmentDirectory dir = new AttachmentDirectoryImpl();
        var trelloAttachment = TrelloAttachment.create(
                attachment, card, dir
        );
        //when
        File filename = trelloAttachment.getFilename();
        //then
        assertThat(filename.getName()).doesNotContain("Á");
        assertThat(filename.getName()).isEqualTo(" 123 - Card name A.txt");
    }
}
