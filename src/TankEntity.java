import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRectf;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

public abstract class TankEntity extends Entity {
	
	protected Texture gun;
	protected Texture body;
	protected Texture bodyShot;
	// angle 0 is direct left
	protected float gunAngle;
	protected int bodyAngle = 0;
	protected int deltaAng = 5;
	public static enum GunType {
		MINIGUN,SHOTGUN,RICOCHET,CANNON,ROCKET,LASER;
	}
	protected GunType gunType = GunType.MINIGUN;
	private boolean changeGun = false;
	private int gunSizeIndex = 0;
	private int[] gunSize = new int[]{ 8,-6,-2,2,6,10,14,14,14,12,12,10,10,8,8,6,6,4,4,6,6};
	
	protected long firingInterval = 100;	// ms
	protected long lastFire;
	private int bulletIndex = 0;
	private MyBullet[] myBullets;

	public TankEntity() {
		width = 40;
        height = 40;
		halfSize = width/2;
		myBullets = new MyBullet[20];
		for (int i = 0; i < myBullets.length; i++) {
			myBullets[i] = new MyBullet(game);
		}
	}
	
	public void move(long delta,float setAng){
		float nx = x + (delta * dx) / 10;
		float ny = y + (delta * dy) / 10;
		
		if(dx != 0 || dy != 0){
			/** add anima here */
			
			if(bodyAngle > setAng){
				if(bodyAngle-setAng <= 180)
					bodyAngle -= deltaAng;
				else if(bodyAngle-setAng > 180)
					bodyAngle += deltaAng;
			}
			if(bodyAngle < setAng){
				if(setAng - bodyAngle <= 180)
					bodyAngle += deltaAng;
				else if(setAng - bodyAngle > 180)
					bodyAngle -= deltaAng;
			}
			
			if(bodyAngle == 360)
				bodyAngle = 0;
			if(bodyAngle == -deltaAng)
				bodyAngle = 360-deltaAng;
		}
		xPreMove = x;
		yPreMove = y;
		if (validLocation(nx, ny)) {
			x = (int)nx;
			y = (int)ny;
		} else if(validLocation(x, ny)){
			y = (int)ny;
		} else if(validLocation(nx, y)){
			x = (int)nx;
		}
	}
	
	public void moveBack() {
		x = xPreMove;
		y = yPreMove;
	}
	
	public void setGun(GunType gun) {
		gunType = gun;
		changeGun = true;
	}
	
	public void Fire(float initBulletX,float initBulletY,float gunRotation) {
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}
		lastFire = System.currentTimeMillis();
		
		switch(gunType){
		case MINIGUN:
			Bullet bullet = myBullets[bulletIndex ++ % myBullets.length];
			bulletIndex %= myBullets.length;
			bullet.reinitialize(initBulletX,initBulletY ,(float)-Math.cos(0.0174532925*gunRotation)*bullet.moveSpeed, (float)-Math.sin(0.0174532925*gunRotation)*bullet.moveSpeed);
			game.addEntity(bullet);
			break;
		case SHOTGUN:
			break;
		case RICOCHET:
			break;
		case CANNON:
			break;
		case ROCKET:
			break;
		case LASER:
			break;
		}
		game.soundManager.playEffect(game.SOUND_SHOT);
	}
	
	public void setGunAngle(float gunAngle) {
		this.gunAngle = gunAngle;
	}
	
	public void setBodyAngle(int bodyAngle) {
		this.bodyAngle = bodyAngle%360;
	}
	
	public void draw() {
		glPushMatrix();
		glTranslatef(x, y, 0);
        glRotatef(bodyAngle, 0f, 0f, 1f);
        glTranslatef(-x, -y, 0);
        
        body.bind();
        super.draw();
        
        if(shoted){
        	bodyShot.bind();
			super.draw();
		}
    	
		glTranslatef(x, y, 0);
        glRotatef(gunAngle - bodyAngle, 0f, 0f, 1f);
        glTranslatef(-x, -y, 0);
        
        halfSize += gunSize[gunSizeIndex];
        gun.bind();
        super.draw();
    	glPopMatrix();
    	halfSize -= gunSize[gunSizeIndex];
    	if(changeGun){
        	gunSizeIndex++;
        	gunSizeIndex %= gunSize.length;
        	if(gunSizeIndex == 0)
        		changeGun = false;
        }
	}
	
	public abstract void collidedWith(Entity other);
}