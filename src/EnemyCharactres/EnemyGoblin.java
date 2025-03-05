package EnemyCharactres;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import EnemyCharactres.Enemy;
import GC.GameCharacter;

public class EnemyGoblin extends Enemy{

	public EnemyGoblin(int x, int y, List<GameCharacter> characters, List<Enemy> enemies) {
		super(x, y, characters, enemies);
		
		
		setCost(5);
        setHP(600);
        setDamage(70);
        setRange(1);
        setWidth(90);
        setHeight(90);
        setAS(800);
        setMS(90);
        initRunImg(6);
        initAttackImg(6);
        
        try {
        	setImg(ImageIO.read(new File("image/goblin/enemy/default/1.png")));
        	setCardImg(ImageIO.read(new File("image/goblin/enemy/default/1.png")));
        	for(int i = 0; i < getRunImg().length; i++){
				   setRunImg(ImageIO.read(new File("image/goblin/enemy/move/"+(i+1)+".png")),i);
				  }
        	for(int i=0;i<getAttackImg().length;i++) {
         	setAttackImg(ImageIO.read(new File("image/goblin/enemy/attack/"+(i+1)+".png")),i);
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
	}


}
