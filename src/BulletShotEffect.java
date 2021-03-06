import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glColor3f;

import org.newdawn.slick.opengl.Texture;

public class BulletShotEffect extends Effect {
	
	protected Texture shotEffect;
	private int ang = 0;
	private int fade = 90;

	public BulletShotEffect(Game ingame,float x, float y) {
		game = ingame;
		this.x = x;
		this.y = y;
		shotEffect = Game.loadTexture("shotEffect.png");
		width = (int)(path_map.TILE_SIZE*0.3);
        height = (int)(path_map.TILE_SIZE*0.3);
		halfSize = width/2;
	}
	
	public void draw() {
		glPushMatrix();
		glTranslatef(x, y, 0);
        glRotatef(ang, 0f, 0f, 1f);
        glTranslatef(-x, -y, 0);

        glColor4f(1f, 1f, 1f, (float) Math.sin(Math.toRadians(fade)));
        super.draw(shotEffect);
        if(Game.state == Game.State.PAUSE || Game.state == Game.State.BACKTOMENU || Game.state == Game.State.HELP || 
        		Game.state == Game.State.LVCOMPLETE || Game.state == Game.State.LVFAILED)
			glColor3f(0.5f, 0.5f, 0.5f);
        else
        	glColor3f(1f, 1f, 1f);
        glPopMatrix();
        
        ang += 10;
        if(fade > 0)
        	fade -= 10;
        if(fade == 0)
        	Game.removeEntity(this);
	}

	@Override
	public void collidedWith(Entity other) {
	}

}
