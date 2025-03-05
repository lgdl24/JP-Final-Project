package EnemyCharactres;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import EnemyCharactres.Enemy;
import GC.GameCharacter;

public class EnemyArcher extends Enemy {
	

    public EnemyArcher(int x, int y, List<GameCharacter> characters, List<Enemy> enemies) {
        super(x, y, characters, enemies);
        setCost(2);
        setHP(300);
        setDamage(50);
        setRange(150);
        setWidth(70);
        setHeight(70);
        setAS(1400);
        setMS(120);
        setAttackType(1);
        setPrSpeed(8);
        initRunImg(6);
        initAttackImg(8);
        

        try {
            setImg(ImageIO.read(new File("image/archer/enemy/default/1.png")));
            setCardImg(ImageIO.read(new File("image/archer/enemy/default/1.png")));
            for(int i = 0; i < getRunImg().length; i++){
				   setRunImg(ImageIO.read(new File("image/archer/enemy/move/"+(i+1)+".png")),i);
				  }
            for(int i=0;i<getAttackImg().length;i++) {
            	setAttackImg(ImageIO.read(new File("image/archer/enemy/attack/"+(i+1)+".png")),i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
