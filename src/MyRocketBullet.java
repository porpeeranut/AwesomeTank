
public class MyRocketBullet extends MyBullet {

	public MyRocketBullet(Game ingame,int speed,int atck) {
		super(ingame,"cannonBullet.png");
		moveSpeed = speed;
		attack = atck;
	}
}
