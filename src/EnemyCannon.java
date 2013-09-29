
public class EnemyCannon extends EnemyTank {

	public EnemyCannon(Game ingame,int hp) {
		super(ingame);
		HP = hp;
		maxHP = HP;
		gun = Game.loadTexture("enemy/cannon_gun.png");
		body = Game.loadTexture("enemy/cannon_body.png");
		Shot = Game.loadTexture("enemy/cannon_shot.png");
	}
}
