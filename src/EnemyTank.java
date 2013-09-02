<<<<<<< HEAD

public class EnemyTank extends TankEntity {
	
	protected boolean died = false;
=======
import org.newdawn.slick.util.pathfinding.Mover;
import pathFinding.*;

public class EnemyTank extends TankEntity {
>>>>>>> 39c289637e236b3f3c3dc1510b136af572347b8b

	public EnemyTank(Game ingame,int hp) {
		game = ingame;
		HP = hp;
<<<<<<< HEAD
		maxHP = HP;
		gun = loadTexture("EnemyGun.png");
		body = loadTexture("EnemyBody.png");
		Shot = loadTexture("EnemyShot.png");
=======
		gun = loadTexture("EnemyGun.png");
		body = loadTexture("EnemyBody.png");
		bodyShot = loadTexture("EnemyShot.png");
	}
	
	/*
	 * Move along each step by path 
	 */
	public void move(long delta ,Path path){
		float setAng=0;
		int speed = 1;
		if(path != null){
			float tmpX = (float) (-speed*Math.cos(Math.PI*setAng/180));
			float tmpY = (float) (-speed*Math.sin(Math.PI*setAng/180));
			super.setDX(tmpX);	
			super.setDY(tmpY);
			//move to
			super.move(delta, setAng);
		}
>>>>>>> 39c289637e236b3f3c3dc1510b136af572347b8b
	}

	@Override
	public void collidedWith(Entity other) {
		if(other instanceof Bullet){
			shoted = true;
		}
	}

}
