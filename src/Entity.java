import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


public abstract class Entity {
	/** center x,y */
	protected float x,y;
	protected float	dx,dy;
	protected int width,height;
	protected float halfSize;
	protected int HP;
	protected Map map;
	private Rectangle	me	= new Rectangle();
	private Rectangle	him	= new Rectangle();
	
	public Entity() {
	}

	public void move(long delta) {
		// update the location of the entity based on move speeds
		x += (delta * dx) / 1000;
		y += (delta * dy) / 1000;
	}

	public void setDX(float dx) {
		this.dx = dx;
	}
	
	public void setDY(float dy) {
		this.dy = dy;
	}
	
	public float getDX() {
		return dx;
	}

	public float getDY() {
		return dy;
	}

	public void draw() {
	}

	public void doLogic() {
	}

	public float get_centerX(){
        return x+width/2;
	}
	public float get_centerY(){
        return y+height/2;
	}

	public void setPositionToMap(int x,int y){
        this.x = x*map.TILE_SIZE + map.TILE_SIZE/2;
        this.y = y*map.TILE_SIZE + map.TILE_SIZE/2;
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
		him.setBounds((int) other.x - width/2, (int) other.y - height/2, other.width, other.height);

		return me.intersects(him);
	}

	public abstract void collidedWith(Entity other);
}
