
public class MyRicochetBullet extends MyBullet {

	public MyRicochetBullet(Game ingame,int speed,int atck) {
		super("bullet.png");
		game = ingame;
		moveSpeed = speed;
		attack = atck;
	}
}
