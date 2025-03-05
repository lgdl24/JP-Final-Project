package EnemyCharactres;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import GC.GameCharacter;

public class EnemyCaptain extends Enemy{

	public EnemyCaptain(int x, int y, List<GameCharacter> characters, List<Enemy> enemies) {
		super(x, y, characters, enemies);
		
		setCost(4);
	    setHP(400);
	    setDamage(50);
	    setRange(10);
	    setWidth(60);
        setHeight(60);
        setAS(1200);
        setMS(150);
        initRunImg(6);
        initAttackImg(6);
        
        try {
        	setImg(ImageIO.read(new File("image/captain/enemy/default/1.png")));
        	setCardImg(ImageIO.read(new File("image/captain/enemy/default/1.png")));
        	for(int i = 0; i < getRunImg().length; i++){
				setRunImg(ImageIO.read(new File("image/captain/enemy/move/"+(i+1)+".png")),i);
				  }
	     	for(int i=0;i<getAttackImg().length;i++) {
	     		setAttackImg(ImageIO.read(new File("image/captain/enemy/attack/"+(i+1)+".png")),i);
	     	}
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
