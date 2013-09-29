import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glColor3f;

import java.util.Random;

import org.newdawn.slick.opengl.Texture;


public class Effect_smoke extends Effect {
	
	Texture smoke;
	private long lastFrameChange;
	private long frameDuration = 50;
	private int fade = 90;
	private int angle;

	public Effect_smoke(Game ingame,float x, float y) {
		game = ingame;
		this.x = x;
		this.y = y;
		if(new Random().nextInt(2) == 1)
			smoke = Game.loadTexture("smoke1.png");
		else
			smoke = Game.loadTexture("smoke2.png");
		width = (int)(game.map.TILE_SIZE*0.38);
        height = (int)(game.map.TILE_SIZE*0.38);
		halfSize = width/2;
		angle = new Random().nextInt(360);
	}
	
	public void move(long delta) {
		lastFrameChange += delta;
		if (lastFrameChange > frameDuration) {
			lastFrameChange = 0;
			fade -= 15;
			if (fade < 0) {
				Game.removeEntity(this);
			}
		}
	}
	
	public void draw() {
		glPushMatrix();
		glTranslatef(x, y, 0);
        glRotatef(angle, 0f, 0f, 1f);
        glTranslatef(-x, -y, 0);
        glColor4f(1f, 1f, 1f, (float) Math.sin(Math.toRadians(fade)));
        super.draw(smoke);
        glColor3f(1f,1f,1f);
        angle++;
        glPopMatrix();
	}

	@Override
	public void collidedWith(Entity other) {
	}
}
