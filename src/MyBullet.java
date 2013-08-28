
public class MyBullet extends Bullet {

	public MyBullet(Game ingame) {
		game = ingame;
	}

	@Override
	public void collidedWith(Entity other) {
		if (used || other instanceof MyTank) {
			return;
		}
		reinitialize(game.initBulletX,game.initBulletY ,getMoveSpeed(), getMoveSpeed());
		game.removeEntity(this);
		other.damage(attack);
		if(other.getHP() <= 0){
			game.removeEntity(other);
			if(other instanceof EnemyTank)
				game.numEnemy--;
		}
		used = true;
	}
}
