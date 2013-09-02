<<<<<<< HEAD
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

=======
>>>>>>> 39c289637e236b3f3c3dc1510b136af572347b8b
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

	public class Brick extends Entity {
	
	protected Texture brick;
<<<<<<< HEAD
=======
	protected Texture brickShot;
>>>>>>> 39c289637e236b3f3c3dc1510b136af572347b8b

	public Brick(Game ingame,int hp) {
		game = ingame;
		brick = loadTexture("brick.jpg");
<<<<<<< HEAD
		Shot = loadTexture("brickShot.jpg");
		width = game.map.TILE_SIZE;
        height = game.map.TILE_SIZE;
		halfSize = width/2;
		HP = hp;
		maxHP = HP;
=======
		brickShot = loadTexture("brickShot.jpg");
		width = game.map.TILE_SIZE;
        height = game.map.TILE_SIZE;
        halfSize = width/2;
		HP = hp;
>>>>>>> 39c289637e236b3f3c3dc1510b136af572347b8b
	}
	
	public void draw() {
		brick.bind();
		super.draw();
		if(shoted){
<<<<<<< HEAD
			Shot.bind();
			super.draw();
		}
		if(showHP){
			drawHP();
		}
=======
			brickShot.bind();
			super.draw();
		}
>>>>>>> 39c289637e236b3f3c3dc1510b136af572347b8b
	}

	@Override
	public void collidedWith(Entity other) {
		if(other instanceof Bullet){
			shoted = true;
		}
	}
<<<<<<< HEAD
=======

>>>>>>> 39c289637e236b3f3c3dc1510b136af572347b8b
}
