
public class EnemyMinigunTurret extends EnemyTank {

	public EnemyMinigunTurret(Game ingame,int hp) {
		super(ingame);
		HP = hp;
		maxHP = HP;		
		gun = Game.loadTexture("enemy/minigunTurret_gun.png");
		body = Game.loadTexture("enemy/minigunTurret_body.png");
		Shot = Game.loadTexture("enemy/minigunTurret_shot.png");
	}
}
