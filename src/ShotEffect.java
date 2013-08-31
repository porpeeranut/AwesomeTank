import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glColor3f;

import org.newdawn.slick.opengl.Texture;


public class ShotEffect extends Entity {
	
	protected Texture shotEffect;
	private int ang = 0;
	private int fade = 90;

	public ShotEffect(Game ingame,float x, float y) {
		game = ingame;
		this.x = x;
		this.y = y;
		shotEffect = loadTexture("shotEffect.png");
		width = shotEffect.getImageWidth()-9;
        height = shotEffect.getImageHeight()-9;
		halfSize = width/2;
		shoted = true;
	}
	
	public void draw() {
		glPushMatrix();
		glTranslatef(x, y, 0);
        glRotatef(ang, 0f, 0f, 1f);
        glTranslatef(-x, -y, 0);
        
        shotEffect.bind();
        glColor4f(1f, 1f, 1f, (float) Math.sin(Math.toRadians(fade)));
        super.draw();
        glColor3f(1f, 1f, 1f);
        glPopMatrix();
        
        ang += 10;
        if(fade > 0)
        	fade -= 10;
        if(fade == 0)
        	game.removeEntity(this);
	}

	@Override
	public void collidedWith(Entity other) {
	}

}
