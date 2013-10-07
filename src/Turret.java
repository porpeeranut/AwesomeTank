import org.newdawn.slick.opengl.Texture;


public class Turret extends Entity {
	
	protected Texture turret;

	public Turret(Game ingame,int hp) {
		game = ingame;
		turret = Game.loadTexture("turret.png");
		Shot = Game.loadTexture("turretShot.png");
		width = (int)(path_map.TILE_SIZE*0.8);
        height = (int)(path_map.TILE_SIZE*0.8);
		halfSize = width/2;
		HP = hp;
		maxHP = HP;
	}
	
	public void draw() {
		super.draw(turret);
		if(shoted){
			super.draw(Shot);
		}
		if(showHP){
			drawHP();
		}
	}

	@Override
	public void collidedWith(Entity other) {
		if(other instanceof Bullet){
			shoted = true;
			Game.soundManager.playEffect(Game.SOUND_SHOT_TANK);
		}
	}
}
