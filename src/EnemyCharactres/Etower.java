package EnemyCharactres;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import GC.GameCharacter;

public class Etower extends Enemy{
	
	public Etower(int x, int y, List<GameCharacter> characters, List<Enemy> enemies) {
		super(x, y, characters,enemies);
		setHP(3000);
        setDamage(35);
        setRange(200);
        setWidth(80);
        setHeight(113);
        setAttackType(1);
        setPrSpeed(15);
        setAS(500);
        
        try {
        	setImg(ImageIO.read(new File("image/tower/enemy/default/1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

		
	}

	
	@Override
	public void move() {
		isMove(0);
	}


}
