package EnemyCharactres;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import GC.GameCharacter;
import GC.Projectile;

public class Enemy {
    private int x, y;       
    private int width, height;      
    private int cost;
    private int hp;         
    private int damage;    
    private int range;      
    private int cognitiveRange;
    private int attackSpeed;
    private int moveSpeed;
    private GameCharacter target;    
    private Enemy closest;           
    private List<GameCharacter> characters; // 아군 리스트
    private List<Enemy> enemies;     // 적군 리스트
    protected Timer t, attackT, moveT, exploreT;        
    private BufferedImage img;
    private Image [] runImg;
    private BufferedImage cardImg;
    private Image [] attackImg;
    private boolean inGame;
    private boolean teamCollision;
    private boolean enemyCollision;
    private boolean isattack;
    private boolean ismove;
    protected int movenum;
    protected int attacknum;
    private int attackType;
    private int prSpeed;
    private EnemyProjectile projectile;
    private Point p1, p2 ,p3 ,p4;
    private Point pA,pB;
    private JLabel showHP;
    
    
    public Enemy(int x, int y,  List<GameCharacter> characters, List<Enemy> enemies) {
        this.x = x;
        this.y = y;
        this.characters = characters;
        this.enemies = enemies;
        setWidth(30);
        setHeight(30);
        setRange(8);
        setAS(1000);
        setMS(80);
        setCost(1);
        teamCollision = false;
        enemyCollision = false;
        closest =null;
        target = null;
        setIsattack(false);
        setIsmove(false);
        movenum = 0;
        attacknum=0;
        initRunImg(12);
        attackType =0;
        prSpeed = 4;
        cognitiveRange = 200; 
        p1 = new Point(1025, 40);
        p2 = new Point(1025, 230);
        p3 = new Point(1025, 400);
        p4 = new Point(1025, 580);
        pA = new Point(950, 100);
        pB = new Point(950, 465);
        showHP = new JLabel(Integer.toString(hp));
        showHP.setBounds(x+width/2,y-10,50,10);
        showHP.setHorizontalAlignment(JLabel.CENTER);
  
        try {
			img = ImageIO.read(new File("image/blue/default/1.png"));
			cardImg = ImageIO.read(new File("image/blue/default/1.png"));
			for(int i = 0; i < runImg.length; i++){
				   runImg[i] = ImageIO.read(new File("image/blue/run/"+(i+1)+".png"));
				  }
			

		} catch (IOException e) {
			e.printStackTrace();
		}
        
        inGame = false;
        

    }

    
	public void draw(Graphics g) throws IOException {
    	if(getIsmove()) {
    		if(movenum > runImg.length-1) movenum = 0;
    		g.drawImage(runImg[movenum], x, y,width,height,null);
    	}else if(getIsattack()) {
    		if(attackImg==null) {
    			g.drawImage(img, x, y,width,height,null);
    		}else {
    		if(attacknum > attackImg.length-1) attacknum = 0;
    		g.drawImage(attackImg[attacknum], x, y,width,height,null);
    		}
    	}
    	else {
    		movenum=0;
    		attacknum=0;
    		g.drawImage(img, x, y,width,height,null);
    	}
    	try {
	    	if(attackType == 1) {
	    		if(projectile != null) {
	    	            projectile.draw(g);
	    	        }}
    	} catch(Exception e) {
    		
    	}
    }
    public void exploreUpdate() {
        findClosestOur();
        findClosestEnemy();

        if (closest == null) {
            teamCollision = false;
        } else {
            isOurCollision();
        }

        if (target == null) {
            enemyCollision = false;
        } else {
            isEnemyCollision();
        }
    }
    public void moveUpdate() {
    	if(!getIsattack()) {
	        if (teamCollision || enemyCollision) {
	        	isMove(1);
	            move(); 
	        }
	        else if (target != null && !isInRange()) {
	        	isMove(1);
	            move(); 
	        } else if (target == null) {
	        	isMove(1);
	            move(); 
	        }
    	}
    }
    public void attackUpdate() {
        if (target != null && isInRange()) {
        	isAttack(1);
        	isMove(0);
            attack(); 
        }else {
        	isAttack(0);
        }
    }

    private void findClosestEnemy() {	
        double closestDistance = 2000;
        
        try {
        	synchronized(characters) {
		        for (GameCharacter character : characters) {
		            double pdistance = getDistance(x, y, character.getX(), character.getY());
		            if (pdistance < cognitiveRange && pdistance < closestDistance) {
		                closestDistance = pdistance;
		                target = character;
		            }}
        }
    }catch(Exception e){
    	 e.printStackTrace();
    }
        
}

    private void findClosestOur() {
        double closestDistance = 2000;   
        try {
        	synchronized(enemies) {
		        for (Enemy enemy : enemies) {
		            if (enemy == this) continue; 
		            double pdistance = getDistance(x, y, enemy.getX(), enemy.getY());
		            if (pdistance < cognitiveRange && pdistance < closestDistance) {
		                closestDistance = pdistance;
		                closest = enemy;
            }
            }
        
        }
    }catch(Exception e){
    	 e.printStackTrace();
    }
    
}
    
    public boolean isInCognitiveRange() {
    	if(target ==null) return false;
    	double pdistance = getDistance(x, y, target.getX(), target.getY());
    	if (pdistance < cognitiveRange) {
    		return true;
    	} else {
    		return false;
    	}
    	
    }
    private void moveTowards(Point point) {
        if (x != point.x || y != point.y) {
            double distance = Math.sqrt(Math.pow(point.x - x, 2) + Math.pow(point.y - y, 2));
            int dx = (int) ((point.x - x) / distance);
            int dy = (int) ((point.y - y) / distance);

            if (dx == 0) dx = (point.x - x) > 0 ? 1 : -1;
            if (dy == 0) dy = (point.y - y) > 0 ? 1 : -1;

            x += dx;
            y += dy;
        }
    }


    public void move() {
        double CenterX = x + width / 2;
        double CenterY = y + height / 2;

        if(x<0) {
    		x=0;
    	}
    	if(y<0) {
    		y=0;
    	}
    	if(x>1200) {
    		x=1200;
    	}
    	if(y>700) {
    		y=700;
    	}
        
    	if (!isInCognitiveRange()&&!teamCollision&&!enemyCollision) {
    	if (y < 350) {
    	    if (x >1090 && y < 150) {
    	        moveTowards(p1);
    	    } else if (x >= 1090 && y >= 150) {
    	        moveTowards(p2);
    	    } else if (x <= 1090 && x > pA.x) {
    	        moveTowards(pA);
    	    } else if (x <= pA.x && x > 115) {
    	        if (x< 100) y++;
    	        else if (y > 200) y--;
    	        x--;
    	    } else if (x <= 115) {
    	         y++;
    	    }
    	    return;
    	} else {
    	    if (x >1090 && y < 485) {
    	        moveTowards(p3);
    	    } else if (x >= 1090 && y >= 485) {
    	        moveTowards(p4);
    	    } else if (x<= 1090 && x > pB.x) {
    	        moveTowards(pB);
    	    } else if (x <= pB.x && x > 115) {
    	        if (y < 465) y++;
    	        else if (y > 565) y--;
    	        x--;
    	    } else if (x <=115) {
    	       y--;
    	    }
    	    return;
    		}
    	}

        if (enemyCollision) {
        	isMove(0);
            double targetCenterX = target.getX() + target.getWidth() / 2;
            double targetCenterY = target.getY() + target.getHeight() / 2;
            double dx = CenterX - targetCenterX;
            double dy = CenterY - targetCenterY;
            double magnitude = Math.sqrt(dx * dx + dy * dy);
            if (magnitude != 0) {
                dx /= magnitude;
                dy /= magnitude;
            }
            double collisionSpeed = 3;
            x += dx * collisionSpeed;
            y += dy * collisionSpeed;
        } 
        if (teamCollision) {
            double closestCenterX = closest.getX() + closest.getWidth() / 2;
            double closestCenterY = closest.getY() + closest.getHeight() / 2;
            double dx = CenterX - closestCenterX;
            double dy = CenterY - closestCenterY;
            double magnitude = Math.sqrt(dx * dx + dy * dy);
            if(!closest.getIsmove()) {
            	if(y<closest.y) {
	            	x -=1;
	            	y-=1;
            	}else {
            		x -=1;
                	y+=1;	
            	}
	            
            }
            else {
	            if (magnitude != 0) {
	            	if(dx ==0) {
	            		if(CenterY > closestCenterY) {
	            			dx+=1;
	            		}else {
	            			dx-=1;
	            		}
	            		dx/=magnitude;
	            	}else if(closest.getIsattack()) {
	            		dx+=1;
	            	}
	            	else {
	            		dx /= magnitude;
	            	}
	                if(dy ==0) {
	                	if (CenterX > closestCenterX){
	                		dy +=1;
	                	}else {
	                		dy-=1;
	                	}
	                }
	                dy/=magnitude;
	            }else {
	            	dy/=magnitude;
	            }
	            double collisionSpeed = 1;
	            x += dx * collisionSpeed;
	            y += dy * collisionSpeed;
	            isMove(0);
	            return;
            	}
            }else {
            // 타겟으로 이동
            if (target != null) {
            	isMove(1);
                if (x + getWidth() / 2 < target.getX() + target.getWidth() / 2) x++;
                else if (x + getWidth() / 2 > target.getX() + target.getWidth() / 2) x--;
                if (y + getHeight() / 2 < target.getY() + target.getHeight() / 2) y++;
                else if (y + getHeight() / 2 > target.getY() + target.getHeight() / 2) y--;
            }
        }
    }

    public boolean isEnemyCollision() {
    	double ptpd = 2000; 
    	double diagonal = getDistance(x, y, x + width, y + height) / 2;
    	double tdiagonal = 0;
    	double cold;
    
    	/*
    	ptpd = getDistance(x + width / 2, y + height / 2,
    	    		target.getX() + target.getWidth() / 2,
    	    		target.getY() + target.getHeight() / 2);
    	
    	tdiagonal = getDistance(target.getX(), target.getY(), 
    	                            target.getX() + target.getWidth(), 
    	                            target.getY() + target.getHeight()) / 2;
    	
    	cold = ((width+target.getWidth()) / 2 + diagonal + tdiagonal) /2;
    	*/
    	cold = target.getWidth()/2 + (this.getWidth()/2)-3;
    	
        if (ptpd < cold) {
            return enemyCollision = true;
        }else {
        	return enemyCollision = false;	
        }
    	
    }
    public boolean isOurCollision() {
        if (closest == null) {
        	teamCollision = false; // closest가 null이면 충돌이 없다고 처리
            return teamCollision;
        }

        double ptpd = getDistance(x + width / 2, y + height / 2, 
                                   closest.getX() + closest.getWidth() / 2, 
                                   closest.getY() + closest.getHeight() / 2);
     /*   double diagonal = getDistance(x, y, x + width, y + height) / 2;
        double cdiagonal = getDistance(closest.getX(), closest.getY(), 
                                        closest.getX() + closest.getWidth(), 
                                        closest.getY() + closest.getHeight()) / 2;
        double colc = (((width + closest.getWidth()) / 2 + diagonal + cdiagonal) / 2)-5;
*/
        double cold = closest.getWidth()/2+ (this.getWidth()/2)-3;
        if (ptpd < cold) {
            teamCollision = true;
            return teamCollision;
         }
         teamCollision = false;
         return teamCollision;
    }

    private boolean isInRange() {
    	if(target !=null) {
	    	double ptpd =getDistance(x+(width/2), y+(height/2), target.getX()+(target.getWidth()/2), target.getY()
					+(target.getHeight()/2)); 
	    	double td = target.getWidth()/2;
	    	double od = this.getWidth()/2;
	    	double inRange = ptpd-od-td;//tdiagonal-diagonal;
    	
    		return inRange <= range;
    	}else {
        return false;
    }
    	}

    private double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public void attack() {
    	if (attackType ==0) {
	    	target.attacked(damage);
	        System.out.println("GameCharacter attacked Enemy! Remaining HP: " + target.getHP());
	        if (!target.isAlive()) {
	            target = null; 
	        }
    	} else if(attackType == 1) {
    		if (getTarget() != null) {
            double arrowX = getX() + getWidth() / 2;
            double arrowY = getY() + getHeight() / 2;
            double targetX = (getTarget()).getX() + getTarget().getWidth() / 2;
            double targetY = getTarget().getY() + getTarget().getHeight() / 2;
          
            projectile = new EnemyProjectile(arrowX, arrowY, targetX, targetY, 30, prSpeed);
            if (!target.isAlive()) {
	            target = null; 
	        }
        }}
    	
    }

    private GameCharacter getTarget() {
		
		return target;
	}


	public void attacked(int damage) {
        hp -= damage;
    }

    public boolean isAlive() {
        if (hp <= 0) {
        	synchronized(getEnemies()){
	            getEnemies().remove(this);
	            
	            stopTimer();
	            return false;
        }}
        return hp > 0;
    }
    
    public boolean isInGame() {
    	return inGame;
    }
    
    public void startGame() {
    	inGame = true;
        startTimer();
    }

    public void stopGame() {
    	inGame = true;
    	stopTimer();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHP() {
        return hp;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public int getCost() {
    	return cost;
    }
    
    public int getDamage() {
    	return damage;
    }
    
    public int getRange() {
    	return range;
    }
    public int getAS() {
    	return attackSpeed;
    }
    public int getMS() {
    	return moveSpeed;
    }
    public JLabel getShowHP() {
    	return showHP;
    }

    public void setHP(int hp) {
    	this.hp = hp;
    }
    
    public void setWidth(int width) {
    	this.width = width;
    }
    
    public void setHeight(int height) {
    	this.height = height;
    }
    
    public void setCost(int cost) {
    	this.cost = cost;
    }
    
    public void setDamage(int damage) {
    	this.damage = damage;
    }
    
    public void setRange(int range) {
    	this.range = range;
    }
    
    public void setAS(int attackSpeed) {
    	this.attackSpeed=attackSpeed;
    }
    public void setMS(int moveSpeed) {
    	this.moveSpeed=moveSpeed;
    }
    public void setEnemies(List<Enemy> enemies) {
    	this.enemies = enemies;    
    	}
    
    public BufferedImage getImg() {
    	return img;
    } 
    public void setTarget(GameCharacter target) {
    	this.target = target;
    }
    public void setAttackType(int i) {
    	this.attackType =i;
    }

    
    public BufferedImage setImg(BufferedImage img) {
    	return this.img = img;
    }
    public void initRunImg(int i) {
    	this.runImg = new Image[i];
    }
    public void initAttackImg(int i) {
    	this.attackImg = new Image[i];
    }

    public boolean isAttack(int i) {
    	if (i==1) {
    		setIsattack(true);
    		return getIsattack();
    	}else {
    		setIsattack(false);
    		return getIsattack();
    	}
    }
    
    
    public void startTimer() {
    	attackT = new Timer();
    	moveT = new Timer();
    	exploreT = new Timer();
        exploreT.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            	try {
            		exploreUpdate();
            		if(attackType == 1) {
                   		if(projectile!=null) {
                   			if (getTarget() != null && projectile.isCollision(getTarget())) {
                   				getTarget().attacked(getDamage()); 
                   				projectile.stopTimer();
                   				projectile= null;
                           }
                       }
                   }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            	
            }
        }, 0, 1); 
        
       
        attackT.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            	attackUpdate();
            
            }
        }, 1000, getAS());
        
        moveT.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                moveUpdate();
                movenum++;
                attacknum++;
                
            }
        }, 1000, getMS()/2);
    }

    public void stopTimer() {
        	this.exploreT.cancel();
        	this.attackT.cancel();
        	this.moveT.cancel();
        
    }

	public List<Enemy> getEnemies() {
		return enemies;
	}

	public Image [] getRunImg() {
		return runImg;
	}

	public BufferedImage getCardImg() {
		return cardImg;
	}

	public void setCardImg(BufferedImage cardImg) {
		this.cardImg = cardImg;
	}

	public Image [] getAttackImg() {
		return attackImg;
	}

	public Image setRunImg(Image img, int i) {
    	return this.runImg[i] = img;
    }
    public Image setAttackImg(Image img, int i) {
    	return this.attackImg[i] = img;
    }

	public int getPrSpeed() {
		return prSpeed;
	}
	public void setX(int x) {
		this.x=x;
	}
	public void setY(int y) {
		this.y=y;
	}
	 public void setCharacters(List<GameCharacter> characters) {
		   this.characters = characters;
	   }
	 public EnemyProjectile getProjectile() {
		 return projectile;
	 }

	public void setPrSpeed(int prSpeed) {
		this.prSpeed = prSpeed;
	}

	public boolean isMove(int i) {
    	if (i ==1) {
    		setIsmove(true);
    		return getIsmove();
    	}else {
    		setIsmove(false);
    	return getIsmove();
    	}
    }


	public boolean getIsmove() {
		return ismove;
	}


	public void setIsmove(boolean ismove) {
		this.ismove = ismove;
	}



	public boolean getIsattack() {
		return isattack;
	}
	public int getAttackType() {
		return attackType;
	}

	public void setIsattack(boolean isattack) {
		this.isattack = isattack;
	}
}

