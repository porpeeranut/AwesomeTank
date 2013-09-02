
public class MyMinigunBullet extends MyBullet {

	public MyMinigunBullet(Game ingame,int speed,int atck) {
		super(ingame,"bullet.png");
		moveSpeed = speed;
		attack = atck;
	}
}
