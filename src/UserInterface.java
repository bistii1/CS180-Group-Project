import java.util.ArrayList;

/**
 * Project 4 -- User Interface
 *
 * User interface with all method signatures
 *
 * @author Anubhuti Mittal, lab sec 31
 *
 * @version November 3, 2024
 *
 */

public interface UserInterface {


    // Friend Management
    String addFriend(User user);
    String removeFriend(User user);

    // Blocked User Management
    String blockUser(User user);
    String unblockUser(User user);

    // Getters
    String getUsername();
    String getPassword();
    String getProfilePicture();
    String getFriends();

    // It is static since it is not specific to an instance variable
    static ArrayList<User> getUsers() {
        return new ArrayList<>();   // returns an empty user ArrayList
    }

    String getBlockedUsers();
    ArrayList<Message> getMessageHistory();

    // Setters
    void setUsername(String newUsername);
    void setProfilePicture(String profilePicture);

}