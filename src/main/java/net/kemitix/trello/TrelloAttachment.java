package net.kemitix.trello;

import com.julienvey.trello.domain.Card;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.logging.Logger;

public class TrelloAttachment implements Attachment {

    private static final Logger LOG =
            Logger.getLogger(
                    TrelloAttachment.class.getName());

    private static final String[] EXTENSIONS = new String[]{"doc", "docx", "odt"};
    private final com.julienvey.trello.domain.Attachment attachment;
    private final Card card;
    private final AttachmentDirectory attachmentDirectory;
    private final String id;

    private TrelloAttachment(
            com.julienvey.trello.domain.Attachment attachment,
            Card card,
            AttachmentDirectory attachmentDirectory
    ) {
        this.attachment = attachment;
        this.card = card;
        this.attachmentDirectory = attachmentDirectory;
        this.id = card.getIdShort();
    }

    public static Attachment create(
            com.julienvey.trello.domain.Attachment attachment,
            Card card,
            AttachmentDirectory dir
    ) {
        return new TrelloAttachment(attachment, card, dir);
    }

    @Override
    public File getFilename() {
        return new File(String.format("%4s - %s.%s",
                id, card.getName(), extension()));
    }

    private String extension() {
        URI uri = URI.create(attachment.getUrl());
        String path = uri.getPath();
        for (String ex : EXTENSIONS) {
            if (path.endsWith("." + ex)) {
                return ex;
            }
        }
        return "";
    }

    @Override
    public LocalAttachment download() {
        try (var source = Channels.newChannel(getUrl().openStream());){
            File filename = new File(attachment.getName());
            LOG.info("Downloading from " + filename);
            var file = attachmentDirectory.createFile(getFilename());
            LOG.info("Downloading to " + file.getCanonicalPath());
            try (var channel = new FileOutputStream(file).getChannel()) {
                long length = channel.transferFrom(source, 0, Long.MAX_VALUE);
                LOG.info("Downloaded length: " + length);
                return new LocalAttachment(file, filename, length);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File getOriginalFilename() {
        return getFilename();
    }

    private URL getUrl() throws MalformedURLException {
        return URI.create(attachment.getUrl()).toURL();
    }
}