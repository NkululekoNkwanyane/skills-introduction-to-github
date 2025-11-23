import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MessageEncryptorApp {

    
    private static String encrypt(String text) {
        int shift = 3;
        StringBuilder encrypted = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                c = (char) ((c - base + shift) % 26 + base);
            }
            encrypted.append(c);
        }
        return encrypted.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MessageEncryptorApp::createAndShowGui);
    }

    private static void createAndShowGui() {
        
        JFrame frame = new JFrame("Secure Messages");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        frame.setLayout(new BorderLayout(10, 10));

        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem openItem = new JMenuItem("Open file");
        JMenuItem encryptItem = new JMenuItem("Encrypt message");
        JMenuItem saveItem = new JMenuItem("Save encrypted message");
        JMenuItem clearItem = new JMenuItem("Clear");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(openItem);
        fileMenu.add(encryptItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(clearItem);
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        
        JLabel titleLabel = new JLabel("Secure Message Encryptor", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 26));
        titleLabel.setForeground(new Color(0, 0, 160));
        frame.add(titleLabel, BorderLayout.NORTH);

        
        JTextArea plainTextArea = new JTextArea();
        JTextArea encryptedTextArea = new JTextArea();

        
        plainTextArea.setLineWrap(false);
        encryptedTextArea.setLineWrap(false);

        JScrollPane plainScroll = new JScrollPane(
                plainTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );

        JScrollPane encryptedScroll = new JScrollPane(
                encryptedTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );

        plainTextArea.setBorder(BorderFactory.createTitledBorder("Plain Message"));
        encryptedTextArea.setBorder(BorderFactory.createTitledBorder("Encrypted Message"));

        
        JPanel textPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        textPanel.add(plainScroll);
        textPanel.add(encryptedScroll);

        frame.add(textPanel, BorderLayout.CENTER);

        
        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        frame.add(statusLabel, BorderLayout.SOUTH);

        
        openItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    plainTextArea.setText(content.toString());
                    encryptedTextArea.setText("");
                    statusLabel.setText("Loaded plain text from " + file.getName());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error reading file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        encryptItem.addActionListener(e -> {
            String text = plainTextArea.getText();
            if (text.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please open or type a message first!");
                return;
            }
            String encrypted = encrypt(text);
            encryptedTextArea.setText(encrypted);
            statusLabel.setText("Encrypted successfully with Caesar shift 3");
        });

        saveItem.addActionListener(e -> {
            String encrypted = encryptedTextArea.getText();
            if (encrypted.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No encrypted message to save!");
                return;
            }
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(encrypted);
                    statusLabel.setText("Saved encrypted message to " + file.getName());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clearItem.addActionListener(e -> {
            plainTextArea.setText("");
            encryptedTextArea.setText("");
            statusLabel.setText("Cleared");
        });

        exitItem.addActionListener(e -> System.exit(0));

        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
