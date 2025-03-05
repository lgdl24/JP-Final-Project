package GC;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import EnemyCharactres.Enemy;

public class Warrior extends GameCharacter{
	public Warrior(int x, int y, List<GameCharacter> characters, List<Enemy> enemies) {
		super(x, y, characters, enemies);
		
		setCost(3);
	    setHP(380);
	    setDamage(100);
	    setRange(1);
	    setWidth(70);
        setHeight(70);
        setAS(1000);
        setMS(120);
        initRunImg(6);
        initAttackImg(6);
	    
        try {
        	setImg(ImageIO.read(new File("image/warrior/player/default/1.png")));
        	setCardImg(ImageIO.read(new File("image/warrior/player/default/1.png")));
        	for(int i = 0; i < getRunImg().length; i++){
				   setRunImg(ImageIO.read(new File("image/warrior/player/move/"+(i+1)+".png")),i);
				  }
	     	for(int i=0;i<getAttackImg().length;i++) {
	      	setAttackImg(ImageIO.read(new File("image/warrior/player/attack/"+(i+1)+".png")),i);
	     	}
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
