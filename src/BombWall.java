import org.newdawn.slick.opengl.Texture;


public class BombWall extends Entity {
	
	protected Texture bombWall;

	public BombWall(Game ingame,int hp) {
		game = ingame;
		bombWall = Game.loadTexture("bombWall.png");
		Shot = Game.loadTexture("bombWallShot.png");
		width = Map.TILE_SIZE;
        height = Map.TILE_SIZE;
		halfSize = width/2;
		HP = hp;
		maxHP = HP;
	}
	
	public void draw() {
		super.draw(bombWall);
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
			Game.soundManager.playEffect(Game.SOUND_SHOT_BRICK);
		}
	}
}
