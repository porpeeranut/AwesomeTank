
public class MyBullet extends Bullet {

	public MyBullet(String pic) {
		super(pic);
	}
	
	@Override
	public void collidedWith(Entity other) {
		if (used || other instanceof MyTank
				|| other instanceof Bullet
				|| other instanceof ShotEffect
				|| other instanceof BombEffect_basic) {
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
			game.addEntity(new BombEffect_basic(game,other.x,other.y));
			if(other instanceof EnemyTank){
				if(!((EnemyTank) other).died){
					((EnemyTank) other).died = true;
					game.numEnemy--;
				}
			}
			if(other instanceof Turret){
				if(!((Turret) other).died){
					((Turret) other).died = true;
					game.numEnemy--;
				}
			}
		}
	}

}
