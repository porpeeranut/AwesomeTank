import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import pathFinding.*;

public class path_map implements TileBasedMap {
	private HashMap<String, Texture> textureTable = new HashMap<String, Texture>();
	private static final int GROUND = 0;
	private static final int BLOCKED = 1;
	private static final int NULL = 2;
	//private int[][] mapData;
	private int[][] mapData;
	private static int WIDTH;
	private static int HEIGHT;
	public static int TILE_SIZE =64;
	//for path	#new add
	private boolean[][] visited;
	private int[][] map4path;
	
	// flip bottom left to top right
	private int[][] convertArrayAndRandomTilePic(int[][] mapData){
		int WIDTH = mapData.length;//17
		int HEIGHT = mapData[0].length;//7
		int[][] tmp = new int[HEIGHT][WIDTH];
		for (int x=0;x<WIDTH;x++) {
			for (int y=0;y<HEIGHT;y++) {
				/** GROUND  1-14
				 *  BLOCKED 101-109
				 *  NULL    200
				 * */
				if(mapData[x][y] == GROUND)
					tmp[y][x] = new Random().nextInt(14) +1;	// random 1-14
				if(mapData[x][y] == BLOCKED)
					tmp[y][x] = 100+(new Random().nextInt(9) +1);	// random 1-9
				if(mapData[x][y] == NULL)
					tmp[y][x] = mapData[x][y]*100;
			}
		}
		return tmp;
	}
	
	private Texture loadTexture(String name) throws FileNotFoundException, IOException{
		return TextureLoader.getTexture("JPG", new FileInputStream(new File("res/"+name)));
	}
	
	public path_map() {
		createMap(1);
		//change this #new add
		WIDTH = mapData.length;//length = 7
		HEIGHT = mapData[0].length;//length = 17
		visited =new boolean[WIDTH][HEIGHT];
		//end change #new add
		try {
			textureTable.put(String.valueOf(100*(int)NULL),loadTexture("null.jpg"));
			for(int i = 1;i <= 14;i++)
				textureTable.put(String.valueOf(i),loadTexture("ground"+i+".jpg"));
			for(int i = 1;i <= 9;i++)
				textureTable.put(String.valueOf(i+100),loadTexture("block"+i+".jpg"));
	    } catch (FileNotFoundException e) { 
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void draw(Path path) {
		Texture tex;
		/*hide this #new this
		WIDTH = mapData.length;
		HEIGHT = mapData[0].length;
		*/
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
            	//draw path
            	if(path != null){
            		if (path.contains(x, y)) {
            			//System.out.println("path ??");
						glColor3f(0.1f,0.5f,1f);
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
		            	glColor3f(1f,1f,1f);
					}
				}
			}
		}
	}
	
	public void createMap(int LV){
		switch(LV){
		case 1:
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
			//System.out.println(mapData.length +" "+mapData[0].length);
			break;
		case 2:
			mapData = new int[][]{
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			        { 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1 },
			        { 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1 },
			        { 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1 },
			        { 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1 },
			        { 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1 },
			        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
			};
			mapData = convertArrayAndRandomTilePic(mapData);
			break;
		}
	}
	
	//block
		public boolean blocked(int x, int y) {
			return mapData[(int) x][(int) y] > 100 && mapData[(int) x][(int) y] < 200;
		}
	/*
	 * ###########################################################################################
	 * #####################################  NEW ADD ############################################
	 * ###########################################################################################
	 */
	
	/**
	 * Clear the array marking which tiles have been visted by the path 
	 * finder.
	 */
	public void clearVisited() {
		for (int x=0;x<getWidthInTiles();x++) {
			for (int y=0;y<getHeightInTiles();y++) {
				visited[x][y] = false;
			}
		}
	}

	@Override
	public int getWidthInTiles() {
		// TODO Auto-generated method stub
		return WIDTH;
	}

	@Override
	public int getHeightInTiles() {
		// TODO Auto-generated method stub
		return HEIGHT;
	}

	@Override
	public void pathFinderVisited(int x, int y) {
		// TODO Auto-generated method stub
		visited[x][y] = true;
	}

	@Override
	public boolean blocked(Mover mover, int x, int y) {
		// TODO Auto-generated method stub
		//return map4path[(int) x][(int) y] == 1;
		return map4path[(int) x][(int) y] > 100 && map4path[(int) x][(int) y] < 200;
		//return mapData[(int) x][(int) y] > 100 && mapData[(int) x][(int) y] < 200;
	}

	@Override
	public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
		// TODO Auto-generated method stub
		return 1;
	}
	
	public void setArrayMap(ArrayList<Entity> entities){
		//clear all visited history ^__^
		//clearVisited(); 
		//create new map from current state
		int[][] tmp_map4path = new int[][]{
		        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		        { 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		        { 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
		};

		for(Entity entity : entities){
			//check all tank
			if( entity instanceof Entity && !(entity instanceof MyTank) ){
				tmp_map4path[(int) (entity.get_centerY()/TILE_SIZE)][(int) (entity.get_centerX()/TILE_SIZE)] = 1;
			}
		}
		
		tmp_map4path = convertArrayAndRandomTilePic(tmp_map4path);
		//add other obstruction to array map
		//System.out.println( tmp_map4path.length);
	
		
		map4path = tmp_map4path;
		//System.out.println( "b4 "+map4path[0].length);
	}
		

}
