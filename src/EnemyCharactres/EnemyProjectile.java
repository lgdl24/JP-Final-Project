package EnemyCharactres;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import EnemyCharactres.Enemy;
import GC.GameCharacter;

public class EnemyProjectile {
    private double x, y; 
    private double speedX, speedY; 
    private int size;
    private Timer moveTimer;
    private BufferedImage projectileImg;

    public EnemyProjectile(double x, double y, double targetX, double targetY, int size, int speed) {
        this.x = x;
        this.y = y;
        this.size = size;
        double dx = targetX - x;
        double dy = targetY - y;
        double magnitude = Math.sqrt(dx * dx + dy * dy);
        this.speedX = (dx / magnitude) * speed; 
        this.speedY = (dy / magnitude) * speed; 
        try {
			projectileImg = ImageIO.read(new File("image/projectile/enemy/arrows/1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        startTimer();
    }

    public void startTimer() {
        moveTimer = new Timer();
        moveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                move();
            }
        }, 0, 20); 
    }
    public void stopTimer() {
        if (moveTimer != null) {
            moveTimer.cancel();
        }
    }
    public void move() {
        x += speedX;
        y += speedY;
    }

    public void draw(Graphics g) {
        g.drawImage(projectileImg, (int)x,(int)y,size,size,null);
    }

    public boolean isCollision(GameCharacter target) {
        double centerX = x + size / 2;
        double centerY = y + size / 2;
        double targetCenterX = target.getX() + target.getWidth() / 2;
        double targetCenterY = target.getY() + target.getHeight() / 2;
        double distance = Math.sqrt(Math.pow(centerX - targetCenterX, 2) + Math.pow(centerY - targetCenterY, 2));
        return distance < (size / 2 + target.getWidth() / 2); 
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public BufferedImage getPrImg() {
    	return projectileImg;
    }
    
    public void setPrImg(BufferedImage img) {
    	this.projectileImg = img;
    }
    
}
