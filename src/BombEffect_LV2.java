import org.newdawn.slick.opengl.Texture;


public class BombEffect_LV2 extends Entity {
	
	private Texture[] bombEffect = new Texture[6];
	private long lastFrameChange;
	private long frameDuration = 50;
	private int frameNumber;

	public BombEffect_LV2(Game ingame,float x, float y) {
		game = ingame;
		this.x = x;
		this.y = y;
		for(int i = 1;i <= 6;i++){
			bombEffect[i-1] = loadTexture("BombEffect_1/"+i+".png");
		}
		width = (int)(ingame.map.TILE_SIZE*2);
        height = (int)(ingame.map.TILE_SIZE*2);
		halfSize = width/2;
	}
	
	public void move(long delta) {
		lastFrameChange += delta;
		if (lastFrameChange > frameDuration) {
			lastFrameChange = 0;
			frameNumber++;
			if (frameNumber >= bombEffect.length) {
				game.removeEntity(this);
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
