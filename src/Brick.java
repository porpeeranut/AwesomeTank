import org.newdawn.slick.opengl.Texture;

public class Brick extends Entity {
	
	protected Texture brick;

	public Brick(Game ingame,int hp) {
		game = ingame;
		brick = Game.loadTexture("brick.jpg");
		Shot = Game.loadTexture("brickShot.jpg");
		width = path_map.TILE_SIZE;
        height = path_map.TILE_SIZE;
		halfSize = width/2;
		HP = hp;
		maxHP = HP;
	}
	
	public void draw() {
		super.draw(brick);
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
