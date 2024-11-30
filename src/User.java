
import java.io.Serializable;
import java.lang.reflect.Array;
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
    private String friends;
    private String blockedUsers;
    private ArrayList<Message> messageHistory;

    // Constructor
    public User(String username, String password, String profilePicture, String friends,
                String blockedUsers, ArrayList<Message> messageHistory) {
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;
        this.friends = friends;
        this.blockedUsers = blockedUsers;
        this.messageHistory = messageHistory;
        //users.add(this);
    }

    /*public User(String username, String password, String profilePicture) {
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;
        this.friends = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
        this.messageHistory = new ArrayList<>();
        //users.add(this);
    }*/

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

    public String getFriends() {
        return friends;
    }


    public String getBlockedUsers() {
        return blockedUsers;
    }

    public ArrayList<Message> getMessageHistory() {
        return messageHistory;
    }

    // Friend Methods
    public void addFriend(User user) {
        // Read current friends if any
        String[] tempFriends = new String[0];
        if (!friends.equals("NA")) {
            tempFriends = friends.split(";");
        }

        String[] tempBlockedUsers = new String[0];
        if (!blockedUsers.equals("NA")) {
            tempBlockedUsers = blockedUsers.split(";");
        }


        // Check if user is blocked before adding as friend
        if (Arrays.asList(tempBlockedUsers).contains(user.getUsername())) {
            System.out.println("This user has been blocked.");
            return;
        }

        if (friends.equals("NA")) {
            friends = user.getUsername();
        } else {
            if (Arrays.asList(tempFriends).contains(user.getUsername())) {
                System.out.println("This user already has " + user.getUsername() + " added as a friend.");
            } else {
                friends += ";" + user.getUsername();
            }
        }
        System.out.println(friends);
    }

    public void removeFriend(User user) {
        // Read current users
        String[] temp;
        ArrayList<String> tempFriends = new ArrayList<>();
        if (!friends.equals("NA")) {
            temp = friends.split(";");
            tempFriends = new ArrayList<>(Arrays.asList(temp));
        }


        // Check if user is present in friend's list before removing as friend
        if (tempFriends.contains(user.getUsername())) {
            tempFriends.remove(user.getUsername());
            friends = String.join(";", tempFriends);
            //user.getFriends().remove(this); // Remove this user from the friend's list
        } else {
            System.out.println("This user is not in your friend list.");
        }
    }

    public void blockUser(User user) {
        String[] tempF;
        ArrayList<String> tempFriends = new ArrayList<>();
        if (!friends.equals("NA")) {
            tempF = friends.split(";");
            tempFriends = new ArrayList<>(Arrays.asList(tempF));
        }

        String[] tempB;
        ArrayList<String> tempBlocked = new ArrayList<>();
        if (!blockedUsers.equals("NA")) {
            tempB = blockedUsers.split(";");
            tempBlocked = new ArrayList<>(Arrays.asList(tempB));
        }


        // Check if user is not present in block list before blocking
        if (!tempBlocked.contains(user.getUsername())) {
            tempBlocked.add(user.getUsername());
            blockedUsers = String.join(";", tempBlocked);
            System.out.println("This user has been blocked.");

            if (tempFriends.contains(user.getUsername())) {
                tempFriends.remove(user.getUsername()); // Remove from friends if they're in the friend list
                friends = String.join(";", tempFriends);
                System.out.println("Friend has been blocked.");
            }
        } else {
            System.out.println("This user has already been blocked.");
        }
    }

    public void unblockUser(User user) {

//        blockedUsers.remove(user);
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

//    public String getFriendUsernames() {
//        String result = "";
//        // goes through arraylist of friend objects and concatonates a string with the usernames of the friends
////        for (User friend : friends) {
////            result += friend.getUsername() + ";";
////        }
//        return result;
//    }
//
//    public String getBlockedUsernames() {
//        // get blocked usernames
//        String result = "";
//       // for (User friend : blockedUsers) {
//           // result += friend.getUsername() + " ";
//       // }
//        return result;
//    }

    public void sendMessageAll(String content) {
        Database tempDatabase = new Database();
        ArrayList<User> friendUsernamesArrayList = new ArrayList<>();
        String[] friendUsernames = new String[0];
        if (!friends.equals("NA")) {
            friendUsernames = friends.split(";");
            for (String username : friendUsernames) {
                friendUsernamesArrayList.add(tempDatabase.findUser(username));
            }
        }

        for (User friend : friendUsernamesArrayList) {
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
        return getUsername() + "," + getPassword() + "," + getProfilePicture() + "," + friends + "," + blockedUsers +
                "," + messageHistory;
    }
}
