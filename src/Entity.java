import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Entity {
	/** center x,y */
	private int x,y;
	private int width,height;
	private Texture gun;
	private Texture body;
	private Map map;
	// angle 0 is direct left
	private float gunAngle;
	private int bodyAngle = 0;
	private int deltaAng = 3;
	private float size;

	public Entity(Map map, float x, float y) {
		this.gun = loadTexture("gun.png");
		this.body = loadTexture("body.png");
		this.map = map;
		this.width = 40;
        this.height = 40;
        this.x = (int)x;
		this.y = (int)y;
		this.size = width/2;
	}
	
	public void move(float dx, float dy,float setAng){
		float nx = x + dx;
		float ny = y + dy;
		if(bodyAngle > setAng){
			if(bodyAngle-setAng <= 180)
				bodyAngle -= deltaAng;
			else if(bodyAngle-setAng > 180)
				bodyAngle += deltaAng;
		}
		if(bodyAngle < setAng){
			if(setAng - bodyAngle <= 180)
				bodyAngle += deltaAng;
			else if(setAng - bodyAngle > 180)
				bodyAngle -= deltaAng;
		}
		
		if(bodyAngle == 360)
			bodyAngle = 0;
		if(bodyAngle == -3)
			bodyAngle = 357;
		
		if (validLocation(nx, ny)) {
			x = (int)nx;
			y = (int)ny;
		} else if(validLocation(x, ny)){
			y = (int)ny;
		} else if(validLocation(nx, y)){
			x = (int)nx;
		}
	}
	
	public boolean validLocation(float nx, float ny) {
		int nxN = (int)(nx-size)/map.TILE_SIZE;
		int nyN = (int)(ny-size)/map.TILE_SIZE;
		int nxP = (int)(nx+size)/map.TILE_SIZE;
		int nyP = (int)(ny+size)/map.TILE_SIZE;
		
		if (map.blocked(nxN, nyN)) {
			return false;
		}
		if (map.blocked(nxP, nyN)) {
			return false;
		}
		if (map.blocked(nxN, nyP)) {
			return false;
		}
		if (map.blocked(nxP, nyP)) {
			return false;
		}
		return true;
	}
	
	public void setGunAngle(float gunAngle) {
		this.gunAngle = gunAngle;
	}
	
	public void setBodyAngle(int bodyAngle) {
		this.bodyAngle = bodyAngle;
	}
	
	public void draw() {
		glTranslatef(x, y, 0);
        glRotatef(bodyAngle, 0f, 0f, 1f);
        glTranslatef(-x, -y, 0);
        
        body.bind();
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
    	
		glTranslatef(x, y, 0);
        glRotatef(gunAngle - bodyAngle, 0f, 0f, 1f);
        glTranslatef(-x, -y, 0);
        
        width += 8;
        height += 8;
        gun.bind();
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
    	width -= 8;
        height -= 8;
	}
	
	public void setPositionToMap(int x,int y){
        this.x = x*map.TILE_SIZE + map.TILE_SIZE/2;
        this.y = y*map.TILE_SIZE + map.TILE_SIZE/2;
	}
	
	public int get_centerX(){
        return x+width/2;
	}
	public int get_centerY(){
        return y+height/2;
	}
	
	private Texture loadTexture(String key){
    	try {
            return TextureLoader.getTexture("PNG", new FileInputStream(new File("res/"+key)));
    	} catch (FileNotFoundException e) {
            e.printStackTrace();
    	} catch (IOException e) {
            e.printStackTrace();
    	}
    	return null;
    }
}