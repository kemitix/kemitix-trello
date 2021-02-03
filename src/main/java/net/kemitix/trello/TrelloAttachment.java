package net.kemitix.trello;

import com.julienvey.trello.domain.Card;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Optional;
import java.util.logging.Logger;

public class TrelloAttachment implements Attachment {

    private static final Logger LOG =
            Logger.getLogger(
                    TrelloAttachment.class.getName());

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
                id, safeCardName(), extension()));
    }

    private String safeCardName() {
        String normalize = Normalizer.normalize(card.getName(), Normalizer.Form.NFD);
        return normalize.replaceAll("[^\\p{ASCII}]", "");
    }

    private String extension() {
        URI uri = URI.create(attachment.getUrl());
        String path = uri.getPath();
        return Optional.ofNullable(path)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(path.lastIndexOf(".") + 1))
                .orElse("");
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
