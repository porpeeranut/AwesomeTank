
public class MyShotgunBullet extends Bullet {

	public MyShotgunBullet(Game ingame,int speed) {
		super("shotgunBullet.png");
		game = ingame;
		moveSpeed = speed;		
	}

	@Override
	public void collidedWith(Entity other) {
		if (used || other instanceof MyTank || other instanceof Bullet) {
			return;
		}
		setDX(0);
		setDY(0);
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