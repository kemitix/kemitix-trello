package net.kemitix.trello;

import java.io.File;

public interface Attachment {

    /**
     * The name of the attachment file.
     *
     * @return the name of the file
     */
    File getFilename();

    /**
     * Downlaods the file to local file system.
     *
     * @return the name of the local file.
     */
    LocalAttachment download();

    /**
     * The original filename.
     *
     * @return the name of the file originally
     */
    File getOriginalFilename();

    /**
     * Adds the API Key Pair to a {@link TrelloAttachment}, creating a new
     * instance, but ignored otherwise.
     *
     * @param apiKeyPair the key pair to add
     * @return a new TrelloAttachment instance or itself
     */
    Attachment withApiKeyPair(final ApiKeyPair apiKeyPair);

}
