
public class EnemyShotgun extends EnemyTank {

	public EnemyShotgun(Game ingame,int hp) {
		super(ingame);
		HP = hp;
		maxHP = HP;
		gun = Game.loadTexture("enemy/shotgun_gun.png");
		body = Game.loadTexture("enemy/shotgun_body.png");
		Shot = Game.loadTexture("enemy/shotgun_shot.png");
	}
}
