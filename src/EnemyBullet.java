
public class EnemyBullet extends Bullet {

	public EnemyBullet(Game ingame) {
		super("bullet.png");
		game = ingame;
	}

	@Override
	public void collidedWith(Entity other) {
		if (used || other instanceof EnemyTank || other instanceof Bullet) {
			return;
		}
		setDX(getMoveSpeed());
		setDY(getMoveSpeed());
		game.removeEntity(this);
		other.damage(attack);
		if(other.getHP() <= 0){
			game.removeEntity(other);
		}
		used = true;
	}
}
