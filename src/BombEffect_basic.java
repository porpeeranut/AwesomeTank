import static org.lwjgl.opengl.GL11.glColor4f;

import org.newdawn.slick.opengl.Texture;


public class BombEffect_basic extends Entity {
	
	private Texture[] bombEffect = new Texture[6];
	private long lastFrameChange;
	private long frameDuration = 50;
	private int frameNumber;

	public BombEffect_basic(Game ingame,float x, float y) {
		game = ingame;
		this.x = x;
		this.y = y;
		for(int i = 1;i <= 6;i++){
			bombEffect[i-1] = loadTexture("BombEffect_1/"+i+".png");
		}
		width = bombEffect[1].getImageWidth();
		height = bombEffect[1].getImageHeight();
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
		bombEffect[frameNumber].bind();
        super.draw();
	}

	@Override
	public void collidedWith(Entity other) {
	}

}
