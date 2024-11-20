
import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Project 4 -- User class
 *
 * User class and corresponding constructors and methods.
 *
 * @author Anubhuti Mittal, lab sec 31
 *
 * @version November 3, 2024
 *
 */

public class User implements UserInterface, Serializable {

    private static final long serialVersionUID = 1L;

    // Variables
    private String username;
    private String password;
    private String profilePicture;
    private ArrayList<User> friends;
    private ArrayList<User> blockedUsers;
    private ArrayList<Message> messageHistory;

    // Constructor
    public User(String username, String password, String profilePicture, ArrayList<User> friends,
                ArrayList<User> blockedUsers, ArrayList<Message> messageHistory) {
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;
        this.friends = friends;
        this.blockedUsers = blockedUsers;
        this.messageHistory = messageHistory;
        //users.add(this);
    }

    public User(String username, String password, String profilePicture) {
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;
        this.friends = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
        this.messageHistory = new ArrayList<>();
        //users.add(this);
    }

    // Profile Management
    public void updateProfile(String newUsername, String newPassword, String newProfilePicture) {
        setUsername(newUsername);
        setPassword(newPassword);
        setProfilePicture(newProfilePicture);
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String newUsername) {
        // Check if any other user has the same username
        // Only if no user has this username, set it
        this.username = newUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    @Override
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    // Set profile picture
//    public void setProfilePicture(String newProfilePicture) {
//        if (profilePicturePath == null || profilePicturePath.isEmpty()) {
//            throw new IllegalArgumentException("Profile picture path cannot be null or empty.");
//        }
//        this.profilePicture = profilePicturePath;
//    }

    public ArrayList<User> getFriends() {

        if (friends == null) {
            friends = new ArrayList<User>();
        }
        else {
            return friends;
        }


        return null;
    }

    public String getFriendsString() {
        return Arrays.toString(friends.toArray());
    }

    public ArrayList<User> getBlockedUsers() {
        return blockedUsers;
    }

    public ArrayList<Message> getMessageHistory() {
        return messageHistory;
    }

    // Friend Methods
    public void addFriend(User user) {
        // Check if user is blocked before adding as friend
        if (blockedUsers.contains(user)) {
            System.out.println("This user has been blocked.");
            return;
        }

        if (!friends.contains(user)) {
            friends.add(user);
            //user.addFriend(this);
            //user.getFriends().add(this); // Add this user to friend's list
        }
    }

    public void removeFriend(User user) {
        // Check if user is present in friend's list before removing as friend
        if (friends.contains(user)) {
            friends.remove(user);
            user.getFriends().remove(this); // Remove this user from the friend's list
        } else {
            System.out.println("This user is not in your friend list.");
        }
    }

    public void blockUser(User user) {
        // Check if user is not present in block list before blocking
        if (!blockedUsers.contains(user)) {
            blockedUsers.add(user);
            System.out.println("This user has been blocked.");

            if(friends.contains(user)) {
                friends.remove(user); // Remove from friends if they're in the friend list
                System.out.println("Friend has been blocked.");
            }
        } else {
            System.out.println("This user has already been blocked.");
        }
    }

    public void unblockUser(User user) {

        blockedUsers.remove(user);
    }

    // Profile Picture Methods

    // Method to load profile picture into a JLabel. Would be used in Phase 3 with more edits

//    public JLabel loadProfilePicture() {
//        JLabel label = new JLabel();
//        if (profilePicture != null) {
//            ImageIcon icon = new ImageIcon(profilePicture);
//            Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
//            label.setIcon(new ImageIcon(scaledImage));
//        } else {
//            label.setText("Profile Picture Unavailable");
//        }
//        return label;
//    }
//  }

    public String getFriendUsernames() {
        String result = "";
        // goes through arraylist of friend objects and concatonates a string with the usernames of the friends
        for (User friend : friends) {
            result += friend.getUsername() + ";";
        }
        return result;
    }

    public String getBlockedUsernames() {
        // get blocked usernames
        String result = "";
        for (User friend : blockedUsers) {
            result += friend.getUsername() + " ";
        }
        return result;
    }

    public void sendMessageAll(String content) {
        for (User friend : friends) {
            sendMessage(friend, content);
        }
    }

    public void sendMessage(User recipient, String content) {
        Message message = new Message(this, recipient, content);
        recipient.messageHistory.add(message);
        System.out.println(recipient.messageHistory);
    }

//    public String getMessage() {
////        String result = "";
////        for (Message message : messageHistory) {
////            result
////        }
////    }

    public String toString(){
        return getUsername() + "," + getPassword() + "," + getProfilePicture() + "," + getFriendUsernames() + "," + getBlockedUsernames() +
                "," + messageHistory;
    }
}
