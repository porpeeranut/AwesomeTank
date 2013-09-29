
public class EnemyCannonTurret extends EnemyTank {

	public EnemyCannonTurret(Game ingame,int hp) {
		super(ingame);
		HP = hp;
		maxHP = HP;
		gun = Game.loadTexture("enemy/cannonTurret_gun.png");
		body = Game.loadTexture("enemy/cannonTurret_body.png");
		Shot = Game.loadTexture("enemy/cannonTurret_shot.png");
	}
}
