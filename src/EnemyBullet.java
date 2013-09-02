
public class EnemyBullet extends Bullet {

	public EnemyBullet(Game ingame) {
		super("bullet.png");
		game = ingame;
	}

	@Override
	public void collidedWith(Entity other) {
<<<<<<< HEAD
		if (used || other instanceof EnemyTank || other instanceof Bullet || other instanceof ShotEffect) {
			return;
		}
		setDX(0);
		setDY(0);
		used = true;
=======
		if (used || other instanceof EnemyTank || other instanceof Bullet) {
			return;
		}
		setDX(getMoveSpeed());
		setDY(getMoveSpeed());
>>>>>>> 39c289637e236b3f3c3dc1510b136af572347b8b
		game.removeEntity(this);
		other.damage(attack);
		if(other.getHP() <= 0){
			game.removeEntity(other);
		}
<<<<<<< HEAD
=======
		used = true;
>>>>>>> 39c289637e236b3f3c3dc1510b136af572347b8b
	}
}
