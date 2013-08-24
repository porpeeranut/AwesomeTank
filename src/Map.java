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
import java.util.HashMap;
import java.util.Random;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Map {
	private HashMap<String, Texture> textureTable = new HashMap<String, Texture>();
	private static final int GROUND = 0;
	private static final int BLOCKED = 1;
	private static final int NULL = 2;
	private int[][] mapData;
	private static int WIDTH;
	private static int HEIGHT;
	public static int TILE_SIZE = 50;

	// flip bottom left to top right
	private int[][] convertArrayAndRandomTilePic(int[][] mapData){
		int WIDTH = mapData.length;
		int HEIGHT = mapData[0].length;
		int[][] tmp = new int[HEIGHT][WIDTH];
		for (int x=0;x<WIDTH;x++) {
			for (int y=0;y<HEIGHT;y++) {
				if(mapData[x][y] == GROUND)
					tmp[y][x] = mapData[x][y]*10+(new Random().nextInt(5) +1);	// random 1-5
				if(mapData[x][y] == BLOCKED)
					tmp[y][x] = mapData[x][y]*10+(new Random().nextInt(9) +1);	// random 1-9
				if(mapData[x][y] == NULL)
					tmp[y][x] = mapData[x][y]*10;
			}
		}
		return tmp;
	}
	
	private Texture loadTexture(String name) throws FileNotFoundException, IOException{
		return TextureLoader.getTexture("JPG", new FileInputStream(new File("D://JavaGameWorkspace//testRPG//res//"+name+".jpg")));
	}
	
	public Map() {
		mapData = new int[][]{
		        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		        { 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		        { 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
		};
		mapData = convertArrayAndRandomTilePic(mapData);
		try {
			textureTable.put(String.valueOf(NULL),loadTexture("null"));
			for(int i = 1;i <= 5;i++)
				textureTable.put(String.valueOf(i),loadTexture("ground"+i));
			for(int i = 1;i <= 9;i++)
				textureTable.put(String.valueOf(BLOCKED)+String.valueOf(i),loadTexture("block"+i));
	    } catch (FileNotFoundException e) { 
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void draw() {
		Texture tex;
		WIDTH = mapData.length;
		HEIGHT = mapData[0].length;
		for (int x=0;x<WIDTH;x++) {
			for (int y=0;y<HEIGHT;y++) {
				tex = textureTable.get(String.valueOf(mapData[x][y]));
				tex.bind();
				glPushMatrix();
				glTranslatef(x*TILE_SIZE, y*TILE_SIZE, 0);
				glBegin(GL_QUADS);				
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
			}
		}
	}
	
	/** True if the location is blocked */
	public boolean blocked(int x, int y) {
		return mapData[(int) x][(int) y] > 10;
	}
}