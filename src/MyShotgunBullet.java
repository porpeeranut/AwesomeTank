
public class MyShotgunBullet extends MyBullet {

	public MyShotgunBullet(Game ingame,int speed,int atck) {
		super(ingame,"shotgunBullet.png");
		moveSpeed = speed;
		attack = atck;
	}
}
