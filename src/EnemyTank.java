
public class EnemyTank extends TankEntity {

	public EnemyTank(Game ingame,int hp) {
		game = ingame;
		HP = hp;
		gun = loadTexture("gun.png");
		body = loadTexture("body.png");
	}

	@Override
	public void collidedWith(Entity other) {
	}

}
