
<<<<<<< HEAD
public class MyShotgunBullet extends MyBullet {
=======
public class MyShotgunBullet extends Bullet {
>>>>>>> 39c289637e236b3f3c3dc1510b136af572347b8b

	public MyShotgunBullet(Game ingame,int speed,int atck) {
		super("shotgunBullet.png");
		game = ingame;
		moveSpeed = speed;
		attack = atck;
	}
<<<<<<< HEAD
=======

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
>>>>>>> 39c289637e236b3f3c3dc1510b136af572347b8b
}
