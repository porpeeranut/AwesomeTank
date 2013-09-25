import org.newdawn.slick.opengl.Texture;


public class MyTank extends TankEntity {

	public MyTank(Game ingame,int hp) {
		super(ingame);
		HP = hp;
		maxHP = HP;
		gun1 = loadTexture("MyGun1.png");
		gun2 = loadTexture("MyGun2.png");
		gun3 = loadTexture("MyGun3.png");
		gun4 = loadTexture("MyGun4.png");
		gun5 = loadTexture("MyGun5.png");
		gun6 = loadTexture("MyGun6.png");
		gun = gun1;
		body = loadTexture("MyBody.png");
		Shot = loadTexture("MyShot.png");
	}
	
	public void gotHPpotion(int hp) {
		HP += hp;
		if(HP > maxHP)
			HP = maxHP;
	}
	
	@Override
	public void collidedWith(Entity other) {
		if (other instanceof EnemyBullet) {
			shoted = true;
			game.soundManager.playEffect(game.SOUND_SHOT_TANK);
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