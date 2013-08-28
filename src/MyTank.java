
public class MyTank extends TankEntity {

	public MyTank(Game ingame,int hp) {
		game = ingame;
		HP = hp;
		gun = loadTexture("gun.png");
		body = loadTexture("body.png");
	}
	
	@Override
	public void collidedWith(Entity other) {
		if (!(other instanceof Bullet)) {
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