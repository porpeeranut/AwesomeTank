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
	protected Texture upgrdLV0;
	protected Texture upgrdLV1;
	protected Texture upgrdLV2;
	protected Texture upgrdLV3;
	protected Texture upgrdLV4;
	protected boolean maxUpgrd;
	protected int priceToUpgrd;
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
		case PLAY_INTRO:
			button = Game.loadTexture("play_big.png");
			button_onMouseOver = Game.loadTexture("play_big_onMouseOver.png");
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
		case UPGRD_MINIGUN:
			upgrdLV1 = Game.loadTexture("upgrd/upgrdMinigunLV1.png");
			upgrdLV2 = Game.loadTexture("upgrd/upgrdMinigunLV2.png");
			upgrdLV3 = Game.loadTexture("upgrd/upgrdMinigunLV3.png");
			upgrdLV4 = Game.loadTexture("upgrd/upgrdMinigunLV4.png");
			switch(Game.player.minigun_currentLV){
			case 1:
				button = upgrdLV1;
				priceToUpgrd = 350;
				break;
			case 2:
				button = upgrdLV2;
				priceToUpgrd = 470;
				break;
			case 3:
				button = upgrdLV3;
				priceToUpgrd = 1100;
				break;
			case 4:
				button = upgrdLV4;
				maxUpgrd = true;
				break;
			}
			break;
		case UPGRD_SHOTGUN:
			upgrdLV0 = Game.loadTexture("upgrd/upgrdMinigunLV0.png");
			upgrdLV1 = Game.loadTexture("upgrd/upgrdMinigunLV1.png");
			upgrdLV2 = Game.loadTexture("upgrd/upgrdMinigunLV2.png");
			upgrdLV3 = Game.loadTexture("upgrd/upgrdMinigunLV3.png");
			upgrdLV4 = Game.loadTexture("upgrd/upgrdMinigunLV4.png");
			switch(Game.player.shotgun_currentLV){
			case 0:
				button = upgrdLV0;
				priceToUpgrd = 2750;
				break;
			case 1:
				button = upgrdLV1;
				priceToUpgrd = 1400;
				break;
			case 2:
				button = upgrdLV2;
				priceToUpgrd = 2000;
				break;
			case 3:
				button = upgrdLV3;
				priceToUpgrd = 2950;
				break;
			case 4:
				button = upgrdLV4;
				maxUpgrd = true;
				break;
			}
			break;
		case UPGRD_CANNON:
			upgrdLV0 = Game.loadTexture("upgrd/upgrdMinigunLV0.png");
			upgrdLV1 = Game.loadTexture("upgrd/upgrdMinigunLV1.png");
			upgrdLV2 = Game.loadTexture("upgrd/upgrdMinigunLV2.png");
			upgrdLV3 = Game.loadTexture("upgrd/upgrdMinigunLV3.png");
			upgrdLV4 = Game.loadTexture("upgrd/upgrdMinigunLV4.png");
			switch(Game.player.cannon_currentLV){
			case 0:
				button = upgrdLV0;
				priceToUpgrd = 16000;
				break;
			case 1:
				button = upgrdLV1;
				priceToUpgrd = 8000;
				break;
			case 2:
				button = upgrdLV2;
				priceToUpgrd = 12000;
				break;
			case 3:
				button = upgrdLV3;
				priceToUpgrd = 18000;
				break;
			case 4:
				button = upgrdLV4;
				maxUpgrd = true;
				break;
			}
			break;
		case UPGRD_ROCKET:
			upgrdLV0 = Game.loadTexture("upgrd/upgrdMinigunLV0.png");
			upgrdLV1 = Game.loadTexture("upgrd/upgrdMinigunLV1.png");
			upgrdLV2 = Game.loadTexture("upgrd/upgrdMinigunLV2.png");
			upgrdLV3 = Game.loadTexture("upgrd/upgrdMinigunLV3.png");
			upgrdLV4 = Game.loadTexture("upgrd/upgrdMinigunLV4.png");
			switch(Game.player.rocket_currentLV){
			case 0:
				button = upgrdLV0;
				priceToUpgrd = 16000;
				break;
			case 1:
				button = upgrdLV1;
				priceToUpgrd = 8000;
				break;
			case 2:
				button = upgrdLV2;
				priceToUpgrd = 12000;
				break;
			case 3:
				button = upgrdLV3;
				priceToUpgrd = 18000;
				break;
			case 4:
				button = upgrdLV4;
				maxUpgrd = true;
				break;
			}
			break;
		case UPGRD_ARMOR:
			upgrdLV1 = Game.loadTexture("upgrd/upgrdArmorLV1.png");
			upgrdLV2 = Game.loadTexture("upgrd/upgrdArmorLV2.png");
			upgrdLV3 = Game.loadTexture("upgrd/upgrdArmorLV3.png");
			upgrdLV4 = Game.loadTexture("upgrd/upgrdArmorLV4.png");
			System.out.println("HP "+Game.player.maxHP);
			switch(Game.player.maxHP){
			case 50:
				button = upgrdLV1;
				priceToUpgrd = 3000;
				break;
			case 80:
				button = upgrdLV2;
				priceToUpgrd = 16000;
				break;
			case 110:
				button = upgrdLV3;
				priceToUpgrd = 28000;
				break;
			case 140:
				button = upgrdLV4;
				maxUpgrd = true;
				break;
			}
			break;
		case UPGRD_SPEED:
			upgrdLV1 = Game.loadTexture("upgrd/upgrdSpeedLV1.png");
			upgrdLV2 = Game.loadTexture("upgrd/upgrdSpeedLV2.png");
			upgrdLV3 = Game.loadTexture("upgrd/upgrdSpeedLV3.png");
			upgrdLV4 = Game.loadTexture("upgrd/upgrdSpeedLV4.png");
			if(Game.player.speed == 2f){
				button = upgrdLV1;
				priceToUpgrd = 500;
			} else if(Game.player.speed == 2.3f){
				button = upgrdLV2;
				priceToUpgrd = 1000;
			} else if(Game.player.speed == 2.6f){
				button = upgrdLV3;
				priceToUpgrd = 1500;
			} else if(Game.player.speed == 2.9f){
				button = upgrdLV4;
				maxUpgrd = true;
			}
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
		switch(thisButton){
		case PLAY_INTRO:
			width = button.getImageWidth() + 15;
			height = button.getImageHeight() + 15;
			break;
		default:
			width = button.getImageWidth();
			height = button.getImageHeight();
			break;
		}
		game = ingame;
		this.x = x;
		this.y = y;
	}
	
	public void upgrade() {
		switch(thisButton){
		case UPGRD_MINIGUN:
			switch(Game.player.minigun_currentLV){
			case 1:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV2;
				priceToUpgrd = 470;
				Game.player.minigunFiringInterval = 150;
				Game.player.minigun_currentLV++;
				break;
			case 2:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV3;
				priceToUpgrd = 1100;
				Game.player.minigunFiringInterval = 120;
				Game.player.minigun_currentLV++;
				break;
			case 3:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV4;
				Game.player.minigunFiringInterval = 90;
				Game.player.minigun_currentLV++;
				maxUpgrd = true;
				break;
			}
			break;
		case UPGRD_SHOTGUN:
			switch(Game.player.shotgun_currentLV){
			case 0:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV1;
				priceToUpgrd = 1400;
				Game.player.unlockGun.put(TankEntity.GunType.SHOTGUN, true);
				Game.player.shotgunFiringInterval = 575;
				Game.player.shotgunAttck = 2;
				Game.player.shotgun_currentLV++;
				break;
			case 1:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV2;
				priceToUpgrd = 2000;
				Game.player.shotgunFiringInterval = 550;
				Game.player.shotgunAttck = 5;
				Game.player.shotgun_currentLV++;
				break;
			case 2:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV3;
				priceToUpgrd = 2950;
				Game.player.shotgunFiringInterval = 525;
				Game.player.shotgunAttck = 8;
				Game.player.shotgun_currentLV++;
				break;
			case 3:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV4;
				Game.player.shotgunFiringInterval = 500;
				Game.player.shotgunAttck = 11;
				Game.player.shotgun_currentLV++;
				maxUpgrd = true;
				break;
			}
			break;
		case UPGRD_CANNON:
			switch(Game.player.cannon_currentLV){
			case 0:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV1;
				priceToUpgrd = 8000;
				Game.player.unlockGun.put(TankEntity.GunType.CANNON, true);
				Game.player.cannonFiringInterval = 800;
				Game.player.cannonAttck = 50;
				Game.player.cannon_currentLV++;
				break;
			case 1:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV2;
				priceToUpgrd = 12000;
				Game.player.cannonFiringInterval = 770;
				Game.player.cannonAttck = 70;
				Game.player.cannon_currentLV++;
				break;
			case 2:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV3;
				priceToUpgrd = 18000;
				Game.player.cannonFiringInterval = 740;
				Game.player.cannonAttck = 90;
				Game.player.cannon_currentLV++;
				break;
			case 3:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV4;
				Game.player.cannonFiringInterval = 700;
				Game.player.cannonAttck = 110;
				Game.player.cannon_currentLV++;
				maxUpgrd = true;
				break;
			}
			break;
		case UPGRD_ROCKET:
			switch(Game.player.rocket_currentLV){
			case 0:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV1;
				priceToUpgrd = 8000;
				Game.player.unlockGun.put(TankEntity.GunType.ROCKET, true);
				Game.player.rocketAttck = 50;
				Game.player.myRocketBullet.moveSpeed = 1.6f;
				Game.player.rocket_currentLV++;
				break;
			case 1:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV2;
				priceToUpgrd = 12000;
				Game.player.rocketAttck = 70;
				Game.player.myRocketBullet.moveSpeed = 1.9f;
				Game.player.rocket_currentLV++;
				break;
			case 2:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV3;
				priceToUpgrd = 18000;
				Game.player.rocketAttck = 90;
				Game.player.myRocketBullet.moveSpeed = 2.2f;
				Game.player.rocket_currentLV++;
				break;
			case 3:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV4;
				Game.player.rocketAttck = 110;
				Game.player.myRocketBullet.moveSpeed = 2.5f;
				Game.player.rocket_currentLV++;
				maxUpgrd = true;
				break;
			}
			break;
		case UPGRD_ARMOR:
			switch(Game.player.maxHP){
			case 50:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV2;
				priceToUpgrd = 16000;
				Game.player.maxHP = 80;
				break;
			case 80:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV3;
				priceToUpgrd = 28000;
				Game.player.maxHP = 110;
				break;
			case 110:
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV4;
				Game.player.maxHP = 140;
				maxUpgrd = true;
				break;
			}
			break;
		case UPGRD_SPEED:
			if(Game.player.speed == 2f) {
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV2;
				priceToUpgrd = 1000;
				Game.player.speed = 2.3f;
			} else if(Game.player.speed == 2.3f) {
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV3;
				priceToUpgrd = 1500;
				Game.player.speed = 2.6f;
			} else if(Game.player.speed == 2.6f) {
				MyTank.myGold -= priceToUpgrd;
				button = upgrdLV4;
				Game.player.speed = 2.9f;
				maxUpgrd = true;
			}
			break;
		default:
			break;
		}
	}

	public boolean On_Mouse_Over(int mousex,int mousey){
		switch(thisButton){
		case UPGRD_MINIGUN:
		case UPGRD_SHOTGUN:
		case UPGRD_CANNON:
		case UPGRD_ROCKET:
			if(mousex > x-width/2 && mousex < x+width/2 && mousey > y-height/2 && mousey < y+height/2-20)
				return true;
			else
				return false;
		default:
			if(mousex > x-width/2 && mousex < x+width/2 && mousey > y-height/2 && mousey < y+height/2)
				return true;
			else
				return false;
		}
	}
	
	public void draw() {
		button.bind();
		TexCoordWidth = button.getWidth();
		TexCoordHeight = button.getHeight();
		basicDraw();
	}
	
	public void draw_OnMouseOver() {
		switch(thisButton){
		case UPGRD_MINIGUN:
		case UPGRD_SHOTGUN:
		case UPGRD_CANNON:
		case UPGRD_ROCKET:
		case UPGRD_ARMOR:
		case UPGRD_SPEED:
			draw();
			break;
		default:
			button_onMouseOver.bind();
			TexCoordWidth = button_onMouseOver.getWidth();
			TexCoordHeight = button_onMouseOver.getHeight();
			basicDraw();
			break;
		}
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
