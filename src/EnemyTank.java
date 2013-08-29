
public class EnemyTank extends TankEntity {

	public EnemyTank(Game ingame,int hp) {
		game = ingame;
		HP = hp;
		gun = loadTexture("EnemyGun.png");
		body = loadTexture("EnemyBody.png");
		bodyShot = loadTexture("EnemyShot.png");
	}

	@Override
	public void collidedWith(Entity other) {
		if(other instanceof Bullet){
			shoted = true;
		}
	}

}
