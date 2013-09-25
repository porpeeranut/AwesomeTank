import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.newdawn.slick.opengl.Texture;


public class Button {
	
	protected Texture button;
	protected Texture button_onMouseOver;
	protected Texture button_clicked;
	protected Texture button_LVlock;
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
			button = Game.loadTexture("menu.png");
			button_onMouseOver = Game.loadTexture("menu_onMouseOver.png");
			break;
		case PLAY:
			button = Game.loadTexture("play.png");
			button_onMouseOver = Game.loadTexture("play_onMouseOver.png");
			break;
		case PAUSE:
			button = Game.loadTexture("pause.png");
			button_onMouseOver = Game.loadTexture("pause_onMouseOver.png");
			button_clicked = Game.loadTexture("pause_clicked.png");
			break;
		case HELP:
			button = Game.loadTexture("help.png");
			button_onMouseOver = Game.loadTexture("help_onMouseOver.png");
			button_clicked = Game.loadTexture("help_clicked.png");
			break;
		case BACKTOUPGRADE:
			button = Game.loadTexture("bckToUpgrd.png");
			button_onMouseOver = Game.loadTexture("bckToUpgrd_onMouseOver.png");
			break;
		case CONTINUE:
			button = Game.loadTexture("continue.png");
			button_onMouseOver = Game.loadTexture("continue_onMouseOver.png");
			break;
		case BCK_TO_MENU_YES:
			button = Game.loadTexture("yes.png");
			button_onMouseOver = Game.loadTexture("yes_onMouseOver.png");
			break;
		case BCK_TO_MENU_NO:
			button = Game.loadTexture("no.png");
			button_onMouseOver = Game.loadTexture("no_onMouseOver.png");
			break;
		default:
			break;
		}
		for(int i = 1;i <= Game.maxLevel;i++){
			if(thisButton.toString().equals("LV"+String.valueOf(i))){
				button = Game.loadTexture("LVbutton/LV"+i+".png");
    			button_onMouseOver = Game.loadTexture("LVbutton/LV"+i+"_onMouseOver.png");
    			button_LVlock = Game.loadTexture("LVbutton/LV"+i+"_lock.png");
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
	
	public void draw_LVlock() {
		button_LVlock.bind();
		TexCoordWidth = button_LVlock.getWidth();
		TexCoordHeight = button_LVlock.getHeight();
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
}
