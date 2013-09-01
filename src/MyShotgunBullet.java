
public class MyShotgunBullet extends MyBullet {

	public MyShotgunBullet(Game ingame,int speed,int atck) {
		super("shotgunBullet.png");
		game = ingame;
		moveSpeed = speed;
		attack = atck;
	}
}
