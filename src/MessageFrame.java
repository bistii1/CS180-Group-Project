import javax.swing.*;

public class MessageFrame extends JFrame {

    private JTextArea textArea;

    public MessageFrame(String initialMessages) {
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setText(initialMessages);

        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane);

        setTitle("All messages");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addMessage(String message) {
        System.out.println("Adding message: " + message); // Debug log
        textArea.append(message + "\n"); // Append message
        textArea.setCaretPosition(textArea.getDocument().getLength()); // Auto-scroll
        textArea.revalidate(); // Ensure layout is updated
        textArea.repaint(); // Force repaint
    }
}
