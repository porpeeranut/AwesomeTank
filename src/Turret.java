import org.newdawn.slick.opengl.Texture;


public class Turret extends Entity {
	
	protected Texture turret;
	protected boolean died = false;

	public Turret(Game ingame,int hp) {
		game = ingame;
		turret = loadTexture("turret.png");
		Shot = loadTexture("shotTurret.png");
		width = turret.getImageWidth();
        height = turret.getImageHeight();
		halfSize = width/2;
		HP = hp;
		maxHP = HP;
	}
	
	public void draw() {
		turret.bind();
		super.draw();
		if(shoted){
			Shot.bind();
			super.draw();
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
