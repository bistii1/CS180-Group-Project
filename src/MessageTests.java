import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class MessageTests {

    private User sender;
    private User recipient;

    @Before
    public void setUp() {
        sender = new User("user1", "password1", "profilePicture1", "user2; user4", "user5", new ArrayList<>());
        recipient = new User("user2", "password2", "profilePicture2", "user3", "user5", new ArrayList<>());
    }

    @Test
    public void testMessageInitialization() {

        String content = "Hello, how are you?";
        
        Message message = new Message(sender, recipient, content);

        assertEquals("Sender should match the initialized value", sender, message.getSender());
        assertEquals("Recipient should match the initialized value", recipient, message.getRecipient());
        assertEquals("Content should match the initialized value", content, message.getContent());
    }

    @Test
    public void testEditMessageContent() {
        String initialContent = "Initial message";
        String newContent = "Edited message";

        Message message = new Message(sender, recipient, initialContent);
        message.setContent(newContent);

        assertEquals("Content should be updated after editing", newContent, message.getContent());
    }

    @Test
    public void testToStringMethod() {
        String content = "See you!";

        Message message = new Message(sender, recipient, content);
        String expected = "user1,user2,See you!";

        assertEquals("toString should return the correct formatted string", expected, message.toString());
    }

    @Test
    public void testSetSenderAndRecipient() {
        String content = "Message";

        Message message = new Message(sender, recipient, content);

        User newSender = new User("user3", "password3", "profilePicture3", "user1", "user5", new ArrayList<>());
        User newRecipient = new User("user4", "password4", "profilePicture4", "user5", "user1", new ArrayList<>());

        message.setSender(newSender);
        message.setRecipient(newRecipient);

        assertEquals("Sender should be updated correctly", newSender, message.getSender());
        assertEquals("Recipient should be updated correctly", newRecipient, message.getRecipient());
    }
}