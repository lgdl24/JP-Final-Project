package TCG;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import EnemyCharactres.Enemy;
import GC.GameCharacter;

class StartPanel extends JPanel {
	private JButton startButton;
	private JButton deckButton;
	private JButton exitButton;
	private CardLayout cl;
	private GamePanel game;
	private DeckPanel deckEditor;
	private DeckEdit de;
	private StartPanel sp;
	protected GameCharacter[] playdeck;
	protected Enemy [] enemydeck;
	int exitOption;
    StartPanel(JPanel cards) {
    	sp =this;
    	setLayout(null);
    	deckEditor = new DeckPanel(cards);
    	playdeck = new GameCharacter[8];
    	for(int i=0; i<8; i++) {
    		playdeck[i] =null;
    	}
    	enemydeck = new Enemy[8];
        startButton = new JButton("시작하기");
        deckButton = new JButton("덱 편집");
        exitButton = new JButton("게임 종료");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	DeckShuffle();
            	for(int i =0; i<8; i++) {
            		if (playdeck[i] == null) {
            			JOptionPane.showMessageDialog(null, "덱이 구성되지 않았습니다.", "덱 카드 부족", JOptionPane.WARNING_MESSAGE);
            			return;
            		}
            	}
                game = new GamePanel(cards,sp);
            	cards.add(game, "GamePanel");
                cl = (CardLayout) cards.getLayout();
                cl.show(cards, "GamePanel");
            }
        });
        
        deckButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		
        		cards.add(deckEditor, "DeckPanel");
        		cl = (CardLayout) cards.getLayout();
        		
        		cl.show(cards, "DeckPanel");
        	}
        });
        exitButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		exitOption = JOptionPane.showConfirmDialog(null, "프로그램을 종료하시겠습니까?","프로그램 종료",JOptionPane.YES_NO_OPTION);
        		if(exitOption == JOptionPane.YES_OPTION)
        			System.exit(JFrame.EXIT_ON_CLOSE);
        		else if(exitOption == JOptionPane.NO_OPTION||exitOption == JOptionPane.CLOSED_OPTION) {
        			return;
        		}
        	}
        });
        
        startButton.setBounds(550, 220, 100, 60);
        deckButton.setBounds(550, 295, 100, 60);		
        exitButton.setBounds(550, 370, 100, 60);
        
        add(startButton);
        add(deckButton);
        add(exitButton);
    }
    
    
    public void DeckShuffle() {
    	Collections.shuffle(deckEditor.deckEdit.PlayDeck);
    	Collections.shuffle(deckEditor.deckEdit.enemyDeck);
    	for (int i =0; i<8; i++) {
    	playdeck[i] = deckEditor.deckEdit.PlayDeck.get(i);
    	enemydeck[i] = deckEditor.deckEdit.enemyDeck.get(i);
    	}
    	
    }
}