
public class EnemyTank extends TankEntity {
	
	protected boolean died = false;

	public EnemyTank(Game ingame,int hp) {
		game = ingame;
		HP = hp;
		maxHP = HP;
		gun = loadTexture("EnemyGun.png");
		body = loadTexture("EnemyBody.png");
		Shot = loadTexture("EnemyShot.png");
	}

	@Override
	public void collidedWith(Entity other) {
		if(other instanceof Bullet){
			shoted = true;
		}
	}

}
