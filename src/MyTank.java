
public class MyTank extends TankEntity {
	
	public static int myGold;
	public static int profit;
	public static boolean gotGold;
	protected int profitBarShowTime;

	public MyTank(Game ingame,int hp) {
		super(ingame);
		HP = hp;
		maxHP = HP;
		gun1 = Game.loadTexture("MyGun1.png");
		gun2 = Game.loadTexture("MyGun2.png");
		gun3 = Game.loadTexture("MyGun3.png");
		gun4 = Game.loadTexture("MyGun4.png");
		gun5 = Game.loadTexture("MyGun5.png");
		gun6 = Game.loadTexture("MyGun6.png");
		gun = gun1;
		body = Game.loadTexture("MyBody.png");
		Shot = Game.loadTexture("MyShot.png");
	}
	
	public void gotHPpotion(int hp) {
		HP += hp;
		if(HP > maxHP)
			HP = maxHP;
	}
	
	public void gotGold(int gl) {
		profit += gl;
		gotGold = true;
		System.out.println(" "+gl+" "+profit);
	}
	
	@Override
	public void collidedWith(Entity other) {
		if (other instanceof EnemyBullet) {
			shoted = true;
			Game.soundManager.playEffect(Game.SOUND_SHOT_TANK);
		} 
		if(!(other instanceof Bullet) && !(other instanceof Effect)){
			/*float tmp = xPreMove;
			xPreMove = x;
			x = tmp;
			if(!this.collidesWith(other)){
				System.out.println("\ny axis");
				xPreMove = x;
				yPreMove = y;
			} else {
				tmp = xPreMove;
				xPreMove = x;
				x = tmp;
				tmp = yPreMove;
				yPreMove = y;
				y = tmp;
				System.out.println("\nx gg");
				if(!this.collidesWith(other)){
					System.out.println("\nx axis");
					xPreMove = x;
					yPreMove = y;
				} else {
					System.out.println("\nx kk");
					tmp = yPreMove;
					yPreMove = y;
					y = tmp;
				}
			}*/
			moveBack();
		}
	}
}