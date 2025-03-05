package GC;

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
import javax.swing.JTextField;

import EnemyCharactres.Enemy;

public class GameCharacter {
    private int x, y;       
    private int width, height;      
    private int cost;
    private int hp;        
    private int damage;     
    private int range;      
    private int cognitiveRange;
    private int attackSpeed;
    private int moveSpeed;
    private Enemy target;    
    private GameCharacter closest;           
    private List<GameCharacter> characters; 
    private List<Enemy> enemies;   
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
    private Projectile projectile;
    private Point p1, p2 ,p3 ,p4;
    private Point pA,pB;
    private JLabel showHP;

    public GameCharacter(int x, int y,  List<GameCharacter> characters, List<Enemy> enemies) {
        this.x = x;
        this.y = y;
        this.characters = characters;
        this.enemies = enemies;
        setWidth(30);
        setHeight(30);
        setRange(1);
        setHP(100);
        setAS(1000);
        setMS(80);
        setCost(1);
        setDamage(2);
        cognitiveRange = 200; 
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
        prSpeed = 0;
        p1 = new Point(175, 40);
        p2 = new Point(175, 230);
        p3 = new Point(175, 400);
        p4 = new Point(175, 600);
        pA = new Point(330, 100);
        pB = new Point(330, 465);
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
    	if(attackType == 1) {
    		if(projectile!=null) {
    			projectile.draw(g);
    		}
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
        	synchronized(enemies){
		        for (Enemy enemy : enemies) {
		            double pdistance = getDistance(x, y, enemy.getX(), enemy.getY());
		            if (pdistance < cognitiveRange && pdistance < closestDistance) {
		                closestDistance = pdistance;
		                target = enemy;
		            }
		        }
	        }
        }catch(Exception e){
        	e.printStackTrace();
    }
        
}
    private void findClosestOur() {
        double closestDistance = 2000;   
        try {
        	synchronized(characters) {
		        for (GameCharacter character: characters) {
		            if (character == this) continue; 
		            double pdistance = getDistance(x, y, character.getX(), character.getY());
		            if (pdistance < cognitiveRange && pdistance < closestDistance) {
		                closestDistance = pdistance;
		                closest = character;
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
	    	    if (x < 135 && y < 150) {
	    	        moveTowards(p1);
	    	    } else if (x <= 135 && y >= 150) {
	    	        moveTowards(p2);
	    	    } else if (x >= 135 && x < pA.x) {
	    	        moveTowards(pA);
	    	    } else if (x >= pA.x && x < 1090) {
	    	        if (y < 100) y++;
	    	        else if (y > 200) y--;
	    	        x++;
	    	    } else if (x >= 1090) {
	    	        y++;
	    	    }
	    	    return;
	    	} else {
	    	    if (x < 135 && y < 485) {
	    	        moveTowards(p3);
	    	    } else if (x <= 135 && y >= 485) {
	    	        moveTowards(p4);
	    	    } else if (x>= 135 && x < pB.x) {
	    	        moveTowards(pB);
	    	    } else if (x >= pB.x && x < 1090) {
	    	        if (y < 465) y++;
	    	        else if (y > 560) y--;
	    	        x++;
	    	    } else if (x >= 1090) {
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
            double collisionSpeed = 2;
            x += dx * collisionSpeed;
            y += dy * collisionSpeed;
            return;
        } 
        if (teamCollision) {
            double closestCenterX = closest.getX() + closest.getWidth() / 2;
            double closestCenterY = closest.getY() + closest.getHeight() / 2;
            double dx = CenterX - closestCenterX;
            double dy = CenterY - closestCenterY;
            double magnitude = Math.sqrt(dx * dx + dy * dy);
            if(!closest.getIsmove()) {
            	if(y<closest.y) {
	            	x +=2;
	            	y-=2;
            	}else {
            		x +=2;
                	y+=2;	
            	}
            }
            else {
            	if(dx ==0) {
	            	if(CenterY > closestCenterY) {
	            		dx+=1;
	            	}else {
	            		dx-=1;
	            		}
	            	}
	            		dx/=magnitude;
	           if(dy ==0) {
	               if (CenterX > closestCenterX){
	                		dy +=1;
	                	}else {
	                		dy-=1;
	                	}
	                }
	                dy/=magnitude;
	            
	            double collisionSpeed = 1;
	            x += dx * collisionSpeed;
	            y += dy * collisionSpeed;
	            isMove(0);
	            return;
            }
        } else {
            // 타겟으로 이동
            if (target != null) {
            	isMove(1);
                if (x + getWidth() / 2 < target.getX() + target.getWidth() / 2) x++;
                else if (x + getWidth() / 2 > target.getX() + target.getWidth() / 2) x--;
                if (y + getHeight() / 2 < target.getY() + target.getHeight() / 2) y++;
                else if (y + getHeight() / 2 > target.getY() + target.getHeight() / 2) y--;
            }
            return;
        }
    }

    public boolean isEnemyCollision() {
    	if(target == null) {
    		enemyCollision = false;
    		return enemyCollision;
    	}
    	double ptpd = getDistance(x + width / 2, y + height / 2, 
                target.getX() + target.getWidth() / 2, 
                target.getY() + target.getHeight() / 2);
    	double cold = target.getWidth()/2 + (this.getWidth()/2)-3;
        if (ptpd < cold) {
            return enemyCollision = true;
        }else {
        	return enemyCollision = false;	
        }
    }
    public boolean isOurCollision() {
        if (closest == null) {
            teamCollision = false; 
            return teamCollision;
        }
        double ptpd = getDistance(x + width / 2, y + height / 2, 
                                   closest.getX() + closest.getWidth() / 2, 
                                   closest.getY() + closest.getHeight() / 2);
        double cold = (closest.getWidth()/2) +(this.getWidth()/2)-3;
        if (ptpd < cold) {
           teamCollision = true;
           return teamCollision;
        }	
        teamCollision = false;
        return teamCollision;
    }

    public boolean isInRange() {
    	if(target !=null) {
	    	double ptpd =getDistance(x+(width/2), y+(height/2), target.getX()+(target.getWidth()/2), target.getY()
					+(target.getHeight()/2)); 
	    	double td = target.getWidth()/2;
	    	double od = this.getWidth()/2;
	    	double inRange = ptpd-od-td;
    	
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
	        if (!target.isAlive()) {
	            target = null; 
	        }
    	} else if(attackType == 1) {
    		if (getTarget() != null) {
	            double arrowX = getX() + getWidth() / 2;
	            double arrowY = getY() + getHeight() / 2;
	            double targetX = getTarget().getX() + getTarget().getWidth() / 2;
	            double targetY = getTarget().getY() + getTarget().getHeight() / 2;
	            projectile = new Projectile(arrowX, arrowY, targetX, targetY, 30, prSpeed);
            if (!target.isAlive()) {
	            target = null; 
	        }
        }}
    	
    }

    public void attacked(int damage) {
        hp -= damage;
    }

    public boolean isAlive() {
        if (hp <= 0) {
        	synchronized(characters) {
            characters.remove(this);
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
    	inGame =false;
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
    public Enemy getTarget() {
    	return target;
    }
    public int getAttackType() {
    	return attackType;
    }
    public int getPrSpeed() {
    	return prSpeed;
    }
    public void setX(int x) {
    	this.x=x;
    }
    public void setY(int y) {
    	this.y =y;
    }

    public void setHP(int hp) {
    	this.hp = hp;
    }
    public JLabel getShowHP() {
    	return showHP;
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
    	this.damage = damage*2;
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
    public void setTarget(Enemy enemy) {
    	this.target = enemy;
    }
    public void setAttackType(int i) {
    	this.attackType =i;
    }
    public void setPrSpeed(int i) {
    	this.prSpeed = i;
    }
    public void initRunImg(int i) {
    	this.runImg = new Image[i];
    }
    public void initAttackImg(int i) {
    	this.attackImg = new Image[i];
    }
    
    public BufferedImage getImg() {
    	return img;
    } 
    public BufferedImage getCardImg() {
    	return cardImg;
    } 
    public Image[]getRunImg(){
    	return runImg;
    }
    public Image[] getAttackImg() {
    	return attackImg;
    }
    
    public BufferedImage setImg(BufferedImage img) {
    	return this.img = img;
    }
    
    public BufferedImage setCardImg(BufferedImage img) {
    	return this.cardImg = img;
    }
    public Image setRunImg(Image img, int i) {
    	return this.runImg[i] = img;
    }
    public Image setAttackImg(Image img, int i) {
    	return this.attackImg[i] = img;
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
    public boolean isMove(int i) {
    	if (i ==1) {
    		setIsmove(true);
    		return getIsmove();
    	}else {
    		setIsmove(false);
    	return getIsmove();
    	}
    }
   public void setCharacters(List<GameCharacter> characters) {
	   this.characters = characters;
   }
   public void setEnemies(List<Enemy>enemies) {
	   this.enemies = enemies;
   }
   public List<GameCharacter> getCharacters() {
	   return characters;
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
                   			if (getTarget() != null && projectile.isCollision(target)) {
                   				getTarget().attacked(damage); 
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

	public boolean getIsmove() {
		return ismove;
	}

	public void setIsmove(boolean ismove) {
		this.ismove = ismove;
	}

	public boolean getIsattack() {
		return isattack;
	}

	public void setIsattack(boolean isattack) {
		this.isattack = isattack;
	}

	public Projectile getProjectile() {
		return projectile;
	}
}
