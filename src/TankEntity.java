import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.newdawn.slick.opengl.Texture;

public abstract class TankEntity extends Entity{
	
	protected Texture gun;
	protected Texture body;
	// angle 0 is direct left
	protected float gunAngle;
	protected int bodyAngle = 0;
	protected int deltaAng = 3;

	public TankEntity() {
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
		if(bodyAngle == -deltaAng)
			bodyAngle = 360-deltaAng;
		
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
		int nxN = (int)(nx-halfSize)/map.TILE_SIZE;
		int nyN = (int)(ny-halfSize)/map.TILE_SIZE;
		int nxP = (int)(nx+halfSize)/map.TILE_SIZE;
		int nyP = (int)(ny+halfSize)/map.TILE_SIZE;
		
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
	
	public abstract void collidedWith(Entity other);
}