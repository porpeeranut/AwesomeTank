import org.newdawn.slick.opengl.Texture;

public class HPpotion extends Entity {
	
	protected Texture potion;

	public HPpotion(Game ingame,float x, float y) {
		game = ingame;
		this.x = x;
		this.y = y;
		potion = Game.loadTexture("HPposion.png");
		width = (int)(Map.TILE_SIZE*0.4);
        height = (int)(Map.TILE_SIZE*0.4);
		halfSize = width/2;
	}
	
	public void draw() {
		super.draw(potion);
	}

	@Override
	public void collidedWith(Entity other) {
		if(other instanceof MyTank){
			((MyTank) other).gotHPpotion((int)(other.maxHP*0.2));
			Game.soundManager.playEffect(Game.SOUND_GOT_HP_POTION);
			Game.removeEntity(this);
		}
	}
}
