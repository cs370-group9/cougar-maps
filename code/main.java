import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        //GUI created using event dispatch thread
        SwingUtilities.invokeLater (() -> {

            //Create application window
            JFrame frame = new JFrame("CougarMaps");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
        });
    }
}
