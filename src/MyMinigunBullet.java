
public class MyMinigunBullet extends Bullet {

	public MyMinigunBullet(Game ingame,int speed,int atck) {
		super("bullet.png");
		game = ingame;
		moveSpeed = speed;
		attack = atck;
	}

	@Override
	public void collidedWith(Entity other) {
		if (used || other instanceof MyTank || other instanceof Bullet || other instanceof ShotEffect) {
			return;
		}
		setDX(0);
		setDY(0);
		used = true;
		game.removeEntity(this);
		game.addEntity(new ShotEffect(game,x,y));
		other.damage(attack);
		if(other.getHP() <= 0){
			game.removeEntity(other);
			if(other instanceof EnemyTank)
				game.numEnemy--;
		}
	}
}
