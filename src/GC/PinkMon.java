package GC;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import EnemyCharactres.Enemy;

public class PinkMon extends GameCharacter {
    public PinkMon(int x, int y, List<GameCharacter> characters, List<Enemy> enemies) {
    	super(x, y, characters, enemies);

        setCost(5);
        setHP(450);
        setDamage(200);
        setRange(80);
        setWidth(70);
        setHeight(70);
        setAttackType(1);
        setAS(1300);
        setMS(200);
        setPrSpeed(5);
        initRunImg(6);
        initAttackImg(4);
       
        try {
        	setImg(ImageIO.read(new File("image/pinkMon/player/default/1.png")));
        	setCardImg(ImageIO.read(new File("image/pinkMon/player/default/1.png")));
        	for(int i = 0; i < getRunImg().length; i++){
				setRunImg(ImageIO.read(new File("image/pinkMon/player/move/"+(i+1)+".png")),i);
        	}
	     	for(int i=0;i<getAttackImg().length;i++) {
	     		setAttackImg(ImageIO.read(new File("image/pinkMon/player/attack/"+(i+1)+".png")),i);
	     	}
        	
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void draw(Graphics g) throws IOException {
    	if(getIsmove()) {
    		if(movenum > getRunImg().length-1) movenum = 0;
    		g.drawImage(getRunImg()[movenum], getX(), getY(),getWidth(),getHeight(),null);
    	}else if(getIsattack()) {
    		if(attacknum > getAttackImg().length-1) attacknum = 0;
    		g.drawImage(getAttackImg()[attacknum],getX(), getY(),getWidth(),getHeight(),null);
    		
    	}
    	else {
    		movenum=0;
    		attacknum=0;
    		g.drawImage(getImg(), getX(), getY(),getWidth(),getHeight(),null);
    	}
    	if(getAttackType() == 1) {
    		if(getProjectile()!=null) {
    			try {
    			getProjectile().setPrImg(ImageIO.read(new File("image/projectile/player/rock/Rock1.png")));
    			getProjectile().draw(g);
    			} catch(Exception e) {
    				
    			}
    		}
    	}
    }

}