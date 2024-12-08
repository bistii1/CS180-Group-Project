import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class DatabaseTest {

    private Database testDatabase;
    private User user1;
    private User user2;

    @Before
    public void setup() {
        testDatabase = new Database();
        user1 = new User("user1", "password1", "profilePicture1", "user2; user4", "user5", new ArrayList<>());
        user2 = new User("user2", "password2", "profilePicture2", "user3", "user5", new ArrayList<>());
    }

    @Test
    public void testAddUser() {
        assertTrue(testDatabase.addUser(user1));
        assertTrue(testDatabase.addUser(user2));
        assertFalse(testDatabase.addUser(user1)); // Duplicate user
    }

    @Test
    public void testFindUser() {
        testDatabase.addUser(user1);
        testDatabase.addUser(user2);

        assertEquals(user1, testDatabase.findUser("user1"));
        assertEquals(user2, testDatabase.findUser("user2"));
        assertNull(testDatabase.findUser("user13"));
    }

    @Test
    public void testLogin() {
        testDatabase.addUser(user1);

        assertEquals(user1, testDatabase.login("user1", "password1"));
        assertNull(testDatabase.login("user2", "password2"));
        assertNull(testDatabase.login("unknownUser", "unknownPassword"));
    }

    @Test
    public void testChangeUsername() {
        testDatabase.addUser(user1);

        assertTrue(testDatabase.changeUsername("user1", "newUsername1"));
        assertNotNull(testDatabase.findUser("newUsername1"));
        assertNull(testDatabase.findUser("user1"));
    }

    @Test
    public void testUpdatePassword() {
        testDatabase.addUser(user1);
        assertTrue(testDatabase.updatePassword("user1", "newPassword1"));
        assertEquals("newPassword1", testDatabase.findUser("user1").getPassword());
        assertFalse(testDatabase.updatePassword("nonExistentUser", "password123"));
    }

    @Test
    public void testViewUsers() {
        testDatabase.addUser(user1);
        testDatabase.addUser(user2);

        assertEquals(2, testDatabase.getUsers().size());
    }

    @Test
    public void testSaveAndLoad() {
        testDatabase.addUser(user1);
        testDatabase.addUser(user2);

        String filename = "test_database.txt";
        testDatabase.saveDatabase(filename);

        File file = new File(filename);
        assertTrue(file.exists());

        Database loadedDatabase = new Database();
        loadedDatabase.loadDatabase(filename);

        assertEquals(2, loadedDatabase.getUsers().size());
        assertEquals(user1.getUsername(), loadedDatabase.getUsers().get(0).getUsername());
        assertEquals(user2.getUsername(), loadedDatabase.getUsers().get(1).getUsername());

        assertTrue(file.delete());
    }

    @Test
    public void testSaveInformation() {
        testDatabase.addUser(user1);
        testDatabase.addUser(user2);

        testDatabase.saveDatabase("database.txt");
        File infoFile = new File("databaseInformation.txt");

        assertTrue(infoFile.exists());
        assertTrue(infoFile.delete());
    }
}
