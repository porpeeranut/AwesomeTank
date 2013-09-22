import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


public class Button {
	
	protected Texture button;
	protected Texture button_onMouseOver;
	protected Texture button_clicked;
	protected Game.CurrentButton thisButton;
	private float TexCoordWidth;
	private float TexCoordHeight;
	protected float x,y;
	protected int width,height;
	protected Game game;

	public Button(Game ingame,Game.CurrentButton btn,float x,float y) {
		thisButton = btn;
		switch(thisButton){
    		case MENU:
    			button = loadTexture("menu.png");
    			button_onMouseOver = loadTexture("menu_onMouseOver.png");
    			break;
    		case PLAY:
    			button = loadTexture("play.png");
    			button_onMouseOver = loadTexture("play_onMouseOver.png");
    			break;
    		case PAUSE:
    			button = loadTexture("pause.png");
    			button_onMouseOver = loadTexture("pause_onMouseOver.png");
    			button_clicked = loadTexture("pause_clicked.png");
    			break;
    		case HELP:
    			button = loadTexture("help.png");
    			button_onMouseOver = loadTexture("help_onMouseOver.png");
    			button_clicked = loadTexture("help_clicked.png");
    			break;
    		case BACKTOUPGRADE:
    			button = loadTexture("bckToUpgrd.png");
    			button_onMouseOver = loadTexture("bckToUpgrd_onMouseOver.png");
    			break;
    		case CONTINUE:
    			button = loadTexture("continue.png");
    			button_onMouseOver = loadTexture("continue_onMouseOver.png");
    			break;
    		case BCK_TO_MENU_YES:
    			button = loadTexture("yes.png");
    			button_onMouseOver = loadTexture("yes_onMouseOver.png");
    			break;
    		case BCK_TO_MENU_NO:
    			button = loadTexture("no.png");
    			button_onMouseOver = loadTexture("no_onMouseOver.png");
    			break;
		}
		for(int i = 1;i <= game.maxLevel;i++){
			if(thisButton.toString().equals("LV"+String.valueOf(i))){
				System.out.println("asdaf");
				button = loadTexture("LV"+i+".png");
    			button_onMouseOver = loadTexture("LV"+i+"_onMouseOver.png");
			}
		}
		//System.out.println(thisButton);
		width = button.getImageWidth();
		height = button.getImageHeight();
		game = ingame;
		this.x = x;
		this.y = y;
	}

	public boolean On_Mouse_Over(int mousex,int mousey){
		if(mousex > x-width/2 && mousex < x+width/2 && mousey > y-height/2 && mousey < y+height/2)
			return true;
		else
			return false;
	}
	
	public void draw() {
		button.bind();
		TexCoordWidth = button.getWidth();
		TexCoordHeight = button.getHeight();
		basicDraw();
	}
	
	public void draw_OnMouseOver() {
		button_onMouseOver.bind();
		TexCoordWidth = button_onMouseOver.getWidth();
		TexCoordHeight = button_onMouseOver.getHeight();
		basicDraw();
	}
	
	public void draw_Clicked() {
		button_clicked.bind();
		TexCoordWidth = button_onMouseOver.getWidth();
		TexCoordHeight = button_onMouseOver.getHeight();
		basicDraw();
	}
	
	private void basicDraw() {
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
}
