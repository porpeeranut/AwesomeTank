import org.newdawn.slick.opengl.Texture;

public class HPpotion extends Entity {
	
	protected Texture potion;

	public HPpotion(Game ingame,float x, float y) {
		game = ingame;
		this.x = x;
		this.y = y;
		potion = loadTexture("HPposion.png");
		width = (int)(game.map.TILE_SIZE*0.4);
        height = (int)(game.map.TILE_SIZE*0.4);
		halfSize = width/2;
	}
	
	public void draw() {
		super.draw(potion);
	}

	@Override
	public void collidedWith(Entity other) {
		if(other instanceof MyTank){
			((MyTank) other).gotHPpotion((int)(other.maxHP*0.2));
			game.soundManager.playEffect(game.SOUND_GOT_HP_POTION);
			game.removeEntity(this);
		}
	}
}
