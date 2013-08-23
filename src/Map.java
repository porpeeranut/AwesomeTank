import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Map {
	private static final int NULL = 0;
	private static final int CLEAR = 1;
	private static final int BLOCKED = 2;
	private static final int WIDTH = 30;
	private static final int HEIGHT = 30;
	public static final int TILE_SIZE = 64;
	private int[][] mapData = new int[WIDTH][HEIGHT];
	
	private Texture tileNULL;
	private Texture tileCLEAR;
	private Texture tileBLOCK;

	public Map() {
		for (int x=0;x<WIDTH;x++) {
			for (int y=0;y<HEIGHT;y++) {
				mapData[x][y] = CLEAR;
			}
		}
		for (int y=0;y<HEIGHT;y++) {
			mapData[0][y] = BLOCKED;
			mapData[2][y] = BLOCKED;
			mapData[7][y] = BLOCKED;
			mapData[11][y] = BLOCKED;
			mapData[WIDTH-1][y] = BLOCKED;
		}
		for (int x=0;x<WIDTH;x++) {
			if ((x > 0) && (x < WIDTH-1)) {
				mapData[x][10] = CLEAR;
			}
			
			if (x > 2) {
				mapData[x][9] = BLOCKED;
			}
			mapData[x][0] = BLOCKED;
			mapData[x][HEIGHT-1] = BLOCKED;
		}
		mapData[0][0] = NULL;
		mapData[4][9] = CLEAR;
		mapData[7][5] = CLEAR;
		mapData[7][4] = CLEAR;
		mapData[11][7] = CLEAR;
		
		try {
			tileNULL = TextureLoader.getTexture("JPG", new FileInputStream(new File("D://JavaGameWorkspace//testRPG//res//null.jpg")));
			tileCLEAR = TextureLoader.getTexture("JPG", new FileInputStream(new File("D://JavaGameWorkspace//testRPG//res//clear.jpg")));
			tileBLOCK = TextureLoader.getTexture("JPG", new FileInputStream(new File("D://JavaGameWorkspace//testRPG//res//block.jpg")));
	    } catch (FileNotFoundException e) { 
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void draw() {
		
		for (int x=0;x<WIDTH;x++) {
			for (int y=0;y<HEIGHT;y++) {
				if (mapData[x][y] == NULL) {
					tileNULL.bind();
				}
				if (mapData[x][y] == CLEAR) {
					tileCLEAR.bind();
				}
				if (mapData[x][y] == BLOCKED) {
					tileBLOCK.bind();
				}
				glPushMatrix();
				glTranslatef(x*TILE_SIZE, y*TILE_SIZE, 0);
				glBegin(GL_QUADS);
            		/*glTexCoord2f(0,0);
            		glVertex2f(x ,y );//upper left
            		glTexCoord2f(1,0);
            		glVertex2f(x*TILE_SIZE ,y);//upper right
            		glTexCoord2f(1,1);
            		glVertex2f(x*TILE_SIZE ,y*TILE_SIZE);//bottom right
            		glTexCoord2f(0,1);
            		glVertex2f(x ,y*TILE_SIZE);//bottom left*/
				
					glTexCoord2f(0, 0);
					glVertex2f(0, 0);
					glTexCoord2f(1, 0);
					glVertex2f(TILE_SIZE, 0);
					glTexCoord2f(1, 1);
					glVertex2f(TILE_SIZE,TILE_SIZE);
					glTexCoord2f(0, 1);
					glVertex2f(0, TILE_SIZE);
				glEnd();
            	glPopMatrix();
                
				// draw the rectangle with a dark outline
				/*g.fillRect(x*TILE_SIZE,y*TILE_SIZE,TILE_SIZE,TILE_SIZE);
				if(g.getColor() != Color.black)
					g.setColor(Color.darkGray.darker());
				g.drawRect(x*TILE_SIZE,y*TILE_SIZE,TILE_SIZE,TILE_SIZE);*/
			}
		}
	}
	
	/** True if the location is blocked */
	public boolean blocked(int x, int y) {
		return mapData[(int) x][(int) y] == BLOCKED;
	}
}