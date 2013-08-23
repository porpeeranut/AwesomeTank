import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Graphics2D;
import java.awt.Image;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

public class Entity {
	//private float x,y;
	/** center x,y */
	private int x,y;
	private int width,height;
	private Texture sprite;
	private Map map;
	/** angle */
	private float ang;
	private float size;

	public Entity(Texture sprite, Map map, float x, float y) {
		this.sprite = sprite;
		this.map = map;
		this.width = sprite.getImageWidth();
        this.height = sprite.getImageHeight();
        this.x = (int)x;
		this.y = (int)y;
		this.size = width/2;
	}
	
	public void move(float dx, float dy){
		float nx = x + dx;
		float ny = y + dy;

		//ang = (float) (Math.atan2(dy, dx) - (Math.PI / 2));
		if (validLocation(nx, ny)) {
			x = (int)nx;
			y = (int)ny;
			//return true;
		} else if(validLocation(x, ny)){
			y = (int)ny;
			//return true;
		} else if(validLocation(nx, y)){
			x = (int)nx;
			//return true;
		}
		//return false;
	}
	
	public boolean validLocation(float nx, float ny) {
		int nxN = (int)(nx-size)/map.TILE_SIZE;
		int nyN = (int)(ny-size)/map.TILE_SIZE;
		int nxP = (int)(nx+size)/map.TILE_SIZE;
		int nyP = (int)(ny+size)/map.TILE_SIZE;
		//System.out.print(nxN+" : "+nyN+" : "+nxP+" : "+nyP+"\n");
		
		if (map.blocked(nxN, nyN)) {
			System.out.print(nxN+" : "+nyN+"\n");
			return false;
		}
		if (map.blocked(nxP, nyN)) {
			System.out.print(nxP+" : "+nyN+"\n");
			return false;
		}
		if (map.blocked(nxN, nyP)) {
			System.out.print(nxN+" : "+nyP+"\n");
			return false;
		}
		if (map.blocked(nxP, nyP)) {
			System.out.print(nxP+" : "+nyP+"\n");
			return false;
		}

		return true;
	}
	
	public void setGunAngle(float ang) {
		this.ang = ang;
	}
	
	public void draw() {
		// work out the screen position of the entity based on the

		// x/y position and the size that tiles are being rendered at. So

		// if we'e'd render on screen 
		// at 15,15.
		
		/*int xp = (int) (Map.TILE_SIZE * x);
		int yp = (int) (Map.TILE_SIZE * y);*/
		
		/*glTranslatef(xp, xp, 0);
        glRotatef(ang, 0f, 0f, 1f);
        glTranslatef(-xp, -xp, 0);*/
		
		/*glTranslatef(get_centerX(), get_centerY(), 0);
        glRotatef(ang, 0f, 0f, 1f);
        glTranslatef(-get_centerX(), -get_centerY(), 0);*/
		
		glTranslatef(x, y, 0);
        glRotatef(ang, 0f, 0f, 1f);
        glTranslatef(-x, -y, 0);
        
        sprite.bind();
        glBegin(GL_QUADS);
    		glTexCoord2f(0,0);
    		glVertex2f(x-width/2 ,y-height/2);//upper left
    		glTexCoord2f(1,0);
    		glVertex2f(x+width/2 ,y-height/2);//upper right
    		glTexCoord2f(1,1);
    		glVertex2f(x+width/2 ,y+height/2);//bottom right
    		glTexCoord2f(0,1);
    		glVertex2f(x-width/2 ,y+height/2);//bottom left
    	glEnd();

		/*g.rotate(ang, xp, yp);
		g.drawImage(image, (int) (xp - 16), (int) (yp - 16), null);
		g.rotate(-ang, xp, yp);*/
	}
	
	public int get_centerX(){
        return x+width/2;
	}

	public int get_centerY(){
        return y+height/2;
	}
}