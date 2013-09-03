
public class MyBullet extends Bullet {

	public MyBullet(Game ingame,String pic) {
		super(ingame,pic);
	}
	
	@Override
	public void collidedWith(Entity other) {
		if (used || other instanceof MyTank
				|| other instanceof Bullet 
				|| other instanceof Effect) {
			return;
		}
		setDX(0);
		setDY(0);
		used = true;
		game.removeEntity(this);
		if(this instanceof MyCannonBullet || this instanceof MyRocketBullet){
			game.addEntity(new BombEffect_BigBullet(game,x,y));
		} else {
			game.addEntity(new BulletShotEffect(game,x,y));
		}
			
		other.damage(attack);
		if(other.getHP() <= 0){
			game.removeEntity(other);
			if(other instanceof BombWall){
				game.addEntity(new BombEffect_BombWall(game,other.x,other.y));
				if(!((BombWall) other).died){
					((BombWall) other).died = true;
				}
			} else if (other instanceof OilTank){
				game.addEntity(new BombEffect_OilTank(game,other.x,other.y));
				if(!((OilTank) other).died){
					((OilTank) other).died = true;
				}
			} else {
				game.addEntity(new BombEffect_basic(game,other.x,other.y));
			}
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
