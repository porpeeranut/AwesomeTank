
public class EnemyRocket extends EnemyTank {

	public EnemyRocket(Game ingame,int hp) {
		super(ingame);
		HP = hp;
		maxHP = HP;
		gun = Game.loadTexture("enemy/rocket_gun.png");
		body = Game.loadTexture("enemy/rocket_body.png");
		Shot = Game.loadTexture("enemy/rocket_shot.png");
	}
}
