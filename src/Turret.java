import org.newdawn.slick.opengl.Texture;


public class Turret extends Entity {
	
	protected Texture turret;
	protected boolean died = false;

	public Turret(Game ingame,int hp) {
		game = ingame;
		turret = loadTexture("turret.png");
		Shot = loadTexture("turretShot.png");
		width = (int)(game.map.TILE_SIZE*0.8);
        height = (int)(game.map.TILE_SIZE*0.8);
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
		}
	}
}
