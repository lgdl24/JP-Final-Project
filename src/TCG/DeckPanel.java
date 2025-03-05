package TCG;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import GC.GameCharacter;

public class DeckPanel extends JPanel implements MouseListener{
    private CardLayout cl;
    private AllCardPanel acp;
    private PlayDeckPanel pdp;
    protected DeckEdit deckEdit;
    private GameCharacter DraggedCard;
    private CharacterAbilityPanel cap;
    private JPanel cards;

    public DeckPanel(JPanel card){
    	cards =card;
    	deckEdit = new DeckEdit();
    	acp = new AllCardPanel(deckEdit);
    	pdp = new PlayDeckPanel(deckEdit);
    	DraggedCard = new GameCharacter(0, 0, GamePanel.characters, GamePanel.enemies);
    	pdp.setBounds(450, 0, 300, 300);
        acp.setBounds(450, 300, 300, 700); 
        add(pdp);
        add(acp);
    	setLayout(null);
        setBackground(Color.WHITE);
        backButton(cards);
        addMouseListener(this);
        
    }

   	public void backButton(JPanel cards) {
        JButton backButton = new JButton("뒤로 가기");
        backButton.addActionListener(e -> {
            cl = (CardLayout) cards.getLayout();
            cl.show(cards, "startPanel");
        });
        setLayout(new BorderLayout());
        add(backButton, BorderLayout.SOUTH);
    } 
   	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        pdp.drawPlayCard(g);
        acp.drawAllCard(g);
   	}
   	
    private int getCardIndex(Point point) {
        int cardWidth = 60, cardHeight = 100, gap = 6;
        int col = (point.x-471) / (cardWidth + gap);
        int row = (point.y-300) / (cardHeight + gap);
        int index = -1;
        if(row>=0&&col>=0) {
        	index = row * 4 + col;
        }
        if (index >= 0 && index < deckEdit.Deck.size()) {
            return index;
        } else {
            return -1;
        }
    }

    
    private int getPlayDeckIndex(Point point) {
    	int cardWidth = 60, cardHeight = 100, gap = 6;
    	int col = (point.x-471) /(cardWidth + gap);
        int row = (point.y )/ (cardHeight + gap+0);
        int index = row * 4 + col;
        if(index >= 0 && index<deckEdit.PlayDeck.size())
        	return index;
        else {
        	return -1;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getY() > 300&&e.getX()>471&&e.getX()<729) {
            int index = getCardIndex(e.getPoint());
            if (index == -1) { 
                return;
            }
            DraggedCard = null;
            cap = new CharacterAbilityPanel(cards, deckEdit, index);
            cards.add(cap, "CharacterAbilityPanel");
            cl = (CardLayout) cards.getLayout();
            cl.show(cards, "CharacterAbilityPanel");
            
        }
    }
		
    @Override
    public void mousePressed(MouseEvent e) {
        int index = getCardIndex(e.getPoint());
        if (index == -1 || index >= deckEdit.Deck.size()) {
            DraggedCard = null;
            return;
        }
        DraggedCard = deckEdit.Deck.get(index);
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        if (DraggedCard == null) {
            return;
        }
        int playDeckIndex = getPlayDeckIndex(e.getPoint());

        if (playDeckIndex == -1 || playDeckIndex >= deckEdit.PlayDeck.size()) {
            DraggedCard = null;
            return;
        }
        if(e.getY()<240) {
        if (deckEdit.PlayDeck.contains(DraggedCard)) {
            JOptionPane.showMessageDialog( null,  "해당 카드가 이미 덱에 포함되어 있습니다.",  "중복 카드 선택", JOptionPane.WARNING_MESSAGE);
        } else {
            deckEdit.PlayDeck.remove(playDeckIndex);
            deckEdit.PlayDeck.add(playDeckIndex, DraggedCard);
        }
        DraggedCard = null;
        }
        repaint();
    }

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
   	
    
   	
}

class AllCardPanel extends JPanel{
		DeckEdit de;
	    public AllCardPanel(DeckEdit deckEdit) {
	        de= deckEdit;
	        setOpaque(false); 
	    }
	public void drawAllCard(Graphics g) {
   		int cardWidth = 60, cardHeight = 100, gap = 6;
   		int i=0;
   		BufferedImage tempImg;
   		
    	for(int row = 0; row < de.Deck.size()/4;row++) {
    		for(int col=0; col<4;col++) {
    			int x = col * (cardWidth + gap);
                int y = row * (cardHeight + gap);
    			tempImg= de.Deck.get(i).getCardImg();
                g.drawImage(tempImg,x+471,y+300,cardWidth,cardHeight,null);
    			i++;
    		}
    	}
    	
    }
	
	
}

class PlayDeckPanel extends JPanel{
	
	private DeckEdit de;
	public PlayDeckPanel(DeckEdit deckEdit) {
		setOpaque(false);
		de= deckEdit;
	}
	public void drawPlayCard(Graphics g) {
	    int cardWidth = 60, cardHeight = 100, gap = 6;

	    BufferedImage tempImg;

	    for (int row = 0; row < 2; row++) { 
	        for (int col = 0; col < 4; col++) { 
	            int index = row * 4 + col; 
	            int x = col * (cardWidth + gap); 
	            int y = row * (cardHeight + gap); 

	            if (index < de.PlayDeck.size()&& de.PlayDeck.get(index) != null) {
	                tempImg = de.PlayDeck.get(index).getCardImg();
	                g.drawImage(tempImg, x + 471, y, cardWidth, cardHeight, null);
	            } else {
	                g.setColor(Color.BLUE);
	                g.fillRect(x + 471, y, cardWidth, cardHeight);
	            }
	        }
	    }
	}
}

class CharacterAbilityPanel extends JPanel{
	CardLayout cl;
	DeckEdit de;
	JLabel damage, hp, cost, as, ms;
	GameCharacter previewCharacter;
	
	CharacterAbilityPanel(JPanel cards,DeckEdit deckEdit, int i){
		setLayout(null);
		de = deckEdit;
		previewCharacter = de.Deck.get(i);
		damage = new JLabel("공격력 : "+previewCharacter.getDamage());
		hp = new JLabel("체력 : "+previewCharacter.getHP());
		cost = new JLabel("코스트 : "+previewCharacter.getCost());
		as = new JLabel("공격속도 : "+ (1.0+(double)(1000-previewCharacter.getAS())/1000));
		ms = new JLabel("이동속도 : "+(1.0+(double)(1000-previewCharacter.getMS())/1000));
		cost.setBounds(650,80,80,20);
		hp.setBounds(650, 110, 80, 20);
		damage.setBounds(650, 140, 80, 20);
		as.setBounds(650, 170, 120, 20);
		ms.setBounds(650, 200, 120, 20);
		
		JButton backButton = new JButton("뒤로 가기");
        backButton.addActionListener(e -> {
            cl = (CardLayout) cards.getLayout();
            cl.show(cards, "DeckPanel");
            cl.removeLayoutComponent(this);
        });
        backButton.setBounds(450,600,300,50);
        add(backButton);
		
		add(damage);
		add(hp);
		add(cost);
		add(as);
		add(ms);
		
	}

	
	public void drawPreviewCard(Graphics g) {
	    int cardWidth = 60, cardHeight = 100, gap = 6;
	    BufferedImage previewImg = previewCharacter.getImg();
	    int x = (600-106+30);
	    int y = 125;
	    if (previewCharacter != null) {
	                g.drawImage(previewImg, x ,y, cardWidth, cardHeight, null);
	            } else {
	                g.setColor(Color.BLUE);
	                g.fillRect(x + 471, y, cardWidth, cardHeight);
	            }
	        }
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawPreviewCard(g);
	}
}
	
	



//이용해야할게 mouse pressed랑 mouse released

