import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class UserTest {

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;

    @Before
    public void setUp() {

        // Initialize users
        user1 = new User("user1", "password1", "profilePicture1", "user2; user4", "user5", new ArrayList<>());
        user2 = new User("user2", "password2", "profilePicture2", "user3", "user5", new ArrayList<>());
        user3 = new User("user3", "password3", "profilePicture3", "user1", "user5", new ArrayList<>());
        user4 = new User("user4", "password4", "profilePicture4", "user5", "user1", new ArrayList<>());
        user5 = new User("user5", "password5", "profilePicture5", "user4", "user1", new ArrayList<>());

        // Create message history
        user1.getMessageHistory().add(new Message(user1, user2, "Hello!"));
        user1.getMessageHistory().add(new Message(user1, user3, "Welcome!"));

        user2.getMessageHistory().add(new Message(user2, user3, "Bye!"));
        user2.getMessageHistory().add(new Message(user2, user1, "It's good to be here"));

        user3.getMessageHistory().add(new Message(user3, user4, "Sayonara."));
        user3.getMessageHistory().add(new Message(user3, user1, "!!"));

        user4.getMessageHistory().add(new Message(user4, user1, "Mayo."));
        user4.getMessageHistory().add(new Message(user4, user3, "??"));

        user5.getMessageHistory().add(new Message(user5, user3, "No."));
        user5.getMessageHistory().add(new Message(user5, user2, "Ice cream"));
    }

    @Test
    public void testUpdateProfile() {
        user1.updateProfile("newUser1", "newPassword1", "newProfilePicture1");
        assertEquals("Incorrect Username Found", "newUser1", user1.getUsername());
        assertEquals("Incorrect Password Found", "newPassword1", user1.getPassword());
        assertEquals("Incorrect Profile Picture Found", "newProfilePicture1", user1.getProfilePicture());
    }

    @Test
    public void testAddFriend() {
        String result = user1.addFriend(user3);
        assertTrue(result.contains("Added"));
        assertTrue(user1.getFriends().contains("user3"));
        assertTrue(user3.getFriends().contains("user1"));
    }

    @Test
    public void testRemoveFriend() {
        user1.addFriend(user3);
        String result = user1.removeFriend(user3);
        assertEquals("Friend has been removed.", result);
        assertFalse(user1.getFriends().contains("user3"));
        assertFalse(user3.getFriends().contains("user1"));
    }

    @Test
    public void testBlockUser() {
        String blockResult = user2.blockUser(user4);
        assertEquals("This user has been blocked.", blockResult);
        assertTrue(user2.getBlockedUsers().contains("user4"));

        // Attempt to add a blocked user as a friend should fail
        String addFriendResult = user2.addFriend(user4);
        assertTrue(addFriendResult.contains("blocked"));
    }

    @Test
    public void testUnblockUser() {
        user2.blockUser(user4);
        String unblockResult = user2.unblockUser(user4);
        assertEquals("This user has been unblocked", unblockResult);
        assertFalse(user2.getBlockedUsers().contains("user4"));
    }

    @Test
    public void testFriendsWith() {
        assertTrue(user1.friendsWith(user2));
        assertFalse(user1.friendsWith(user3));
    }

    @Test
    public void testSendMessage() {
        String messageResult = user1.sendMessage(user2, "Hello!");
        assertEquals("Message sent!", messageResult);
        assertTrue(user1.getMessageHistory().size() > 0);
        assertTrue(user2.getMessageHistory().size() > 0);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals("user2", user2.getUsername());
        assertEquals("password2", user2.getPassword());
        assertEquals("profilePicture2", user2.getProfilePicture());
        assertNotNull(user2.getMessageHistory());
    }

    @Test
    public void testDeleteMessage() {
        Message message = new Message(user1, user2, "Test message");
        user1.getMessageHistory().add(message);
        user1.deleteMessage(message);
        assertFalse(user1.getMessageHistory().contains(message));
    }

    @Test
    public void testBlockedMethod() {
        user2.blockUser(user4);
        assertTrue(user2.blocked(user4));
        assertFalse(user2.blocked(user3));
    }

    @Test
    public void testSendMessageToBlockedUser() {
        user1.blockUser(user2);
        String result = user1.sendMessage(user2, "Hi");
        assertTrue(result.contains("blocked"));
    }


    @Test
    public void testToStringMethod() {
        String userString = user1.toString();
        assertTrue(userString.contains("user1"));
        assertTrue(userString.contains("password1"));
    }
}