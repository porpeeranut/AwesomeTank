import org.newdawn.slick.opengl.Texture;


public class BombEffect_basic extends Effect {
	
	private Texture[] bombEffect = new Texture[16];
	private long lastFrameChange;
	private long frameDuration = 25;
	private int frameNumber;

	public BombEffect_basic(Game ingame,float x, float y) {
		game = ingame;
		this.x = x;
		this.y = y;
		for(int i = 1;i <= bombEffect.length;i++){
			bombEffect[i-1] = Game.loadTexture("BombEffect_1/"+i+".png");
		}
		width = (int)(path_map.TILE_SIZE*2);
        height = (int)(path_map.TILE_SIZE*2);
		halfSize = width/2;
	}
	
	public void move(long delta) {
		lastFrameChange += delta;
		if (lastFrameChange > frameDuration) {
			lastFrameChange = 0;
			frameNumber++;
			if (frameNumber >= bombEffect.length) {
				Game.removeEntity(this);
			}
		}
	}
	
	public void draw() {
        super.draw(bombEffect[frameNumber]);
	}

	@Override
	public void collidedWith(Entity other) {
	}
}
