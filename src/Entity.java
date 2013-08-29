import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


public abstract class Entity {
	/** center x,y */
	protected float x,y;
	/** move speed */
	protected float	dx,dy;
	/** center x,y bofore move */
	protected float	xPreMove,yPreMove;
	protected int width,height;
	protected float halfSize;
	protected int HP;
	protected Game game;
	private Rectangle	me	= new Rectangle();
	private Rectangle	him	= new Rectangle();
	protected boolean shoted;
	protected int shotFade = 90;
	
	public Entity() {
	}

	public void move(long delta) {
		x += (delta * dx) / 10;
		y += (delta * dy) / 10;
	}
	
	public void preMove(long delta) {
	}
	
	public boolean validLocation(float nx, float ny) {
		int nxN = (int)(nx-halfSize)/game.map.TILE_SIZE;
		int nyN = (int)(ny-halfSize)/game.map.TILE_SIZE;
		int nxP = (int)(nx+halfSize)/game.map.TILE_SIZE;
		int nyP = (int)(ny+halfSize)/game.map.TILE_SIZE;
		
		if (game.map.blocked(nxN, nyN)) {
			return false;
		}
		if (game.map.blocked(nxP, nyN)) {
			return false;
		}
		if (game.map.blocked(nxN, nyP)) {
			return false;
		}
		if (game.map.blocked(nxP, nyP)) {
			return false;
		}
		return true;
	}
	
	public void damage(int damage) {
		this.HP -= damage;
	}
	public void setHP(int HP) {
		this.HP = HP;
	}

	public void setDX(float dx) {
		this.dx = dx;
	}
	
	public void setDY(float dy) {
		this.dy = dy;
	}
	
	public int getHP() {
		return this.HP;
	}
	
	public float getDX() {
		return dx;
	}

	public float getDY() {
		return dy;
	}

	public void draw() {
		glPushMatrix();
	    glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2f(x-halfSize ,y-halfSize);//upper left
			glTexCoord2f(1,0);
			glVertex2f(x+halfSize ,y-halfSize);//upper right
			glTexCoord2f(1,1);
			glVertex2f(x+halfSize ,y+halfSize);//bottom right
			glTexCoord2f(0,1);
			glVertex2f(x-halfSize ,y+halfSize);//bottom left
		glEnd();
		glPopMatrix();
		if(shoted){
			if(shotFade > 0){
				shotFade -= 30;
			} else {
				shotFade = 90;
				shoted = false;
			}
		}
	}

	public void doLogic() {
	}

	public float get_centerX(){
        return x+width/2;
	}
	public float get_centerY(){
        return y+height/2;
	}

	/** (1,1) to (mapWIDTH, mapHEIGHT)*/
	public void setPositionToMap(int x,int y){
        this.x = x*game.map.TILE_SIZE + game.map.TILE_SIZE/2;
        this.y = y*game.map.TILE_SIZE + game.map.TILE_SIZE/2;
	}
	
	protected Texture loadTexture(String key){
    	try {
            return TextureLoader.getTexture("PNG", new FileInputStream(new File("res/"+key)));
    	} catch (FileNotFoundException e) {
            e.printStackTrace();
    	} catch (IOException e) {
            e.printStackTrace();
    	}
    	return null;
    }

	public boolean collidesWith(Entity other) {
		me.setBounds((int) x - width/2, (int) y - height/2, width, height);
		him.setBounds((int) other.x - other.width/2, (int) other.y - other.height/2, other.width, other.height);
		return me.intersects(him);
	}

	public abstract void collidedWith(Entity other);
}
