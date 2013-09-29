import org.newdawn.slick.opengl.Texture;

public class Box extends Entity {
	
	protected Texture box;

	public Box(Game ingame,int hp) {
		game = ingame;
		box = Game.loadTexture("box.png");
		Shot = Game.loadTexture("boxShot.png");
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
			Game.soundManager.playEffect(Game.SOUND_SHOT_BOX);
		}
	}
}
