package GC;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import EnemyCharactres.Enemy;

public class PlayerNexus extends GameCharacter{
	
	private boolean winPoint;
	public PlayerNexus(int x, int y, List<GameCharacter> characters, List<Enemy> enemies) {
		super(x, y, characters, enemies);
		setHP(3000);
        setDamage(100);
        setRange(100);
        setWidth(120);
        setHeight(120);
        setAS(1500);
        setMS(120);
        setAttackType(1);
        setPrSpeed(8);
        winPoint = false;
        
        try {
        	setImg(ImageIO.read(new File("image/nexus/player/default/1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

		
	}

	@Override
    public boolean isAlive() {
        if (getHP() <= 0) {
            getCharacters().remove(this);
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