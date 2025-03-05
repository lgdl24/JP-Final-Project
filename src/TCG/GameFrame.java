package TCG;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import TCG.GamePanel;
import TCG.StartPanel;

public class GameFrame extends JFrame {
	static final int SCREEN_WIDTH = 1200;
    static final int SCREEN_HEIGHT = 700;
    

    public GameFrame() {
        setTitle("Trading Card Game");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel cards = new JPanel(new CardLayout());
        StartPanel start = new StartPanel(cards);
        cards.add(start, "startPanel");
        add(cards, BorderLayout.CENTER);
        setVisible(true);
    }
}
