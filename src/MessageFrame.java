import javax.swing.*;

public class MessageFrame extends JFrame {

    private JTextArea textArea;

    public MessageFrame() {
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);

        // Set the JFrame's properties
        setTitle("All messages");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to add text dynamically
    public void addMessage(String message) {
        textArea.append(message + "\n"); // Add new message with a newline
    }
}
