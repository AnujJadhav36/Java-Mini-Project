import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;

public class SimpleTextEditor extends JFrame implements ActionListener {
    private JTextPane textPane;
    private JFileChooser fileChooser;

    public SimpleTextEditor() {
        // Set up the frame
        setTitle("Simple Text Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create the text pane
        textPane = new JTextPane();
        add(new JScrollPane(textPane), BorderLayout.CENTER);
        
        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
            
        // Create the File menu
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(this);
        fileMenu.add(newMenuItem);
        
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(this);
        fileMenu.add(openMenuItem);
        
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(this);
        fileMenu.add(saveMenuItem);
        
        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(this);
        fileMenu.add(saveAsMenuItem);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(this);
        fileMenu.add(exitMenuItem);
        
        // Add File menu to menu bar
        menuBar.add(fileMenu);

        // Create Edit menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.addActionListener(this);
        editMenu.add(cutMenuItem);

        JMenuItem copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.addActionListener(this);
        editMenu.add(copyMenuItem);

        JMenuItem pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.addActionListener(this);
        editMenu.add(pasteMenuItem);

        menuBar.add(editMenu);

        // Create Format menu
        JMenu formatMenu = new JMenu("Format");

        JMenuItem boldMenuItem = new JMenuItem("Bold");
        boldMenuItem.addActionListener(this);
        formatMenu.add(boldMenuItem);

        JMenuItem italicMenuItem = new JMenuItem("Italic");
        italicMenuItem.addActionListener(this);
        formatMenu.add(italicMenuItem);

        JMenuItem underlineMenuItem = new JMenuItem("Underline");
        underlineMenuItem.addActionListener(this);
        formatMenu.add(underlineMenuItem);

        // Add list options
        JMenuItem numberListMenuItem = new JMenuItem("Numbered List");
        numberListMenuItem.addActionListener(this);
        formatMenu.add(numberListMenuItem);

        JMenuItem bulletListMenuItem = new JMenuItem("Bulleted List");
        bulletListMenuItem.addActionListener(this);
        formatMenu.add(bulletListMenuItem);

        menuBar.add(formatMenu);
        
        // Set the menu bar
        setJMenuBar(menuBar);

        // Initialize the file chooser
        fileChooser = new JFileChooser();
        
        // Make the frame visible
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();

        switch (command) {
            case "New":
                textPane.setText("");
                break;
            case "Open":
                int openResult = fileChooser.showOpenDialog(this);
                if (openResult == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        textPane.read(reader, null);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error opening file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            case "Save":
                int saveResult = fileChooser.showSaveDialog(this);
                if (saveResult == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        textPane.write(writer);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error saving file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            case "Save As":
                int saveAsResult = fileChooser.showSaveDialog(this);
                if (saveAsResult == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        textPane.write(writer);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error saving file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            case "Cut":
                textPane.cut();
                break;
            case "Copy":
                textPane.copy();
                break;
            case "Paste":
                textPane.paste();
                break;
            case "Bold":
                StyleConstants.setBold(attributeSet, true);
                doc.setCharacterAttributes(textPane.getSelectionStart(), textPane.getSelectionEnd() - textPane.getSelectionStart(), attributeSet, false);
                break;
            case "Italic":
                StyleConstants.setItalic(attributeSet, true);
                doc.setCharacterAttributes(textPane.getSelectionStart(), textPane.getSelectionEnd() - textPane.getSelectionStart(), attributeSet, false);
                break;
            case "Underline":
                StyleConstants.setUnderline(attributeSet, true);
                doc.setCharacterAttributes(textPane.getSelectionStart(), textPane.getSelectionEnd() - textPane.getSelectionStart(), attributeSet, false);
                break;
            case "Numbered List":
                addListMarkers(true);
                break;
            case "Bulleted List":
                addListMarkers(false);
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }

    private void addListMarkers(boolean isNumbered) {
        try {
            int start = textPane.getSelectionStart();
            int end = textPane.getSelectionEnd();
            Document doc = textPane.getDocument();
            String text = doc.getText(start, end - start);
            String[] lines = text.split("\n");
            StringBuilder newText = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                if (isNumbered) {
                    newText.append((i + 1)).append(". ").append(lines[i]).append("\n");
                } else {
                    newText.append("• ").append(lines[i]).append("\n");
                }
            }
            doc.remove(start, end - start);
            doc.insertString(start, newText.toString(), null);
        } catch (BadLocationException ex) {
            JOptionPane.showMessageDialog(this, "Error modifying text.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleTextEditor::new);
    }
}
