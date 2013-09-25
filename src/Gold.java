import java.util.Random;

import org.newdawn.slick.opengl.Texture;

public class Gold extends Entity {
	
	protected Texture gold;

	public Gold(Game ingame) {
		game = ingame;
		gold = loadTexture("gold.png");
		float ratio = (float) (new Random().nextFloat() * (0.25 - 0.15) + 0.15);	// random 0.15-0.25
		width = (int)(game.map.TILE_SIZE*ratio);
		height = (int)(game.map.TILE_SIZE*ratio);
		halfSize = width/2;
	}
	
	public void draw() {
		super.draw(gold);
		dx *= 0.8;
		dy *= 0.8;
		if(Math.abs(dx) < 0.01)
			setDX(0);
		if(Math.abs(dy) < 0.01)
			setDY(0);
	}

	@Override
	public void collidedWith(Entity other) {
		if(other instanceof MyTank){
			((MyTank) other).gotGold(game.currentLevel * (new Random().nextInt(20) +10));	// random 10-30
			game.soundManager.playEffect(game.SOUND_GOT_GOLD);
			game.removeEntity(this);
		}
	}
}
