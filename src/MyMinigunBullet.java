
public class MyMinigunBullet extends MyBullet {

	public MyMinigunBullet(Game ingame,int speed,int atck) {
		super("bullet.png");
		game = ingame;
		moveSpeed = speed;
		attack = atck;
	}
}
