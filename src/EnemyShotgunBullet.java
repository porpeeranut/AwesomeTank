
public class EnemyShotgunBullet extends EnemyBullet {

	public EnemyShotgunBullet(Game ingame,int speed,int atck) {
		super(ingame,"shotgunBullet.png");
		moveSpeed = speed;
		attack = atck;
	}
}
