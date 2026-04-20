import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.OutputStream;
import java.io.PrintStream;

public class ChatAppGUI extends JFrame {

    private ChatService chatService;
    private JTextPane consoleArea;

    // Components
    private JTextField userIdField, userNameField;
    private JTextField senderField, receiverField, messageField;
    private JTextField histUser1Field, histUser2Field;
    private JTextField parentGroupField, newGroupField;
    private JTextField groupUserIdField, targetGroupField;

    public ChatAppGUI() {
        chatService = new ChatService();

        // Setup Main Frame
        setTitle("Chat Application Backend Simulator");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 244, 248)); // Light grayish-blue background
        setLocationRelativeTo(null);

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        header.setBackground(new Color(15, 23, 42)); // Slate 900
        JLabel title = new JLabel("Data Structures Backend Simulator");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Split View
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(0); // Hide divider
        splitPane.setBorder(null);

        // Wrap the control panel in a ScrollPane so all sections are accessible
        JScrollPane controlScroll = new JScrollPane(createControlPanel());
        controlScroll.setBorder(null);
        controlScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        controlScroll.getVerticalScrollBar().setUnitIncrement(16);

        splitPane.setLeftComponent(controlScroll);
        splitPane.setRightComponent(createConsolePanel());
        add(splitPane, BorderLayout.CENTER);

        // Redirect System streams to the TextPane
        redirectSystemStreams();
        
        System.out.println("=================================================");
        System.out.println(">>> SYSTEM INITIALIZED SUCCESSFULLY.");
        System.out.println(">>> Core Data Structures Loaded: ");
        System.out.println("    - HashMap (User lookups & History mapping)");
        System.out.println("    - Queue (Message FIFO delivery)");
        System.out.println("    - LinkedList (Chat logs sequence)");
        System.out.println("    - Tree (Group hierarchy)");
        System.out.println("=================================================\n");
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));
        panel.setBackground(new Color(240, 244, 248));
        // Removed the panel.setPreferredSize(new Dimension(400, 0)); which was breaking the scrollbar!

        // Create cards for each section
        panel.add(createCardPanel("1. Register User (Hash Table)", new JPanel[] {
            createInputRow("User ID:", userIdField = new JTextField()),
            createInputRow("Name:", userNameField = new JTextField())
        }, createSolidButton("Register User", new Color(59, 130, 246), e -> {
            chatService.addUser(userIdField.getText(), userNameField.getText());
            userIdField.setText(""); userNameField.setText("");
        })));

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        panel.add(createCardPanel("2. Queue Message (Queue DS)", new JPanel[] {
            createInputRow("Sender ID:", senderField = new JTextField()),
            createInputRow("Receiver ID:", receiverField = new JTextField()),
            createInputRow("Message:", messageField = new JTextField())
        }, createSolidButton("Push to Queue", new Color(16, 185, 129), e -> {
            chatService.sendMessage(senderField.getText(), receiverField.getText(), messageField.getText());
            messageField.setText("");
        })));

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        panel.add(createCardPanel("3. View Logs (Linked List)", new JPanel[] {
            createInputRow("User 1:", histUser1Field = new JTextField()),
            createInputRow("User 2:", histUser2Field = new JTextField())
        }, createSolidButton("Extract History", new Color(245, 158, 11), e -> 
            chatService.viewChatHistory(histUser1Field.getText(), histUser2Field.getText())
        )));

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 4. Manage Groups (Tree DS)
        panel.add(createCardPanel("4. Manage Groups (Tree DS)", new JPanel[] {
            createInputRow("Parent Group:", parentGroupField = new JTextField()),
            createInputRow("New Group Name:", newGroupField = new JTextField())
        }, createSolidButton("Create Group", new Color(139, 92, 246), e -> {
            chatService.createGroup(parentGroupField.getText(), newGroupField.getText());
            parentGroupField.setText(""); newGroupField.setText("");
        })));

        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Add User to Group section within the same conceptually (or separate it, but sticking to Card format)
        panel.add(createCardPanel("Add User to Group", new JPanel[] {
            createInputRow("User ID:", groupUserIdField = new JTextField()),
            createInputRow("Group Name:", targetGroupField = new JTextField())
        }, createSolidButton("Add User", new Color(139, 92, 246), e -> {
            chatService.addUserToGroup(groupUserIdField.getText(), targetGroupField.getText());
            groupUserIdField.setText(""); targetGroupField.setText("");
        })));

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 5. Critical Backend Actions
        JPanel actionCard = new JPanel();
        actionCard.setLayout(new BoxLayout(actionCard, BoxLayout.Y_AXIS));
        actionCard.setBackground(Color.WHITE);
        actionCard.setBorder(new CompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        actionCard.add(createSectionTitle("5. Execution Actions"));
        actionCard.add(Box.createRigidArea(new Dimension(0, 10)));
        actionCard.add(createSolidButton("Execute Queue Delivery", new Color(99, 102, 241), e -> chatService.deliverMessages()));
        actionCard.add(Box.createRigidArea(new Dimension(0, 10)));
        actionCard.add(createSolidButton("Print Group Tree", new Color(236, 72, 153), e -> chatService.printGroupTree()));
        
        panel.add(actionCard);

        return panel;
    }

    private JPanel createCardPanel(String title, JPanel[] inputs, JButton btn) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true), // rounded-ish look via standard line border
            new EmptyBorder(15, 15, 15, 15)
        ));

        card.add(createSectionTitle(title));
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        for (JPanel input : inputs) {
            card.add(input);
        }
        
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(btn);
        return card;
    }

    private JPanel createConsolePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 0, 20, 20));
        panel.setBackground(new Color(240, 244, 248));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(30, 30, 30));
        wrapper.setBorder(new CompoundBorder(
            new LineBorder(new Color(60, 60, 60), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel logTitle = new JLabel(" TERMINAL OUTPUT");
        logTitle.setFont(new Font("Consolas", Font.BOLD, 14));
        logTitle.setForeground(new Color(200, 200, 200));
        logTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        wrapper.add(logTitle, BorderLayout.NORTH);

        consoleArea = new JTextPane();
        consoleArea.setEditable(false);
        consoleArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        consoleArea.setBackground(new Color(30, 30, 30));
        consoleArea.setForeground(new Color(74, 222, 128)); // Hacker Green
        consoleArea.setCaretColor(Color.WHITE);

        JScrollPane scroll = new JScrollPane(consoleArea);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setBackground(new Color(40, 40, 40));
        wrapper.add(scroll, BorderLayout.CENTER);

        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    // --- Aesthetic Helpers ---

    private JLabel createSectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(30, 41, 59));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel createInputRow(String labelText, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(100, 116, 139));
        lbl.setPreferredSize(new Dimension(80, 30));
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(203, 213, 225), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        
        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        row.setBorder(new EmptyBorder(0, 0, 10, 0));
        return row;
    }

    private JButton createSolidButton(String text, Color bg, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setPreferredSize(new Dimension(300, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(bg.darker());
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        
        if (action != null) {
            btn.addActionListener(action);
        }
        return btn;
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(final int b) { updateTextPane(String.valueOf((char) b)); }
            @Override
            public void write(byte[] b, int off, int len) { updateTextPane(new String(b, off, len)); }
            @Override
            public void write(byte[] b) { write(b, 0, b.length); }
        };
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    private void updateTextPane(final String text) {
        SwingUtilities.invokeLater(() -> {
            try {
                consoleArea.getDocument().insertString(consoleArea.getDocument().getLength(), text, null);
                consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
            } catch (Exception ignored) {}
        });
    }
}

