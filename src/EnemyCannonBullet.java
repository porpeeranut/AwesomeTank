
public class EnemyCannonBullet extends EnemyBullet {

	public EnemyCannonBullet(Game ingame,int speed,int atck) {
		super(ingame,"cannonBullet.png");
		moveSpeed = speed;
		attack = atck;
	}
}
