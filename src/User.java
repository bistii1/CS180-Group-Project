
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
    public String addFriend(User user) {
        // Read current friends if any
        String[] tempFriends = new String[0];
        if (!friends.equals("NA")) {
            tempFriends = friends.split(";");
        }

        String[] tempBlockedUsers = new String[0];
        if (!blockedUsers.equals("NA")) {
            tempBlockedUsers = blockedUsers.split(";");
        }

        String[] userTempBlockedUsers = new String[0];
        if (!user.getBlockedUsers().equals("NA")) {
            userTempBlockedUsers = user.getBlockedUsers().split(";");
        }
        System.out.println(userTempBlockedUsers);


        // Check if user is blocked before adding as friend
        if (Arrays.asList(tempBlockedUsers).contains(user.getUsername())) {
            return "You have this user blocked.";
        }

        if (Arrays.asList(userTempBlockedUsers).contains(this.getUsername())) {
            return "This user has you blocked.";
        }

        if (friends.equals("NA")) {
            friends = user.getUsername();
            user.addFriend(this);
        } else {
            if (Arrays.asList(tempFriends).contains(user.getUsername())) {
                return "This user already has " + user.getUsername() + " added as a friend.";
            } else {
                friends += ";" + user.getUsername();
                user.addFriend(this);
            }
        }
        return "Added " + user.getUsername() + " as a friend!";
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
            if (tempFriends.isEmpty()) {
                friends = "NA";
            } else if (tempFriends.size() == 1) {
                friends = tempFriends.get(0);
            } else {
                friends = String.join(";", tempFriends);
            }
            user.removeFriend(this);
        } else {
            System.out.println("This user is not in your friend list.");
        }
    }

    public String blockUser(User user) {
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

            if (tempFriends.contains(user.getUsername())) {
                tempFriends.remove(user.getUsername()); // Remove from friends if they're in the friend list
                if (tempFriends.isEmpty()) {
                    friends = "NA";
                } else if (tempFriends.size() == 1) {
                    friends = tempFriends.get(0);
                } else {
                    friends = String.join(";", tempFriends);
                }
                removeFriend(user);
                user.removeFriend(this);
                return "Friend has been blocked.";
            }
            return "This user has been blocked.";
        } else {
            return "This user has already been blocked.";
        }
    }

    public String unblockUser(User user) {
        String[] tempB;
        ArrayList<String> tempBlocked = new ArrayList<>();
        if (!blockedUsers.equals("NA")) {
            tempB = blockedUsers.split(";");
            tempBlocked = new ArrayList<>(Arrays.asList(tempB));
        }

        if (tempBlocked.contains(user.getUsername())) {
            tempBlocked.remove(user.getUsername());
            if (tempBlocked.isEmpty()) {
                blockedUsers = "NA";
            } else if (tempBlocked.size() == 1) {
                blockedUsers = tempBlocked.get(0);
            } else {
                blockedUsers = String.join(";", tempBlocked);
            }
            return "This user has been blocked";
        } else {
            return "This user was never blocked.";
        }
    }

    public boolean friendsWith(User user) {
        String[] tempF;
        ArrayList<String> tempFriends = new ArrayList<>();
        if (!friends.equals("NA")) {
            tempF = friends.split(";");
            tempFriends = new ArrayList<>(Arrays.asList(tempF));
        } else {
            return false;
        }

        if (tempFriends.contains(user.getUsername())) {
            return true;
        } else {
            return false;
        }
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
        this.messageHistory.add(message);
//        System.out.println(recipient.messageHistory);
//        System.out.println(recipient);
//        for (User user : Database.users) {
//            System.out.println(user);
//        }
    }

    public void deleteMessage(Message message) {
        this.messageHistory.remove(message);
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
