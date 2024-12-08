/**
 * Team Project -- Database Interface
 *
 * Database interface with all Database methods
 *
 * @author Bisti Potdar, lab sec 31
 *
 * @version November 3, 2024
 *
 */

public interface MessageInterface {

    /**
     * Gets the sender of the message.
     *
     * @return the User object representing the sender.
     */
    User getSender();

    /**
     * Sets the sender of the message.
     *
     * @param sender the User object to set as the sender.
     */
    void setSender(User sender);

    /**
     * Gets the recipient of the message.
     *
     * @return the User object representing the recipient.
     */
    User getRecipient();

    /**
     * Sets the recipient of the message.
     *
     * @param recipient the User object to set as the recipient.
     */
    void setRecipient(User recipient);

    /**
     * Gets the content of the message.
     *
     * @return the content of the message as a String.
     */
    String getContent();

    /**
     * Sets the content of the message.
     *
     * @param content the new content for the message.
     */
    void setContent(String content);

    /**
     * Returns a string representation of the message.
     *
     * @return a String containing the sender's username, recipient's username,
     *         and the message content, separated by commas.
     */
    @Override
    String toString();
}
