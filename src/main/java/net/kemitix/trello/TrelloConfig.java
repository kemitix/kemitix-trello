package net.kemitix.trello;

public interface TrelloConfig {

    /**
     * The Trello Developer API Key.
     *
     * <p>https://trello.com/app-key</p>
     *
     * @return The Trello Developer API Key
     */
    String getTrelloKey();

    /**
     * The Trello Developer API Key Token.
     *
     * <p>https://trello.com/app-key</p>
     *
     * @return The Trello Developer API Key Token
     */
    String getTrelloSecret();

    /**
     * The name of the user with access to the Trello Slush Pile Board.
     *
     * @return The Trello Username
     */
    String getUserName();

    /**
     * The trello board containing the Slush Pile.
     *
     * @return The Trello Board Name
     */
    String getBoardName();

    /**
     * The email address to send submission attachments to.
     *
     * <p>e.g. the kindle address</p>
     *
     * @return The Sender Email Address
     */
    String getSender();

    /**
     * The email address to send emails from.
     *
     * <p>If sending to Kindle, then ensure this address is listed as a valid sender.</p>
     *
     * @return The Reader Email address
     */
    String getReader();

    /**
     * The URL of the Webhook to register with services.
     *
     * @return The Webhook URL
     */
    String getWebhook();

}
