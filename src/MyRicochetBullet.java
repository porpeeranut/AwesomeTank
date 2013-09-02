
public class MyRicochetBullet extends MyBullet {

	public MyRicochetBullet(Game ingame,int speed,int atck) {
		super(ingame,"bullet.png");
		moveSpeed = speed;
		attack = atck;
	}
}
