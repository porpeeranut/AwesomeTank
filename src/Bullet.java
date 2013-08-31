import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;


public abstract class Bullet extends Entity {
	
	public int moveSpeed;
	public int attack = 5;
	protected Texture bull;
	protected boolean used;
	protected boolean die = false;

	public Bullet(String pic) {
		this.dx = moveSpeed;
		this.dy = moveSpeed;
		bull = loadTexture(pic);
		width = bull.getImageWidth()-5;
        height = bull.getImageHeight()-5;
		halfSize = width/2;
	}
	
	public int getMoveSpeed() {
		return moveSpeed;
	}
	
	public void move(long delta) {
		super.move(delta);
	}
	
	public void reinitialize(float x, float y,float dx,float dy) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		used = false;
	}
	
	public void die(long delta) {
		die = true;
	}
	
	public void draw() {
		bull.bind();
		super.draw();
	}

	public abstract void collidedWith(Entity other);
}
