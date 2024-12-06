import java.io.Serializable;

public class Message implements Serializable {

    private User sender;
    private User recipient;
    private String content;

    public Message (User sender, User recipient, String content) {

        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    // edit message
    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {
        return sender.getUsername() + "," + recipient.getUsername() + "," + content;
    }
}
