import org.newdawn.slick.opengl.Texture;

public class OilTank extends Entity {
	
	protected Texture oiltank;

	public OilTank(Game ingame,int hp) {
		game = ingame;
		oiltank = Game.loadTexture("oiltank.png");
		Shot = Game.loadTexture("oiltankShot.png");
		width = (int)(Map.TILE_SIZE*0.6);
        height = (int)(Map.TILE_SIZE*0.6);
		halfSize = width/2;
		HP = hp;
		maxHP = HP;
	}
	
	public void draw() {
		super.draw(oiltank);
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
