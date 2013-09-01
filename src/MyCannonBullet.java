
public class MyCannonBullet extends MyBullet {

	public MyCannonBullet(Game ingame,int speed,int atck) {
		super("cannonBullet.png");
		game = ingame;
		moveSpeed = speed;
		attack = atck;
	}
}
