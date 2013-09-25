import org.newdawn.slick.opengl.Texture;

public class Box extends Entity {
	
	protected Texture box;

	public Box(Game ingame,int hp) {
		game = ingame;
		box = loadTexture("box.png");
		Shot = loadTexture("boxShot.png");
		width = (int)(game.map.TILE_SIZE*0.6);
        height = (int)(game.map.TILE_SIZE*0.6);
		halfSize = width/2;
		HP = hp;
		maxHP = HP;
	}
	
	public void draw() {
		super.draw(box);
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
			game.soundManager.playEffect(game.SOUND_SHOT_BOX);
		}
	}
}
