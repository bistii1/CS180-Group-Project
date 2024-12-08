import javax.swing.*;

public class MessageFrame extends JFrame implements MessageFrameInterface {

    private JTextArea textArea;

    public MessageFrame(String initialMessages) {
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setText(initialMessages);

        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane);

        setTitle("Messages");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addMessage(String message) {
        textArea.append(message + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
        textArea.revalidate();
        textArea.repaint();
    }
}
