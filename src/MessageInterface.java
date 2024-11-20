import java.util.ArrayList;

/**
 * Team Project -- Message Interface
 *
 * Message interface with all Database methods
 *
 * @author Neel Sharma, lab sec 31
 *
 * @version November 3, 2024
 *
 */

public interface MessageInterface {
    String setMessageData();
    void editMessage(String newContent, String oldContent);
    void sendNewMessage();
    void deleteMessage();
    String getFileName();
    void setFileName(String fileName);
    String getSenderID();
    boolean getFriendOnly();
    String getContent();
    void getMessageID();
    void getStatus();
    void setMessageID();
    void setSenderID();
    void setFriendOnly();
    void setContent();
    ArrayList<String> getDirectMessages();
    void setFriendOnly(boolean friendOnly);
    boolean isFriendOnly();
}
