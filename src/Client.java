import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import java.net.Socket;

public class Client extends JFrame implements ActionListener {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String thisUserName;

    // GUI Components
    private JPanel mainPanel;
    private JButton searchUsersButton;
    private JButton addFriendButton;
    private JButton sendMessageButton;
    private JButton blockButton;
    private JButton removeFriendButton;
    private JButton viewAllIncomingMessagesButton;
    private JButton viewSentMessagesButton;
    private JButton deleteMessagesButton;
    private JButton unblockButton;
    private final JButton viewFriendMessagesButton;
    private JButton viewProfileButton;

    public Client() {
        // Connect to server
        final int port = 2424;
        final String host = "localhost";

        try {
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Connection failed to server", "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        //Database.loadDatabase("database.txt");

        // Initialize GUI
        setTitle("User Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2));

        searchUsersButton = new JButton("Search All Users");
        addFriendButton = new JButton("Add Friend");
        sendMessageButton = new JButton("Send Message");
        blockButton = new JButton("Block User");
        removeFriendButton = new JButton("Remove Friend");
        viewAllIncomingMessagesButton = new JButton("View All Incoming Messages");
        viewSentMessagesButton = new JButton("View Sent Messages");
        deleteMessagesButton = new JButton("Delete Messages");
        unblockButton = new JButton("Unblock User");
        viewFriendMessagesButton = new JButton("View Friend Messages");
        viewProfileButton = new JButton("View Profile");

        searchUsersButton.addActionListener(this);
        addFriendButton.addActionListener(this);
        sendMessageButton.addActionListener(this);
        blockButton.addActionListener(this);
        removeFriendButton.addActionListener(this);
        viewAllIncomingMessagesButton.addActionListener(this);
        viewSentMessagesButton.addActionListener(this);
        deleteMessagesButton.addActionListener(this);
        unblockButton.addActionListener(this);
        viewFriendMessagesButton.addActionListener(this);
        viewProfileButton.addActionListener(this);

        mainPanel.add(searchUsersButton);
        mainPanel.add(addFriendButton);
        mainPanel.add(sendMessageButton);
        mainPanel.add(blockButton);
        mainPanel.add(removeFriendButton);
        mainPanel.add(viewAllIncomingMessagesButton);
        mainPanel.add(viewSentMessagesButton);
        mainPanel.add(deleteMessagesButton);
        mainPanel.add(unblockButton);
        mainPanel.add(viewFriendMessagesButton);
        mainPanel.add(viewProfileButton);

       loginOrCreateAccount();
    }


    private void loginOrCreateAccount() {
        // Create a JDialog
        JDialog dialog = new JDialog((Frame) null, "Login or Create Account", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        // Create a tabbed pane for Login and Create Account
        JTabbedPane tabbedPane = new JTabbedPane();

        // login Panel
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.add(new JLabel("Username:"));
        JTextField loginUsernameField = new JTextField();
        loginPanel.add(loginUsernameField);
        loginPanel.add(new JLabel("Password:"));
        JPasswordField loginPasswordField = new JPasswordField();
        loginPanel.add(loginPasswordField);

        JButton loginButton = new JButton("Login");
        loginPanel.add(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
                dialog.dispose();
            }
        });
        tabbedPane.add("Login", loginPanel);

        // Create Account Panel
        JPanel createAccountPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        createAccountPanel.add(new JLabel("New Username:"));
        JTextField createUsernameField = new JTextField();
        createAccountPanel.add(createUsernameField);
        createAccountPanel.add(new JLabel("New Password:"));
        JPasswordField createPasswordField = new JPasswordField();
        createAccountPanel.add(createPasswordField);
        createAccountPanel.add(new JLabel("Confirm Password:"));
        JPasswordField confirmPasswordField = new JPasswordField();
        createAccountPanel.add(confirmPasswordField);

        JButton createAccountButton = new JButton("Create Account");
        createAccountPanel.add(createAccountButton);
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayMainMenu();
                dialog.dispose();
            }
        });
        tabbedPane.add("Create Account", createAccountPanel);

        // Add tabbedPane to the dialog
        dialog.add(tabbedPane, BorderLayout.CENTER);

        // Add Cancel Button
        JPanel buttonPanel = new JPanel();
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Show the dialog
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
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

                String username = "";
                while (true) {
                    username = JOptionPane.showInputDialog(this, "Enter a username:", "Create Account", JOptionPane.QUESTION_MESSAGE);
                    if (!username.contains(";") && !username.equals("NA") && !username.isEmpty()) {
                        break;
                    }
                    JOptionPane.showMessageDialog(this, "Username cannot contain ';' or be 'NA' or be empty", "Error", JOptionPane.ERROR_MESSAGE);
                }
                String password = "";
                while (true) {
                    password = JOptionPane.showInputDialog(this, "Enter a password:", "Create Account", JOptionPane.QUESTION_MESSAGE);
                    if (!password.isEmpty()) {
                        break;
                    }
                    JOptionPane.showMessageDialog(this, "Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                }
                String profilePicture = JOptionPane.showInputDialog(this, "Enter a profile picture path:", "Create Account", JOptionPane.QUESTION_MESSAGE);
                if (profilePicture.isEmpty()) {
                    profilePicture = "default.jpg";
                }

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
        } else if (e.getSource() == sendMessageButton) {
            try {
                sendMessage();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == blockButton) {
            blockUser();
        } else if (e.getSource() == removeFriendButton) {
            removeFriend();
        } else if (e.getSource() == viewAllIncomingMessagesButton) {
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
        } else if (e.getSource() == unblockButton) {
            unblockUser();
        } else if (e.getSource() == viewFriendMessagesButton) {
            try {
                viewIncomingFriendMessages();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == viewProfileButton) {
            viewProfile();
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


    private void sendMessage() throws IOException {
        writer.println("Message");
//        boolean allFriends = JOptionPane.showConfirmDialog(this, "Do you want to send this message to all friends?", "allFriends", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
//        writer.println(allFriends);
//        writer.println(thisUserName);
//        if (allFriends) {
//            String message = JOptionPane.showInputDialog(this, "Enter your message:", "Message all", JOptionPane.QUESTION_MESSAGE);
//            writer.println(message);
//        } else {
//            String friend = JOptionPane.showInputDialog(this, "Enter friend's username to message:", "Message Friend", JOptionPane.QUESTION_MESSAGE);
//            String message = JOptionPane.showInputDialog(this, "Enter your message:", "Message Friend", JOptionPane.QUESTION_MESSAGE);
//            writer.println(friend + "\n" + message);
//        }
        writer.println(thisUserName);
        String friend = JOptionPane.showInputDialog(this, "Enter username to message:", "Send Message", JOptionPane.QUESTION_MESSAGE);
        String message = JOptionPane.showInputDialog(this, "Enter your message:", "Send Message", JOptionPane.QUESTION_MESSAGE);
        writer.println(friend + "\n" + message);
        String condition = reader.readLine();
        if (condition.equals("null")) {
            JOptionPane.showMessageDialog(this, "No user found", "Send Message", JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                condition = reader.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(this, condition, "Send Message", JOptionPane.INFORMATION_MESSAGE);
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
            try {
                condition = reader.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(this, condition, "Remove friend", JOptionPane.INFORMATION_MESSAGE);
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

    private void unblockUser() {
        writer.println("Unblock user");
        String unblockedUser = JOptionPane.showInputDialog(this, "Enter username to unblock: ", "Unblock User", JOptionPane.QUESTION_MESSAGE);
        writer.println(thisUserName);
        writer.println(unblockedUser);

        String condition;
        try {
            condition = reader.readLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        if (condition.equals("null")) {
            JOptionPane.showMessageDialog(this, "No user found", "Unblock User", JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                condition = reader.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(this, condition, "Unblock User", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void viewIncomingFriendMessages() throws IOException {
        writer.println("View Incoming Friend Messages");
        writer.println(thisUserName);
        MessageFrame messageFrame = new MessageFrame("Messages to " + thisUserName + " from friends\n");
        String message = "";
        while (!((message = reader.readLine()).equals("END"))) {
            messageFrame.addMessage(message);
        }
    }

    private void viewProfile() {
        writer.println("View Profile");
        String friend = JOptionPane.showInputDialog(this, "Enter username to view:", "View profile", JOptionPane.QUESTION_MESSAGE);
        writer.println(thisUserName);
        writer.println(friend);
        String condition;
        try {
            condition = reader.readLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        if (condition.equals("null")) {
            JOptionPane.showMessageDialog(this, "No user found", "View profile", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String profilePicture;
            try {
                profilePicture = reader.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            String description;
            try {
                description = reader.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JLabel label = new JLabel();
            ImageIcon icon = new ImageIcon(profilePicture);
            Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImage));

            label.setText("<html><center>" + description + "</center></html>");
            label.setHorizontalTextPosition(SwingConstants.CENTER);
            label.setVerticalTextPosition(SwingConstants.BOTTOM);

            JOptionPane.showMessageDialog(this, label, "View profile: " + friend, JOptionPane.PLAIN_MESSAGE);
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