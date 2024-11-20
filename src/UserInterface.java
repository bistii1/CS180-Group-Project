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

    void updateProfile(String newUsername, String newPassword, String newProfilePicture);

    // Friend Management
    void addFriend(User user);
    void removeFriend(User user);

    // Blocked User Management
    void blockUser(User user);
    void unblockUser(User user);

    // Getters
    String getUsername();
    String getPassword();
    String getProfilePicture();
    ArrayList<User> getFriends();

    // It is static since it is not specific to an instance variable
    static ArrayList<User> getUsers() {
        return new ArrayList<>();   // returns an empty user ArrayList
    }

    ArrayList<User> getBlockedUsers();
    ArrayList<Message> getMessageHistory();

    // Setters
    void setUsername(String newUsername);
    void setProfilePicture(String profilePicture);

}