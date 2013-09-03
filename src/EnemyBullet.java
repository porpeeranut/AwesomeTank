
public class EnemyBullet extends Bullet {

	public EnemyBullet(Game ingame) {
		super(ingame,"bullet.png");
	}

	@Override
	public void collidedWith(Entity other) {
		if (used || other instanceof EnemyTank 
				|| other instanceof Bullet 
				|| other instanceof Effect) {
			return;
		}
		setDX(0);
		setDY(0);
		used = true;
		game.removeEntity(this);
		other.damage(attack);
		if(other.getHP() <= 0){
			game.removeEntity(other);
		}
	}
}
