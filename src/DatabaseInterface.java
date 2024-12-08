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

import java.util.ArrayList;

public interface DatabaseInterface {

    /**
     * Logs in a user based on username and password.
     *
     * @param userName the username of the user.
     * @param password the password of the user.
     * @return the logged-in User object, or null if login fails.
     */
    User login(String userName, String password);

    /**
     * Finds a user by username.
     *
     * @param username the username to search for.
     * @return the User object with the specified username, or null if not found.
     */
    User findUser(String username);

    /**
     * Adds a new user to the database.
     *
     * @param user the User object to add.
     * @return true if the user was successfully added, false if the user already exists.
     */
    boolean addUser(User user);

    /**
     * Saves the database to a file.
     *
     * @param filename the name of the file to save to.
     */
    void saveDatabase(String filename);

    /**
     * Loads the database from a file.
     *
     * @param filename the name of the file to load from.
     */
    void loadDatabase(String filename);

    /**
     * Displays all users in the database.
     */
    void viewUsers();

    /**
     * Retrieves the list of users in the database.
     *
     * @return an ArrayList of User objects.
     */
    ArrayList<User> getUsers();
}
