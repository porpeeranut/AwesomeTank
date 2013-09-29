
public class EnemyShotgunTurret extends EnemyTank {

	public EnemyShotgunTurret(Game ingame,int hp) {
		super(ingame);
		HP = hp;
		maxHP = HP;
		gun = Game.loadTexture("enemy/shotgunTurret_gun.png");
		body = Game.loadTexture("enemy/shotgunTurret_body.png");
		Shot = Game.loadTexture("enemy/shotgunTurret_shot.png");
	}
}
