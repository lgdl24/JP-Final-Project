package EnemyCharactres;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import GC.GameCharacter;

public class EnemyWhiteGiant extends Enemy{

	public EnemyWhiteGiant(int x, int y, List<GameCharacter> characters, List<Enemy> enemies) {
		super(x, y, characters, enemies);
		
		setCost(8);
        setHP(1100);
        setDamage(180);
        setRange(1);
        setWidth(80);
        setHeight(80);
        setAS(1800);
        setMS(150);
        initRunImg(6);
        initAttackImg(6);
     
    	try {
			setImg(ImageIO.read(new File("image/whiteGiant/enemy/default/1.png")));
		   	setCardImg(ImageIO.read(new File("image/whiteGiant/enemy/default/1.png")));
	    	for(int i = 0; i < getRunImg().length; i++){
				   setRunImg(ImageIO.read(new File("image/whiteGiant/enemy/move/"+(i+1)+".png")),i);
				  }
	    	for(int i=0;i<getAttackImg().length;i++) {
	    		setAttackImg(ImageIO.read(new File("image/whiteGiant/enemy/attack/"+(i+1)+".png")),i);
	    	}
	    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
    
	}
	

}
