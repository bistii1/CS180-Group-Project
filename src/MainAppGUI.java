//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//
//public class MainAppGUI extends JFrame implements ActionListener {
//    private JPanel contentPane;
//    private JButton loginButton, createAccountButton, searchUsersButton, addFriendButton,
//            messageFriendButton, blockButton, removeFriendButton,
//            viewAllIncomingMessagesButton, viewSentMessagesButton, deleteMessagesButton;
//
//    private CardLayout cardLayout;
//    private JPanel cardPanel;
//    private LoginPanel loginPanel;
//    private FeedPanel feedPanel;
//    private CreateAccountPanel createAccountPanel;
//    private Client client; // Client instance for server communication
//
//    public MainAppGUI(Client client) {
//        this.client = client;  // Initialize the client for communication
//
//        setTitle("Modern Messaging App");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setBounds(100, 100, 800, 600); // Large screen size
//
//        contentPane = new JPanel();
//        contentPane.setLayout(new BorderLayout());
//        setContentPane(contentPane);
//
//        cardLayout = new CardLayout();
//        cardPanel = new JPanel(cardLayout);
//        contentPane.add(cardPanel, BorderLayout.CENTER);
//
//        loginPanel = new LoginPanel();
//        createAccountPanel = new CreateAccountPanel();
//        feedPanel = new FeedPanel(client);
//
//        cardPanel.add(loginPanel, "Login");
//        cardPanel.add(createAccountPanel, "CreateAccount");
//        cardPanel.add(feedPanel, "Feed");
//
//        // Show the login screen first
//        cardLayout.show(cardPanel, "Login");
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == loginButton) {
//            // Show the Feed screen after successful login
//            cardLayout.show(cardPanel, "Feed");
//        }
//        else if (e.getSource() == createAccountButton) {
//            // Show the Create Account screen
//            cardLayout.show(cardPanel, "CreateAccount");
//        }
//        else if (e.getSource() == searchUsersButton) {
//            // Handle search users action
//            // You can create a new panel or dialog for searching users
//        }
//        // Handle other buttons (like add friends, send message, etc.)
//    }
//
//    // Main entry point to launch the app
//    public static void main(String[] args) {
//        // Initialize the client and GUI
//        Client client = new Client("localhost", 12345); // Assume server is on localhost and port 12345
//        EventQueue.invokeLater(() -> {
//            try {
//                MainAppGUI frame = new MainAppGUI(client);
//                frame.setVisible(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    // Define each screen panel (Login, Create Account, Feed)
//    class LoginPanel extends JPanel {
//        public LoginPanel() {
//            setLayout(new BorderLayout());
//            setBackground(Color.white);
//            // Layout for the login panel (fields, buttons)
//            JButton loginButton = new JButton("Login");
//            loginButton.addActionListener(e -> {
//                // Handle login logic
//                client.sendMessage("Login Request");
//                // If login successful, show Feed
//            });
//            add(loginButton, BorderLayout.SOUTH);
//        }
//    }
//
//    // Panel for the Feed screen
//    class FeedPanel extends JPanel {
//        private Client client;
//
//        public FeedPanel(Client client) {
//            this.client = client;
//            setLayout(new BorderLayout());
//            setBackground(Color.white);
//
//            // Feed UI elements (messages, bottom navigation bar, etc.)
//            JButton sendMessageButton = new JButton("Send Message");
//            sendMessageButton.addActionListener(e -> {
//                client.sendMessage("Hello from GUI");
//                // Handle message sending logic here
//            });
//            add(sendMessageButton, BorderLayout.SOUTH);
//        }
//    }
//
//    // Create Account panel
//    class CreateAccountPanel extends JPanel {
//        public CreateAccountPanel() {
//            setLayout(new BorderLayout());
//            setBackground(Color.white);
//            // Layout for create account fields (username, password, etc.)
//            JButton createAccountButton = new JButton("Create Account");
//            createAccountButton.addActionListener(e -> {
//                // Handle account creation logic
//                client.sendMessage("Create Account Request");
//                // After creation, show login or feed
//            });
//            add(createAccountButton, BorderLayout.SOUTH);
//        }
//    }
//}
