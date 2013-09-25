
public class EnemyTank extends TankEntity {

	public EnemyTank(Game ingame,int hp) {
		super(ingame);
		HP = hp;
		maxHP = HP;
		gun = Game.loadTexture("EnemyGun.png");
		body = Game.loadTexture("EnemyBody.png");
		Shot = Game.loadTexture("EnemyShot.png");
	}

	@Override
	public void collidedWith(Entity other) {
		if(other instanceof Bullet){
			shoted = true;
			Game.soundManager.playEffect(Game.SOUND_SHOT_TANK);
		}
	}

}
