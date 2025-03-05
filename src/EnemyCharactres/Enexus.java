package EnemyCharactres;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import GC.GameCharacter;
import TCG.GamePanel;

public class Enexus extends Enemy{

	boolean winPoint;
	public Enexus(int x, int y, List<GameCharacter> characters, List<Enemy> enemies) {
		super(x, y, characters,enemies);
		setHP(3000);
        setDamage(20);
        setRange(130);
        setWidth(120);
        setHeight(120);
        setEnemies(enemies);
        setAttackType(1);
        setPrSpeed(8);
        winPoint = false;
        
        try {
        	setImg(ImageIO.read(new File("image/nexus/enemy/default/1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	
	@Override
    public boolean isAlive() {
        if (getHP() <= 0) {
            getEnemies().remove(this);
            stopTimer();
            setWP(1);
            return false;
        }
        return getHP() > 0;
    }
	
	@Override
	public void move() {
		isMove(0);
	}
	public boolean getWP() {
		
		return winPoint;
	}
	public boolean setWP(int w) {
		if (w==1) {
			winPoint = true;
			return winPoint;
		}
		return false;
	}
}
