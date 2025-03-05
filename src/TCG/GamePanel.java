package TCG;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import EnemyCharactres.Enemy;
import EnemyCharactres.EnemyArcher;
import EnemyCharactres.EnemyCaptain;
import EnemyCharactres.EnemyGoblin;
import EnemyCharactres.EnemyHammerMan;
import EnemyCharactres.EnemyPawn;
import EnemyCharactres.EnemyPinkMon;
import EnemyCharactres.EnemyWarrior;
import EnemyCharactres.EnemyWhiteGiant;
import EnemyCharactres.Enexus;
import EnemyCharactres.Etower;
import GC.Archer;
import GC.Captain;
import GC.GameCharacter;
import GC.Goblin;
import GC.HammerMan;
import GC.Pawn;
import GC.PinkMon;
import GC.PlayerNexus;
import GC.PlayerTower;
import GC.Warrior;
import GC.WhiteGiant;

public class GamePanel extends JPanel implements MouseListener{
    static List<GameCharacter> characters;
    static List<Enemy> enemies;
    static List<Enemy> drawEnemies;
    static List<GameCharacter>drawCharacters;
    static List<Etower> etowers;
    private Timer gameTimer;
    private Timer costTimer;
    private Timer judgeTimer;
    private Timer setTextTimer;
    private CardLayout cl;
    private BufferedImage backgroundImg;
    private JLabel showCost;
    private int cost, ecost;
    private GameCharacter[]playdeck;
    private Enemy [] enemydeck;
    private StartPanel sp;
    private GameCharacter selectedCard;
    private Enemy selectedEnemyCard;
    private int mouseX, mouseY, getindex;
    int exitOption;
    private Enexus enexus;
    private Etower etower,etower2;
    private PlayerNexus ournexus;
    private PlayerTower ourtower, ourtower2;
    private JLabel showMyScore;
    private JLabel showEScore;
    private JLabel cardCost0,cardCost1,cardCost2,cardCost3;
    private int myScore, eScore;
    private int timeLimit;
    private JLabel gameTime;
    private int index0,index1,index2,index3, eindex;
    private int etime0, etime1,etime2,etime3;
    private int selectType;
    
    
    GamePanel(JPanel cards, StartPanel startpanel) {
    	sp = startpanel;
    	playdeck = sp.playdeck;
    	enemydeck = sp.enemydeck;
    	selectedCard = null;
    	selectedEnemyCard = null;
    	timeLimit =180;
    	eindex = 0;
    	selectType=0;
    	index0=0; index1=0;index2=0;index3=0;
    	etime0=0; etime1=0; etime2=0; etime3=0;
        characters = new ArrayList<>();
        enemies = new ArrayList<>();
        drawCharacters = new ArrayList<>();
        drawEnemies = new ArrayList<>();
        setLayout(null);
        try {
        	backgroundImg = ImageIO.read(new File("background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setBackground(Color.WHITE);
        backButton(cards);
        addMouseListener(this);
        initializeEnemiesTower();
        initializeOurTower();
        showScore();
        cardCost();
        showGameTime();
        showCost();
        startGameTimer(cards);
        selectEnemyCard();
    }
    
    private void showCost() {
    	showCost = new JLabel("Cost : "+getCost());
        showCost.setBounds(50, 600, 80, 30);
        showCost.setOpaque(true);
        showCost.setBackground(new Color(85, 139, 240));
        showCost.setHorizontalAlignment(JTextField.CENTER);
        showCost.setFont(new Font("Arial", Font.BOLD, 16));
        add(showCost);
    }
    private void cardCost() {
    	cardCost0 = new JLabel(Integer.toString(playdeck[0].getCost()));
    	cardCost1 = new JLabel(Integer.toString(playdeck[1].getCost()));
    	cardCost2 = new JLabel(Integer.toString(playdeck[2].getCost()));
    	cardCost3 = new JLabel(Integer.toString(playdeck[3].getCost()));
    	cardCost0.setBounds(521,648, 35, 20);// 521 562 606 648
    	cardCost1.setBounds(562,648, 35, 20);
    	cardCost2.setBounds(606,648, 35, 20);
    	cardCost3.setBounds(648,648, 35, 20);
    	cardCost0.setHorizontalAlignment(JLabel.CENTER);
    	cardCost1.setHorizontalAlignment(JLabel.CENTER);
    	cardCost2.setHorizontalAlignment(JLabel.CENTER);
    	cardCost3.setHorizontalAlignment(JLabel.CENTER);
    	cardCost0.setOpaque(true);
    	cardCost1.setOpaque(true);
    	cardCost2.setOpaque(true);
    	cardCost3.setOpaque(true);
    	cardCost0.setBackground(new Color(255, 200, 200));
    	cardCost1.setBackground(new Color(255, 200, 200));
    	cardCost2.setBackground(new Color(255, 200, 200));
    	cardCost3.setBackground(new Color(255, 200, 200));
    	add(cardCost0);
    	add(cardCost1);
    	add(cardCost2);
    	add(cardCost3);
    	
    }
    
    private void backButton(JPanel cards) {
        JButton backButton = new JButton("뒤로 가기");
        backButton.setBounds(10, 10, 100, 30);
        backButton.addActionListener(e -> {
            cleanupGame();
            cards.remove(this);
            cl = (CardLayout) cards.getLayout();
            cl.show(cards, "startPanel");
        });
        add(backButton, BorderLayout.SOUTH);
    }
    private void showScore() {
    	showMyScore = new JLabel(Integer.toString(myScore));
    	showMyScore.setBounds(557, 30, 40, 30);
    	showMyScore.setOpaque(true);
    	showMyScore.setBackground(new Color(240, 240, 240));
    	showMyScore.setHorizontalAlignment(JLabel.CENTER);
    	showMyScore.setFont(new Font("Arial", Font.BOLD, 16));
    	
    	showEScore = new JLabel(Integer.toString(eScore));
    	showEScore.setBounds(603, 30, 40, 30);
    	showEScore.setOpaque(true);
    	showEScore.setBackground(new Color(240, 240, 240));
    	showEScore.setHorizontalAlignment(JTextField.CENTER);
    	showEScore.setFont(new Font("Arial", Font.BOLD, 16));
    	add(showMyScore);
    	add(showEScore);
    }
    private void showGameTime() {
    	gameTime = new JLabel(Integer.toString(timeLimit/60)+"분 " +Integer.toString(timeLimit%60)+"초");
    	gameTime.setBounds(1000,30,80,30);
    	gameTime.setOpaque(true);
    	gameTime.setBackground(new Color(230,230,230));
    	gameTime.setHorizontalAlignment(JLabel.CENTER);
    	add(gameTime);
    }
    
  
    
    private void initializeEnemiesTower() { //포탑 초기
    	enexus = new Enexus(1064,250, characters,enemies);
    	etower = new Etower(985,74, characters,enemies);
    	etower2 = new Etower(985,442, characters,enemies);
        enemies.add(enexus);
        enemies.add(etower);
        enemies.add(etower2);
        drawEnemies.add(enexus);
        drawEnemies.add(etower);
        drawEnemies.add(etower2);
        add(enexus.getShowHP());
    	add(etower.getShowHP());
    	add(etower2.getShowHP());
        for (Enemy enemy : enemies) {
        	enemy.startGame();
        }
 
    }
    private void initializeOurTower() {
    	ournexus =new PlayerNexus(24, 250, characters, enemies);
    	ourtower =new PlayerTower(135, 74, characters, enemies);
    	ourtower2 = new PlayerTower(135, 442, characters, enemies);
    	characters.add(ournexus);
    	characters.add(ourtower);
    	characters.add(ourtower2);
    	drawCharacters.add(ournexus);
    	drawCharacters.add(ourtower);
    	drawCharacters.add(ourtower2);
    	add(ournexus.getShowHP());
    	add(ourtower.getShowHP());
    	add(ourtower2.getShowHP());
    	for (GameCharacter character : characters) {
    		character.startGame();
    	}
    }
    
    private void startGameTimer(JPanel cards) {
        gameTimer = new Timer(5, event -> {
        	repaint();
        }); 
        
        judgeTimer = new Timer(50, event-> {
        	if(timeLimit<=0) {
        		cleanupGame();
        		if(myScore>eScore) {
        			exitOption = JOptionPane.showConfirmDialog(null, "전투에 승리하였습니다\n로비로 돌아가겠습니까??","게임 종료",JOptionPane.YES_OPTION);
            		if(exitOption == JOptionPane.YES_OPTION) {
            			cards.remove(this);
                        cl = (CardLayout) cards.getLayout();
                        cl.show(cards, "startPanel");
            		}
        		}else if(myScore <eScore) {
        			exitOption = JOptionPane.showConfirmDialog(null,"전투에 패배하였습니다.\n로비로 돌아가겠습니까??","게임 종",JOptionPane.YES_OPTION);
            		if(exitOption == JOptionPane.YES_OPTION) {
            			cards.remove(this);
                        cl = (CardLayout) cards.getLayout();
                        cl.show(cards, "startPanel");
            		}
        		}else {
        			exitOption = JOptionPane.showConfirmDialog(null,"무승부입니다.\n로비로 돌아가겠습니까??","게임 종",JOptionPane.YES_OPTION);
            		if(exitOption == JOptionPane.YES_OPTION) {
            			cards.remove(this);
                        cl = (CardLayout) cards.getLayout();
                        cl.show(cards, "startPanel");
            		}
        		}
        	}
        	if(!etower.isAlive()&&!etower2.isAlive()) {
        		myScore =2;
        	}	else if(!etower.isAlive()||!etower2.isAlive()) {
        		myScore =1;
        	} else {
        		myScore =0;
        	}
        	if(!ourtower.isAlive()&&!ourtower2.isAlive()) {
        		eScore =2;
        	}	else if(!ourtower.isAlive()||!ourtower2.isAlive()) {
        		eScore =1;
        	} else {
        		eScore =0;
        	}
        	if(enexus.getWP()) {
        		cleanupGame();
        		exitOption = JOptionPane.showConfirmDialog(null, "전투에 승리하였습니다\n로비로 돌아가겠습니까??","게임 종료",JOptionPane.YES_OPTION);
        		if(exitOption == JOptionPane.YES_OPTION) {
        			cards.remove(this);
                    cl = (CardLayout) cards.getLayout();
                    cl.show(cards, "startPanel");
        		}
        	
        	}else if(ournexus.getWP()) {
        		cleanupGame();
        		exitOption = JOptionPane.showConfirmDialog(null,"전투에 패배하였습니다.\n로비로 돌아가겠습니까??","게임 종료",JOptionPane.YES_OPTION);
        		if(exitOption == JOptionPane.YES_OPTION) {
        			cards.remove(this);
                    cl = (CardLayout) cards.getLayout();
                    cl.show(cards, "startPanel");
        		}
        	
        	}
        	
        	});
        costTimer = new Timer(1000,event ->{
        	timeLimit -=1;
        	if(cost<10) {
        		cost +=1;
        		if(cost<=0) {
        			cost =0;
        		}
        		if(getCost() == 10) {
        			showCost.setBackground(new Color(207, 139, 80));
        		}
        		else {
        			 showCost.setBackground(new Color(85, 139, 240));
        		}
        	}
        	if(ecost<10) {
        		ecost +=1;
        		etime0+=1;
        		etime1+=1;
        		etime2+=1;
        		etime3+=1;
        		if(ecost<=0) {
        			ecost =0;
        		}
        	}
        	
        	if(enemydeck[eindex].getCost()<ecost) {
        		ecost -= enemydeck[eindex].getCost();
        		addEnemy(eindex);
        		if(eindex == 0) {
        			etime0=0;
        		}else if(eindex == 1) {
        			etime1 =0;
        		}else if(eindex == 2) {
        			etime2 =0;
        		} else if(eindex == 3) {
        			etime3 =0;
        		} 
        		EnemyCardLotation(eindex);
        		selectEnemyCard();
        		
        	}
        });
        setTextTimer = new Timer(5, event ->{
        	if(cost>=playdeck[0].getCost()) {
        		cardCost0.setBackground(new Color(85, 139, 240));
        	} else {
        		cardCost0.setBackground(new Color(255, 200, 200));
        	}
        	if(cost>=playdeck[1].getCost()) {
        		cardCost1.setBackground(new Color(85, 139, 240));
        	} else {
        		cardCost1.setBackground(new Color(255, 200, 200));
        	}
        	if(cost>=playdeck[2].getCost()) {
        		cardCost2.setBackground(new Color(85, 139, 240));
        	} else {
        		cardCost2.setBackground(new Color(255, 200, 200));
        	}
        	if(cost>=playdeck[3].getCost()) {
        		cardCost3.setBackground(new Color(85, 139, 240));
        	} else {
        		cardCost3.setBackground(new Color(255, 200, 200));
        	}
        	showMyScore.setText(Integer.toString(myScore));
        	showEScore.setText(Integer.toString(eScore));
        	showCost.setText("Cost : "+ Integer.toString(getCost()));
        	gameTime.setText(Integer.toString(timeLimit/60)+"분 " +Integer.toString(timeLimit%60)+"초");
        });
        gameTimer.start();
        costTimer.start();
        judgeTimer.start();
        setTextTimer.start();
    }
    
    private void cleanupGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (costTimer != null) {
        	costTimer.stop();
        }
        if (judgeTimer != null) {
        	judgeTimer.stop();
        }
        if (setTextTimer !=null) {
        	setTextTimer.stop();
        }
        for (GameCharacter character : characters) {
        	character.stopTimer();
        }
        for (Enemy enemy : enemies) {
            enemy.stopTimer();
        }
        for (GameCharacter character : drawCharacters) {
        	character.stopTimer();
        }
        for (Enemy enemy : drawEnemies) {
            enemy.stopTimer();
        }
        characters.clear();
        enemies.clear();
        drawCharacters.clear();
        drawEnemies.clear();
    }
    
    private void addCharacter(int x, int y) {
    	selectedCard.setX(x-selectedCard.getWidth()/2);
    	selectedCard.setY(y-selectedCard.getHeight()/2);
    	selectedCard.setCharacters(characters);
    	selectedCard.setEnemies(enemies);
        characters.add(selectedCard);
        drawCharacters.add(selectedCard);
        add(selectedCard.getShowHP());
        selectedCard.startGame();
        System.out.println("생성");	
    }
    
    private void addEnemy(int index) {
    	int x =(int)(Math.random()*480)+650;
    	int y =(int)(Math.random()*400)+150;
    	selectedEnemyCard = enemydeck[index];
    	makeNewEnemyObject(x,y);
    	selectedEnemyCard.setX(x);
    	selectedEnemyCard.setY(y);
    	selectedEnemyCard.setEnemies(enemies);
    	selectedEnemyCard.setCharacters(characters);
    	add(selectedEnemyCard.getShowHP());
    	enemies.add(selectedEnemyCard);
    	drawEnemies.add(selectedEnemyCard);
    	selectedEnemyCard.startGame();
    }
    private int selectEnemyCard() {
        index0 = enemydeck[0].getCost(); // 우선순위
        index1 = enemydeck[1].getCost();
        index2 = enemydeck[2].getCost();
        index3 = enemydeck[3].getCost();
        if (selectType == 0) {
            selectType = 1;
            if(index0 == index1 && index1 == index2 && index2 == index3) {
            	if (etime0 > etime1 && etime0 > etime2 && etime0 > etime3) {
                	eindex = 0;
            	}else if (etime1 > etime0 && etime1 > etime2 && etime1 > etime3) {
	                eindex = 1;
	            } else if (etime2 > etime0 && etime2 > etime1 && etime2 > etime3) {
	                eindex = 2;
	            } else if (etime3 > etime0 && etime3 > etime1 && etime3 > etime2) {
	                eindex = 3;
	            } else {
	                eindex = 0;
	            }
            }
            else if (index0 > index1 && index0 > index2 && index0 > index3) {
                   eindex = 0;
                
            } else if (index0 == index1 || index0 == index2 || index0 == index3) {
            	if (etime0 > etime1 && etime0 > etime2 && etime0 > etime3) {
                	eindex = 0;
            	}else if (etime1 > etime0 && etime1 > etime2 && etime1 > etime3) {
	                eindex = 1;
	            } else if (etime2 > etime0 && etime2 > etime1 && etime2 > etime3) {
	                eindex = 2;
	            } else if (etime3 > etime0 && etime3 > etime1 && etime3 > etime2) {
	                eindex = 3;
	            } else {
	                eindex = 0;
	            }
            }
            else if (index1 > index0 && index1 > index2 && index1 > index3) {
                    eindex = 1;
               
            }else if (index1 == index0 || index1 == index2 || index1 == index3) {
                if (etime0 > etime1 && etime0 > etime2 && etime0 > etime3) {
                    eindex = 0;
                } else if (etime1 > etime0 && etime1 > etime2 && etime1 > etime3) {
                    eindex = 1;
                } else if (etime2 > etime0 && etime2 > etime1 && etime2 > etime3) {
                    eindex = 2;
                } else if (etime3 > etime0 && etime3 > etime1 && etime3 > etime2) {
                    eindex = 3;
                } else {
                    eindex = 1;
                }
            }else if (index2 > index0 && index2 >index1 && index2 > index3) {
            	eindex = 2;
            } else if (index2 == index0 || index2 == index1 || index2 == index3) {
                if (etime0 > etime1 && etime0 > etime2 && etime0 > etime3) {
                    eindex = 0;
                } else if (etime1 > etime0 && etime1 > etime2 && etime1 > etime3) {
                    eindex = 1;
                } else if (etime2 > etime0 && etime2 > etime1 && etime2 > etime3) {
                    eindex = 2;
                } else if (etime3 > etime0 && etime3 > etime1 && etime3 > etime2) {
                    eindex = 3;
                } else {
                    eindex = 2;
                }
            } else if (index3 > index0 && index3 > index1 && index3 > index2) {
                	eindex =3;
            	}
            else if (index3 == index0 || index3 == index1 || index3 == index2) {
                if (etime0 > etime1 && etime0 > etime2 && etime0 > etime3) {
                    eindex = 0;
                } else if (etime1 > etime0 && etime1 > etime2 && etime1 > etime3) {
                    eindex = 1;
                } else if (etime2 > etime0 && etime2 > etime1 && etime2 > etime3) {
                    eindex = 2;
                } else if (etime3 > etime0 && etime3 > etime1 && etime3 > etime2) {
                    eindex = 3;
                } else {
                    eindex = 3;
                }
                }
             else {
                eindex = -1;
            }
            return eindex;
        } else {
            selectType = 0;
            if (etime0 == etime1 && etime1 == etime2 && etime2 == etime3) {
                if (index0 > index1 && index0 > index2 && index0 > index3) {
                    eindex = 0;
                } else if (index1 > index0 && index1 > index2 && index1 > index3) {
                    eindex = 1;
                } else if (index2 > index0 && index2 > index1 && index2 > index3) {
                    eindex = 2;
                } else if (index3 > index0 && index3 > index1 && index3 > index2) {
                    eindex = 3;
                } else {
                    eindex = 0;
                }
            }
            else if (etime0 > etime1 && etime0 > etime2 && etime0 > etime3) {
                    eindex = 0;
            }else if (etime0 == etime1 || etime0 == etime2 || etime0 == etime3) {
                if (index0 > index1 && index0 > index2 && index0 > index3) {
                    eindex = 0;
                } else if (index1 > index0 && index1 > index2 && index1 > index3) {
                    eindex = 1;
                } else if (index2 > index0 && index2 > index1 && index2 > index3) {
                    eindex = 2;
                } else if (index3 > index0 && index3 > index1 && index3 > index2) {
                    eindex = 3;
                } else {
                    eindex = 0;
                }
            }else if (etime1 > etime0 && etime1 > etime2 && etime1 >etime3) {
                    eindex = 1;
             
            }else if (etime1 == etime0 || etime1 == etime2 || etime1 == etime3) {
                if (index0 > index1 && index0 > index2 && index0 > index3) {
                    eindex = 0;
                } else if (index1 > index0 && index1 > index2 && index1 > index3) {
                    eindex = 1;
                } else if (index2 > index0 && index2 > index1 && index2 > index3) {
                    eindex = 2;
                } else if (index3 > index0 && index3 > index1 && index3 > index2) {
                    eindex = 3;
                } else {
                    eindex = 1;
                }
            }else if (etime2 > etime0 && etime2 > etime1 && etime2 > etime3) {
                    eindex = 2;
                
            } else if (etime2 == etime0 || etime2 == etime1 || etime2 == etime3) {
                if (index0 > index1 && index0 > index2 && index0 > index3) {
                    eindex = 0;
                } else if (index1 > index0 && index1 > index2 && index1 > index3) {
                    eindex = 1;
                } else if (index2 > index0 && index2 > index1 && index2 > index3) {
                    eindex = 2;
                } else if (index3 > index0 && index3 > index1 && index3 > index2) {
                    eindex = 3;
                } else {
                    eindex = 2;
                }
            }else if (etime3 > etime0 && etime3 > etime1 && etime3 > etime2) {
                    eindex = 3;
                
            } else if (etime3 == etime0 || etime3 == etime1 || etime3 == etime2) {
                    if (index0 > index1 && index0 > index2 && index0 > index3) {
                        eindex = 0;
                    } else if (index1 > index0 && index1 > index2 && index1 > index3) {
                        eindex = 1;
                    } else if (index2 > index0 && index2 > index1 && index2 > index3) {
                        eindex = 2;
                    } else if (index3 > index0 && index3 > index1 && index3 > index2) {
                        eindex = 3;
                    } else {
                        eindex = 3;
                    }
            }else {
                eindex = -1;
            }
            return eindex;
        }
    }



   public void drawPlayDeck(Graphics g) {
	   int cardWidth = 35, cardHeight = 68, gap = 6;
  		int i=0;
  		BufferedImage tempImg;
  		
   		for(int col=0; col<4;col++) {
   			int x = col * (cardWidth + gap);
            int y = 570;
   			tempImg= playdeck[i].getCardImg();
               g.drawImage(tempImg,x+521,y,cardWidth,cardHeight,null);
   			i++;
   			
   		}
   }
   
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0,0,null);
       
        synchronized(drawEnemies) {
        for (Enemy enemy : drawEnemies) {
        	try {
	            if (enemy.isAlive()) {
						enemy.draw(g);
						enemy.getShowHP().setLocation(enemy.getX()+enemy.getWidth()/3, enemy.getY()-10);
						enemy.getShowHP().setText(Integer.toString(enemy.getHP()));
					
	            } else {
	            	remove(enemy.getShowHP());
	            }	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        }
        synchronized (drawCharacters) {
            for (GameCharacter character : drawCharacters) {
            	try {
	                if (character.isAlive()) {
							character.draw(g);
							character.getShowHP().setLocation(character.getX()+character.getWidth()/3, character.getY()-10);
							character.getShowHP().setText(Integer.toString(character.getHP()));
	                } else {
	                	remove(character.getShowHP());
	                }
            	} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        drawPlayDeck(g);
    }
    private int getCardIndex(Point point) {
        int cardWidth = 35, cardHeight = 68, gap = 6;
        if (point.x < 521 || point.x > 679 || point.y < 600 || point.y > 668) {
            return -1; 
        }
        int col = (point.x - 521) / (cardWidth + gap); // 521 562 606 648
        int row = (point.y - 600) / (cardHeight + gap);
        int index = row * 4 + col;

        if (index >= 0 && index < playdeck.length) {
            return index; 
        } else {
            return -1; 
        }
    }

    private void CardLotation(int index) {
    	GameCharacter temp =playdeck[index];
    	playdeck[index] = playdeck[4];
    	playdeck[4] = playdeck[5];
    	playdeck[5] = playdeck[6];
    	playdeck[6] = playdeck[7];
    	playdeck[7] = temp;
    	
    }
    private void EnemyCardLotation(int index) {
    	Enemy temp = enemydeck[index];
    	enemydeck[index] = enemydeck[4];
    	enemydeck[4] = enemydeck[5];
    	enemydeck[5] = enemydeck[6];
    	enemydeck[6] = enemydeck[7];
    	enemydeck[7] = temp;
    }
    
    
    
    public int getCost() {
    	return cost;
    }
	public void setMouseX(int x) {
		this.mouseX = x;
	}
	public void setMouseY(int y) {
		this.mouseY = y;
	}
	public void setGetIndex(int getindex) {
		this.getindex =getindex;
}
	public void makeNewObject(int x, int y) {
		if (selectedCard instanceof Warrior) {
			selectedCard = new Warrior(x,y,characters,enemies);
		}else if (selectedCard instanceof Pawn) {
			selectedCard = new Pawn(x,y,characters,enemies);
		} else if (selectedCard instanceof Archer) {
			selectedCard = new Archer(x,y,characters,enemies);
		}else if (selectedCard instanceof Goblin) {
			selectedCard = new Goblin(x,y,characters,enemies);
		}else if (selectedCard instanceof WhiteGiant) {
			selectedCard = new WhiteGiant(x,y,characters,enemies);
		}else if (selectedCard instanceof HammerMan) {
			selectedCard = new HammerMan(x,y,characters,enemies);
		}else if (selectedCard instanceof PinkMon) {
			selectedCard = new PinkMon(x,y,characters,enemies);
		} else if (selectedCard instanceof Captain) {
			selectedCard = new Captain(x,y,characters,enemies);
		} else {
			selectedCard = null;
			System.out.println("생성오류");
		}
	}
	public void makeNewEnemyObject(int x, int y) {
		if (selectedEnemyCard instanceof EnemyWarrior) {
			selectedEnemyCard = new EnemyWarrior(x,y,characters,enemies);
		}else if (selectedEnemyCard instanceof EnemyPawn) {
			selectedEnemyCard = new EnemyPawn(x,y,characters,enemies);
		} else if (selectedEnemyCard instanceof EnemyArcher) {
			selectedEnemyCard = new EnemyArcher(x,y,characters,enemies);
		}else if (selectedEnemyCard instanceof EnemyGoblin) {
			selectedEnemyCard = new EnemyGoblin(x,y,characters,enemies);
		}else if (selectedEnemyCard instanceof EnemyWhiteGiant) {
			selectedEnemyCard = new EnemyWhiteGiant(x,y,characters,enemies);
		}else if (selectedEnemyCard instanceof EnemyHammerMan) {
			selectedEnemyCard = new EnemyHammerMan(x,y,characters,enemies);
		}else if (selectedEnemyCard instanceof EnemyPinkMon) {
			selectedEnemyCard = new EnemyPinkMon(x,y,characters,enemies);
		} else if (selectedEnemyCard instanceof EnemyCaptain) {
			selectedEnemyCard = new EnemyCaptain(x,y,characters,enemies);
		} else {
			selectedEnemyCard = null;
			System.out.println("생성오류");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		return;
	}

	@Override
	public void mousePressed(MouseEvent e) {	
		int index = getCardIndex(e.getPoint());
		setMouseX(e.getX()); 
		setMouseY(e.getY());
		if (index == -1) { 
	        return;
	    }
		setGetIndex(index);
		if(playdeck[index].getCost()<=cost) {
			if(e.getX()>=521&&e.getX()<=679&&e.getY()>=600&&e.getY()<=668) {
				selectedCard=playdeck[index];
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(selectedCard ==null) {
			return;
		}
		else if(e.getX()<600&&selectedCard.getCost()<=cost) {
			cost -= selectedCard.getCost();			
			System.out.println(selectedCard);
			makeNewObject(e.getX(),e.getY());
			addCharacter(e.getX(),e.getY());
			CardLotation(getindex);
			if(getindex==0) {
				cardCost0.setText(Integer.toString(playdeck[0].getCost()));
			} else if(getindex==1){
				cardCost1.setText(Integer.toString(playdeck[1].getCost()));
			}else if(getindex==2){
				cardCost2.setText(Integer.toString(playdeck[2].getCost()));
			}else if(getindex==3){
				cardCost3.setText(Integer.toString(playdeck[3].getCost()));
			}
			selectedCard = null;
		}
		else {
			selectedCard =null;
			return;
		}
		
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