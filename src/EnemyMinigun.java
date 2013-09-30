
public class EnemyMinigun extends EnemyTank {

	public EnemyMinigun(Game ingame,int hp) {
		super(ingame);
		HP = hp;
		maxHP = HP;
		/*gun = Game.loadTexture("enemy/minigun_gun.png");
		body = Game.loadTexture("enemy/minigun_body.png");
		Shot = Game.loadTexture("enemy/minigun_shot.png");*/
		
		gun = Game.loadTexture("enemy/rocket_gun.png");
		body = Game.loadTexture("enemy/rocket_body.png");
		Shot = Game.loadTexture("enemy/rocket_shot.png");
	}
}
