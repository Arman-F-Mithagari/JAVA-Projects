import javax.swing.JFrame;
public class SnakeGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");//ItCreate's game window
        GamePanel panel = new GamePanel();//It Add's custome panel
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack(); //by using this we can size to panel
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);//it Centers the screen
        panel.requestFocusInWindow();
    }
}



