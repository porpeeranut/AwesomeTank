
public class EnemyMinigunBullet extends EnemyBullet {

	public EnemyMinigunBullet(Game ingame,int speed,int atck) {
		super(ingame,"bullet.png");
		moveSpeed = speed;
		attack = atck;
	}
}
