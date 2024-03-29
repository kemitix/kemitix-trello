package net.kemitix.trello;

import com.julienvey.trello.domain.Card;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
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
    private final ApiKeyPair apiKeyPair;

    private TrelloAttachment(
            com.julienvey.trello.domain.Attachment attachment,
            Card card,
            AttachmentDirectory attachmentDirectory,
            ApiKeyPair apiKeyPair
    ) {
        this.attachment = attachment;
        this.card = card;
        this.attachmentDirectory = attachmentDirectory;
        this.id = card.getIdShort();
        this.apiKeyPair = apiKeyPair;
    }

    public static Attachment create(
            com.julienvey.trello.domain.Attachment attachment,
            Card card,
            AttachmentDirectory dir
    ) {
        return new TrelloAttachment(attachment, card, dir, ApiKeyPair.none());
    }

    public static Attachment create(
            com.julienvey.trello.domain.Attachment attachment,
            Card card,
            AttachmentDirectory dir,
            ApiKeyPair apiKeyPair
    ) {
        return new TrelloAttachment(attachment, card, dir, apiKeyPair);
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
        if (apiKeyPair == ApiKeyPair.none()) {
            throw new UnsupportedOperationException(
                    "Download not permitted without a valid API KeyPair");
        }
        try (var source = Channels.newChannel(getConnection().getInputStream());) {
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

    private URLConnection getConnection() throws IOException {
        var connection = (HttpURLConnection) getUrl().openConnection();
        connection.setRequestProperty("Authorization", String.format(
                "OAuth oauth_consumer_key=\"%s\", oauth_token=\"%s\"",
                apiKeyPair.getKey(), apiKeyPair.getToken()));
        return connection;
    }

    @Override
    public File getOriginalFilename() {
        return getFilename();
    }

    @Override
    public Attachment withApiKeyPair(ApiKeyPair apiKeyPair) {
        return create(attachment, card, attachmentDirectory, apiKeyPair);
    }

    private URL getUrl() throws MalformedURLException {
        return URI.create(attachment.getUrl()).toURL();
    }
}
