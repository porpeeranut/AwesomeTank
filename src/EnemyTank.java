
public class EnemyTank extends TankEntity {

	public EnemyTank(Game ingame,int hp) {
		super(ingame);
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
			game.soundManager.playEffect(game.SOUND_SHOT_TANK);
		}
	}

}
