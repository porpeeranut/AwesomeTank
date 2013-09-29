import org.newdawn.slick.opengl.Texture;


public class Brick_2 extends Entity {
	
	protected Texture brick;

	public Brick_2(Game ingame,int hp) {
		game = ingame;
		brick = Game.loadTexture("brick_2.jpg");
		Shot = Game.loadTexture("brick_2Shot.jpg");
		width = game.map.TILE_SIZE;
        height = game.map.TILE_SIZE;
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
