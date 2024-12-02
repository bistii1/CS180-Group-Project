import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import java.net.Socket;
import java.util.Arrays;

public class Client extends JFrame implements ActionListener {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String thisUserName;

    // GUI Components
    private JPanel mainPanel;
    private JButton searchUsersButton, addFriendButton, messageFriendButton, blockButton, removeFriendButton,
            viewIncomingMessagesButton, viewSentMessagesButton, deleteMessagesButton;

    public Client() {
        // Connect to server
        final int port = 2424;
        final String host = "localhost";

        try {
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Connection failed to server", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        //Database.loadDatabase("database.txt");

        // Initialize GUI
        setTitle("User Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1));

        searchUsersButton = new JButton("Search All Users");
        addFriendButton = new JButton("Add Friend");
        messageFriendButton = new JButton("Message Friend");
        blockButton = new JButton("Block User");
        removeFriendButton = new JButton("Remove Friend");
        viewIncomingMessagesButton = new JButton("View Incoming Messages");
        viewSentMessagesButton = new JButton("View Sent Messages");
        deleteMessagesButton = new JButton("Delete Messages");

        searchUsersButton.addActionListener(this);
        addFriendButton.addActionListener(this);
        messageFriendButton.addActionListener(this);
        blockButton.addActionListener(this);
        removeFriendButton.addActionListener(this);
        viewIncomingMessagesButton.addActionListener(this);
        viewSentMessagesButton.addActionListener(this);
        deleteMessagesButton.addActionListener(this);

        mainPanel.add(searchUsersButton);
        mainPanel.add(addFriendButton);
        mainPanel.add(messageFriendButton);
        mainPanel.add(blockButton);
        mainPanel.add(removeFriendButton);
        mainPanel.add(viewIncomingMessagesButton);
        mainPanel.add(viewSentMessagesButton);
        mainPanel.add(deleteMessagesButton);

        loginOrCreateAccount();
    }

    private void loginOrCreateAccount() {
        String[] options = {"Login", "Create Account"};
        String choice = (String) JOptionPane.showInputDialog(
                this, "Would you like to login or create a new account?",
                "Login or Create Account", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if ("Login".equals(choice)) {
            handleLogin();
        } else if ("Create Account".equals(choice)) {
            handleAccountCreation();
        }
    }

    private void handleLogin() {
        try {
            writer.println("Login");
            boolean loggedIn = false;

            while (!loggedIn) {
                String username = JOptionPane.showInputDialog(this, "Enter your username:", "Login", JOptionPane.QUESTION_MESSAGE);
                String password = JOptionPane.showInputDialog(this, "Enter your password:", "Login", JOptionPane.QUESTION_MESSAGE);

                writer.println(username);
                writer.println(password);

                thisUserName = reader.readLine();
                if ("Login failed".equals(thisUserName)) {
                    JOptionPane.showMessageDialog(this, "Incorrect username or password", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Welcome, " + thisUserName, "Logged In", JOptionPane.INFORMATION_MESSAGE);
                    loggedIn = true;
                    displayMainMenu();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error during login", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAccountCreation() {
        try {
            writer.println("Create account");
            boolean created = false;

            while (!created) {
                String username = JOptionPane.showInputDialog(this, "Enter a username:", "Create Account", JOptionPane.QUESTION_MESSAGE);
                String password = JOptionPane.showInputDialog(this, "Enter a password:", "Create Account", JOptionPane.QUESTION_MESSAGE);
                String profilePicture = JOptionPane.showInputDialog(this, "Enter a profile picture path:", "Create Account", JOptionPane.QUESTION_MESSAGE);

                writer.println(username);
                writer.println(password);
                writer.println(profilePicture);

                thisUserName = reader.readLine();
                if ("User already exists".equals(thisUserName)) {
                    JOptionPane.showMessageDialog(this, "User already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    //saveToDatabase(username, password, profilePicture);
                    JOptionPane.showMessageDialog(this, "Welcome, " + thisUserName, "Account Created", JOptionPane.INFORMATION_MESSAGE);
                    created = true;
                    displayMainMenu();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error during account creation", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*private void saveToDatabase(String username, String password, String profilePicture) {
        try (FileWriter fw = new FileWriter("databaseInformation.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(username + "," + password + "," + profilePicture);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }*/

    private void displayMainMenu() {
        Container content = this.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(mainPanel, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchUsersButton) {
            searchUsers();
        } else if (e.getSource() == addFriendButton) {
            addFriend();
        } else if (e.getSource() == messageFriendButton) {
            messageFriend();
        } else if (e.getSource() == blockButton) {
            blockUser();
        } else if (e.getSource() == removeFriendButton) {
            removeFriend();
        } else if (e.getSource() == viewIncomingMessagesButton) {
            try {
                viewIncomingMessages();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == viewSentMessagesButton) {
            try {
                viewSentMessages();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == deleteMessagesButton) {
            try {
                deleteMessages();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    private void searchUsers() {
        try {
            boolean searched = false;
            writer.println("Search");
            while (!searched) {
                String searchText = JOptionPane.showInputDialog(this, "Enter username to search:", "Search Users", JOptionPane.QUESTION_MESSAGE);
                writer.println(searchText);

                String response = reader.readLine();
                if ("Search failed".equals(response)) {
                    JOptionPane.showMessageDialog(this, "No users found", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Found users: " + response, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
                int result = JOptionPane.showConfirmDialog(this, "Search Again?", "Search Result", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.NO_OPTION) {
                    searched = true;
                }
                writer.println(result);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error searching users", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addFriend() {
        writer.println("Add friend");
        String friend = JOptionPane.showInputDialog(this, "Enter friend's username to add:", "Add Friend", JOptionPane.QUESTION_MESSAGE);
        writer.println(thisUserName);
        writer.println(friend);

        String condition;
        try {
            condition = reader.readLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        if (condition.equals("null")) {
            JOptionPane.showMessageDialog(this, "No friend found", "Add Friend", JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                condition = reader.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(this, condition, "Add Friend", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void blockUser() {
        writer.println("Block user");
        String blockedUser = JOptionPane.showInputDialog(this, "Enter username to block: ", "Block User", JOptionPane.QUESTION_MESSAGE);
        writer.println(thisUserName);
        writer.println(blockedUser);

        String condition;
        try {
            condition = reader.readLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        if (condition.equals("null")) {
            JOptionPane.showMessageDialog(this, "No user found", "Block User", JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                condition = reader.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(this, condition, "Block User", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void messageFriend() {
        writer.println("Message");
        boolean allFriends = JOptionPane.showConfirmDialog(this, "Do you want to send this message to all friends?", "allFriends", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        writer.println(allFriends);
        writer.println(thisUserName);
        if (allFriends) {
            String message = JOptionPane.showInputDialog(this, "Enter your message:", "Message all", JOptionPane.QUESTION_MESSAGE);
            writer.println(message);
        } else {
            String friend = JOptionPane.showInputDialog(this, "Enter friend's username to message:", "Message Friend", JOptionPane.QUESTION_MESSAGE);
            String message = JOptionPane.showInputDialog(this, "Enter your message:", "Message Friend", JOptionPane.QUESTION_MESSAGE);
            writer.println(friend + "\n" + message);
        }

    }


    private void removeFriend() {
        writer.println("Remove friend");
        String friend = JOptionPane.showInputDialog(this, "Enter friend's username to remove:", "Remove Friend", JOptionPane.QUESTION_MESSAGE);
        writer.println(thisUserName);
        writer.println(friend);

        String condition;
        try {
            condition = reader.readLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        if (condition.equals("null")) {
            JOptionPane.showMessageDialog(this, "No friend found", "Remove Friend", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Removed " + friend + "!", "Friend Removed", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void viewIncomingMessages() throws IOException {
        writer.println("View Incoming Messages");
        writer.println(thisUserName);
        MessageFrame messageFrame = new MessageFrame("Messages to " + thisUserName + "\n");
        String message = "";
        while (!((message = reader.readLine()).equals("END"))) {
            messageFrame.addMessage(message);
        }
    }

    private void viewSentMessages() throws IOException {
        writer.println("View Sent Messages");
        writer.println(thisUserName);
        MessageFrame messageFrame = new MessageFrame("Messages " + thisUserName + " sent\n");
        String message = "";
        while (!((message = reader.readLine()).equals("END"))) {
            messageFrame.addMessage(message);
        }
    }

    private void deleteMessages() throws IOException {
        writer.println("Delete Messages");
        writer.println(thisUserName);
        MessageFrame messageFrame = new MessageFrame("Messages " + thisUserName + " sent\n");
        String message = "";
        int numbers = 0;
        boolean condition = Boolean.parseBoolean(reader.readLine());
        while (!((message = reader.readLine()).equals("END"))) {
            messageFrame.addMessage(message);
            numbers++;
        }
        if (condition) {
            int[] options = new int[numbers];
            for (int i = 0; i < numbers; i++) {
                options[i] = i + 1;
            }
            Integer[] dropdownOptions = new Integer[numbers];
            for (int i = 0; i < numbers; i++) {
                dropdownOptions[i] = options[i];
            }

            Integer selectedOption = (Integer) JOptionPane.showInputDialog(null,
                    "Select a message to delete:", "Delete Message", JOptionPane.QUESTION_MESSAGE, null,
                    dropdownOptions, dropdownOptions[0]
            );
            writer.println(selectedOption);

            condition = Boolean.parseBoolean(reader.readLine());
            if (condition) {
                JOptionPane.showMessageDialog(null, "Refresh page to see change", "Message Deleted", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Client client = new Client();
                client.setVisible(true);
            }
        });
    }
}
