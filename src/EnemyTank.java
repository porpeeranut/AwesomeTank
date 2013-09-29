
public class EnemyTank extends TankEntity {

	public EnemyTank(Game ingame) {
		super(ingame);
	}

	@Override
	public void collidedWith(Entity other) {
		if(other instanceof Bullet){
			shoted = true;
			Game.soundManager.playEffect(Game.SOUND_SHOT_TANK);
		}
	}
}
