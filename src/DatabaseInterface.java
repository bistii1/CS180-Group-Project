import java.io.IOException;

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

public interface DatabaseInterface {

    User findUser(String username);
    void viewUsers();

    void saveDatabase(String filename) throws IOException;

    boolean addUser(User user);

    static DatabaseInterface loadDatabase(String filename) throws IOException {
        return null;
    }

}
