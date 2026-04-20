import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        
        // Magically transforms retro Java buttons into native modern Windows/Mac elements!
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Enqueue the GUI creation on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> {
            ChatAppGUI gui = new ChatAppGUI();
            gui.setVisible(true);
        });
    }
}
