import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.HashMap;
import java.util.Random;

import org.newdawn.slick.opengl.Texture;

public abstract class TankEntity extends Entity {
	
	protected Texture gun1;
	protected Texture gun2;
	protected Texture gun3;
	protected Texture gun4;
	protected Texture gun5;
	protected Texture gun6;
	protected Texture gun;
	protected Texture body;
	protected float speed = 2;
	// angle 0 is direct left
	protected float gunAngle;
	protected int bodyAngle = 0;
	protected int deltaAng = 5;
	public static enum GunType {
		MINIGUN,SHOTGUN,RICOCHET,CANNON,ROCKET,LASER;
	}
	protected GunType gunType = GunType.MINIGUN;
	/** unlockGun true = unlock */
	public HashMap<GunType, Boolean> unlockGun = new HashMap<GunType, Boolean>();
	private boolean changeGun = false;
	private int gunSizeIndex = 0;
	private int[] gunSize = new int[]{ 8,-6,-2,2,6,10,14,14,14,12,12,10,10,8,8,6,6,4,4,6,6};
	
	protected long minigunFiringInterval = 180;	// ms
	protected long shotgunFiringInterval = 600;	// ms
	protected long cannonFiringInterval = 800;	// ms
	protected boolean rocketReleased;
	protected long lastFire;
	private int minigunBulIndex;
	private int shotgunBulIndex;
	private int cannonBulIndex;
	protected int minigunAttck = 5;
	protected int shotgunAttck = 2;
	protected int cannonAttck = 50;
	protected int rocketAttck = 50;
	protected int minigun_currentLV = 1;
	protected int shotgun_currentLV;
	protected int cannon_currentLV;
	protected int rocket_currentLV;
	private MyMinigunBullet[] myBullets;
	private MyShotgunBullet[] myShotgunBullets;
	private MyCannonBullet[] myCannonBullets;
	public MyRocketBullet myRocketBullet;
	
	private EnemyMinigunBullet[] enemyBullets;
	private EnemyShotgunBullet[] enemyShotgunBullets;
	private EnemyCannonBullet[] enemyCannonBullets;
	public EnemyRocketBullet enemyRocketBullet;

	public TankEntity(Game ingame) {
		game = ingame;
		width = (int)(path_map.TILE_SIZE*0.7);
        height = (int)(path_map.TILE_SIZE*0.7);
        if(this instanceof EnemyMinigunTurret
        		|| this instanceof EnemyShotgunTurret
        		|| this instanceof EnemyCannonTurret){
        	width = (int)(path_map.TILE_SIZE*0.8);
            height = (int)(path_map.TILE_SIZE*0.8);
        }
		halfSize = width/2;
		unlockGun.put(TankEntity.GunType.MINIGUN, false);
		unlockGun.put(TankEntity.GunType.SHOTGUN, false);
		unlockGun.put(TankEntity.GunType.RICOCHET, false);
		unlockGun.put(TankEntity.GunType.CANNON, false);
		unlockGun.put(TankEntity.GunType.ROCKET, false);
		unlockGun.put(TankEntity.GunType.LASER, false);
		if(this instanceof MyTank) {
			unlockGun.put(TankEntity.GunType.MINIGUN, true);
			myBullets = new MyMinigunBullet[50];
			for (int i = 0; i < myBullets.length; i++) {
				myBullets[i] = new MyMinigunBullet(game,12,5);
			}
			myShotgunBullets = new MyShotgunBullet[70];
			for (int i = 0; i < myShotgunBullets.length; i++) {
				myShotgunBullets[i] = new MyShotgunBullet(game,5,5);
			}
			myCannonBullets = new MyCannonBullet[20];
			for (int i = 0; i < myCannonBullets.length; i++) {
				myCannonBullets[i] = new MyCannonBullet(game,7,50);
			}
			myRocketBullet = new MyRocketBullet(game, 2, 50);
		} else {
			enemyBullets = new EnemyMinigunBullet[50];
			for (int i = 0; i < enemyBullets.length; i++) {
				enemyBullets[i] = new EnemyMinigunBullet(game,12,5);
			}
			enemyShotgunBullets = new EnemyShotgunBullet[70];
			for (int i = 0; i < enemyShotgunBullets.length; i++) {
				enemyShotgunBullets[i] = new EnemyShotgunBullet(game,5,5);
			}
			enemyCannonBullets = new EnemyCannonBullet[20];
			for (int i = 0; i < enemyCannonBullets.length; i++) {
				enemyCannonBullets[i] = new EnemyCannonBullet(game,7,50);
			}
			enemyRocketBullet = new EnemyRocketBullet(game, 2, 50);
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
	
	public void setGun(GunType gunT) {
		if(unlockGun.get(gunT)){
			Game.soundManager.playEffect(Game.SOUND_CHANGE_GUN);
			gunType = gunT;
			changeGun = true;
			switch(gunType){
			case MINIGUN:
				gun = gun1;
				break;
			case SHOTGUN:
				gun = gun2;
				break;
			case RICOCHET:
				gun = gun3;
				break;
			case CANNON:
				gun = gun4;
				break;
			case ROCKET:
				gun = gun5;
				break;
			case LASER:
				gun = gun6;
				break;
			}
		}
	}
	
	public void Fire(float initBulletX,float initBulletY,float gunRotation) {
		Bullet bullet = null;
		switch(gunType){
		case MINIGUN:
			if (System.currentTimeMillis() - lastFire < minigunFiringInterval) {
				return;
			}
			lastFire = System.currentTimeMillis();
			if(this instanceof MyTank) {
				bullet = myBullets[minigunBulIndex ++ % myBullets.length];
				minigunBulIndex %= myBullets.length;
			} else {
				bullet = enemyBullets[minigunBulIndex ++ % enemyBullets.length];
				minigunBulIndex %= enemyBullets.length;
			}
			bullet.reinitialize(initBulletX,initBulletY ,(float)-Math.cos(0.0174532925*gunRotation)*bullet.moveSpeed, (float)-Math.sin(0.0174532925*gunRotation)*bullet.moveSpeed);
			bullet.attack = minigunAttck;
			Game.addEntity(bullet);
			Game.soundManager.playEffect(Game.SOUND_FIRE_MINIGUN);
			break;
		case SHOTGUN:
			if (System.currentTimeMillis() - lastFire < shotgunFiringInterval) {
				return;
			}
			lastFire = System.currentTimeMillis();
			for(int i = 0;i < 10;i++){
				if(this instanceof MyTank) {
					bullet = myShotgunBullets[shotgunBulIndex ++ % myShotgunBullets.length];
					shotgunBulIndex %= myShotgunBullets.length;
				} else {
					bullet = enemyShotgunBullets[shotgunBulIndex ++ % enemyShotgunBullets.length];
					shotgunBulIndex %= enemyShotgunBullets.length;
				}
				float ranDX = (float)-Math.cos(0.0174532925*(gunRotation + new Random().nextInt(30)-15))*bullet.moveSpeed*(new Random().nextInt(3)+6)*0.1f;
				float ranDY = (float)-Math.sin(0.0174532925*(gunRotation + new Random().nextInt(30)-15))*bullet.moveSpeed*(new Random().nextInt(3)+6)*0.1f;
				bullet.reinitialize(initBulletX,initBulletY ,ranDX, ranDY);
				bullet.attack = shotgunAttck;
				Game.addEntity(bullet);
			}
			Game.soundManager.playEffect(Game.SOUND_FIRE_SHOTGUN);
			break;
		case RICOCHET:
			break;
		case CANNON:
			if (System.currentTimeMillis() - lastFire < cannonFiringInterval) {
				return;
			}
			lastFire = System.currentTimeMillis();
			if(this instanceof MyTank) {
				bullet = myCannonBullets[cannonBulIndex ++ % myCannonBullets.length];
				cannonBulIndex %= myCannonBullets.length;
			} else {
				bullet = enemyCannonBullets[cannonBulIndex ++ % enemyCannonBullets.length];
				cannonBulIndex %= enemyCannonBullets.length;
			}
			bullet.reinitialize(initBulletX,initBulletY ,(float)-Math.cos(0.0174532925*gunRotation)*bullet.moveSpeed, (float)-Math.sin(0.0174532925*gunRotation)*bullet.moveSpeed);
			bullet.attack = cannonAttck;
			Game.addEntity(bullet);
			Game.soundManager.playEffect(Game.SOUND_FIRE_CANNON);
			break;
		case ROCKET:
			if(rocketReleased){
				rocketReleased = false;
				if(this instanceof MyTank) {
					Game.removeEntity(myRocketBullet);
					Game.addEntity(new BombEffect_BigBullet(game,myRocketBullet.x,myRocketBullet.y));
				} else {
					Game.removeEntity(enemyRocketBullet);
					Game.addEntity(new BombEffect_BigBullet(game,enemyRocketBullet.x,enemyRocketBullet.y));
				}
				Game.soundManager.playEffect(Game.SOUND_BOMB_TANK);
			} else {
				rocketReleased = true;
				if(this instanceof MyTank) {
					myRocketBullet.reinitialize(initBulletX,initBulletY ,0, 0);
					myRocketBullet.attack = rocketAttck;
					Game.addEntity(myRocketBullet);
				} else {
					enemyRocketBullet.reinitialize(initBulletX,initBulletY ,0, 0);
					enemyRocketBullet.attack = rocketAttck;
					Game.addEntity(enemyRocketBullet);
				}
				Game.soundManager.playEffect(Game.SOUND_FIRE_ROCKET);
			}
			break;
		case LASER:
			break;
		}
	}
	
	public void setMinigunAttck(int attck) {
		minigunAttck = attck;
	}
	
	public void setShotgunAttck(int attck) {
		shotgunAttck = attck;
	}
	
	public void setCannonAttck(int attck) {
		cannonAttck = attck;
	}
	
	public void setRocketAttck(int attck) {
		rocketAttck = attck;
	}
	
	public void setGunAngle(float gunAngle) {
		this.gunAngle = gunAngle;
	}
	
	public void setBodyAngle(int bodyAngle) {
		this.bodyAngle = bodyAngle % 360 - bodyAngle % deltaAng;
	}
	
	public void draw() {
		glPushMatrix();
		glTranslatef(x, y, 0);
        glRotatef(bodyAngle, 0f, 0f, 1f);
        glTranslatef(-x, -y, 0);
        
        super.draw(body);
        
        if(shoted){
			super.draw(Shot);
		}
        if(this instanceof MyTank){
        	if(MyTank.gotGold){
        		if(((MyTank)(this)).profitBarShowTime < 150){
        			((MyTank)(this)).profitBarShowTime++;
    			} else {
    				((MyTank)(this)).profitBarShowTime = 0;
    				MyTank.gotGold = false;
    			}
        	}
		}
    	
		glTranslatef(x, y, 0);
        glRotatef(gunAngle - bodyAngle, 0f, 0f, 1f);
        glTranslatef(-x, -y, 0);
        
        width += gunSize[gunSizeIndex];
        height += gunSize[gunSizeIndex];
        gun.bind();
        super.draw(gun);
    	glPopMatrix();
    	width -= gunSize[gunSizeIndex];
    	height -= gunSize[gunSizeIndex];
    	if(changeGun){
        	gunSizeIndex++;
        	gunSizeIndex %= gunSize.length;
        	if(gunSizeIndex == 0)
        		changeGun = false;
        }
    	
    	if(showHP){
			drawHP();
		}
	}
	
	public abstract void collidedWith(Entity other);
}