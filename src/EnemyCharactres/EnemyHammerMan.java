package EnemyCharactres;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import GC.GameCharacter;

public class EnemyHammerMan extends Enemy{

	public EnemyHammerMan(int x, int y, List<GameCharacter> characters, List<Enemy> enemies) {
		super(x, y, characters, enemies);
		setCost(4);
        setHP(600);
        setDamage(40);
        setRange(5);
        setWidth(50);
        setHeight(60);
        setAS(300);
        setMS(50);
        initRunImg(8);
        initAttackImg(3);
       
        try {
        	setImg(ImageIO.read(new File("image/hammerman/enemy/default/1.png")));
        	setCardImg(ImageIO.read(new File("image/hammerman/enemy/default/1.png")));
        	for(int i = 0; i < getRunImg().length; i++){
				   setRunImg(ImageIO.read(new File("image/hammerman/enemy/move/"+(i+1)+".png")),i);
				  }
        	for(int i=0;i<getAttackImg().length;i++) {
        		setAttackImg(ImageIO.read(new File("image/hammerman/enemy/attack/"+(i+1)+".png")),i);
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
	}    

}
