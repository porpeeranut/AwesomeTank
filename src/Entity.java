import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
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
	protected Texture Shot;
	protected Texture HPbar;
	protected Texture HPred;
	protected Texture HPblk;
	protected int HP;
	protected int maxHP;
	protected boolean showHP;
	protected int HPshowTime = 0;
	protected boolean died = false;
	protected Game game;
	protected boolean shoted;
	protected int shotFade = 90;
	protected boolean touchedBombEffect;
	protected int BombTime = 0;

	public Entity() {
		HPbar = loadTexture("HPbar.png");
		HPred = loadTexture("HPred.png");
		HPblk = loadTexture("HPblk.png");
	}

	public void move(long delta) {
		x += (delta * dx) / 10;
		y += (delta * dy) / 10;
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
	
	public void reset() {
		dx = 0;
		dy = 0;
		showHP = false;
    	shoted = false;
    	touchedBombEffect = false;
    	died = false;
	}
	
	public void setHP(int HP) {
		this.HP = HP;
		this.maxHP = HP;
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
	
	public void basicDraw(Texture tex) {
		tex.bind();
		float TexCoordWidth = tex.getWidth();
		float TexCoordHeight = tex.getHeight();
		glPushMatrix();
	    glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2f(x-width/2 ,y-height/2);//upper left
			glTexCoord2f(TexCoordWidth,0);
			glVertex2f(x+width/2 ,y-height/2);//upper right
			glTexCoord2f(TexCoordWidth,TexCoordHeight);
			glVertex2f(x+width/2 ,y+height/2);//bottom right
			glTexCoord2f(0,TexCoordHeight);
			glVertex2f(x-width/2 ,y+height/2);//bottom left
		glEnd();
		glPopMatrix();
	}

	public void draw() {
	}
	
	public void draw(Texture tex) {
		basicDraw(tex);
		if(shoted){
			showHP = true;
			if(shotFade > 0){
				shotFade -= 30;
			} else {
				shotFade = 90;
				shoted = false;
			}
		}
		if(touchedBombEffect){
			showHP = true;
			if(BombTime < 200){
				BombTime++;
			} else {
				BombTime = 0;
				touchedBombEffect = false;
			}
		}
		if(showHP){
			if(HPshowTime < 100){
				HPshowTime++;
			} else {
				HPshowTime = 0;
				showHP = false;
			}
		}
	}
	
	public void drawHP() {
		y -= height/2;
		HPred.bind();
		glPushMatrix();
		glBegin(GL_QUADS);
			glVertex2f(x-(halfSize-1) ,y-(height/12.8f));
			glVertex2f((x-(halfSize-1))+(width-2)*HP/maxHP ,y-(height/12.8f));
			glVertex2f((x-(halfSize-1))+(width-2)*HP/maxHP ,y+(height/12.8f));
			glVertex2f(x-(halfSize-1) ,y+(height/12.8f));
		glEnd();
		glPopMatrix();
		
		HPbar.bind();
		glPushMatrix();
		glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2f(x-halfSize ,y-(height/12.8f));
			glTexCoord2f(HPbar.getWidth(),0);
			glVertex2f(x+halfSize ,y-(height/12.8f));
			glTexCoord2f(HPbar.getWidth(),HPbar.getHeight());
			glVertex2f(x+halfSize ,y+(height/12.8f));
			glTexCoord2f(0,HPbar.getHeight());
			glVertex2f(x-halfSize ,y+(height/12.8f));
		glEnd();
		glPopMatrix();
		
		HPblk.bind();
		glPushMatrix();
		glBegin(GL_QUADS);
			glVertex2f((x-(halfSize-1))+(width-2)*HP/maxHP ,y-(height/12.8f));
			glVertex2f(x+(halfSize-1) ,y-(height/12.8f));
			glVertex2f(x+(halfSize-1) ,y+(height/12.8f));
			glVertex2f((x-(halfSize-1))+(width-2)*HP/maxHP ,y+(height/12.8f));
		glEnd();
		glPopMatrix();
		y += height/2;
	}

	public void doLogic() {
	}

	public float get_centerX(){
        return x;
	}
	public float get_centerY(){
        return y;
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
		/*Polygon rect1 = new Polygon(new float[]{(x - width/2), (y - height/2),
												(x + width/2), (y - height/2),
												(x + width/2), (y + height/2),
												(x - width/2), (y + height/2)});
		Polygon rect2 = new Polygon(new float[]{other.x - other.width/2,other.y - other.height/2,
												other.x + other.width/2,other.y - other.height/2,
												other.x + other.width/2,other.y + other.height/2,
												other.x - other.width/2,other.y + other.height/2,});
		if(rect1.intersects(rect2)){
			if(this instanceof TankEntity){
				rect1 = (Polygon) rect1.transform(Transform.createRotateTransform(
		                (float) Math.toRadians(((TankEntity)this).bodyAngle), x,y));
				System.out.println(String.valueOf(((TankEntity)this).bodyAngle)+"\n");
			}
			if(other instanceof TankEntity){
				rect2 = (Polygon) rect2.transform(Transform.createRotateTransform(
		                (float) Math.toRadians(((TankEntity)other).bodyAngle), other.x,other.y));
			}
		}
		return rect1.intersects(rect2);*/
		
		Rectangle  me  = new Rectangle();
		Rectangle  him  = new Rectangle();
		me.setBounds((int) x - width/2, (int) y - height/2, width, height);
		him.setBounds((int) other.x - other.width/2, (int) other.y - other.height/2, other.width, other.height);
		return me.intersects(him);
	}

	public abstract void collidedWith(Entity other);
}
