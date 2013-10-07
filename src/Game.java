import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import pathFinding.AStarPathFinder;
import pathFinding.Path;
 
public class Game{
	public static enum State {
		INTRO,UPGRADE,SELECT_LEVEL,PLAY,BACKTOMENU,PAUSE,HELP,LVCOMPLETE,LVFAILED;
	}
	public static State state = State.INTRO;
	private int fade;
	private boolean changingState;
	private State nextState;
	private int nextLV;
	public static enum CurrentButton {
		NONE,MENU,PLAY,PLAY_INTRO,PAUSE,HELP,BACKTOUPGRADE,CONTINUE,BCK_TO_MENU_YES,BCK_TO_MENU_NO,
		LV1,LV2,LV3,LV4,LV5,LV6,LV7,LV8,LV9,LV10,
		UPGRD_MINIGUN,UPGRD_SHOTGUN,UPGRD_CANNON,UPGRD_ROCKET,
		UPGRD_ARMOR,UPGRD_SPEED;
	}
	public HashMap<Integer, Boolean> unlockLV = new HashMap<Integer, Boolean>();
	public static int currentLevel;
	public static int maxLevel = 10;
	private Button[] btn;
	private CurrentButton currentButton = CurrentButton.NONE;
	private Texture fadeTex;
	private Texture backgndIntro;
	private Texture backgndUpgrade;
	private Texture moneyBackgnd;
	private Texture backgndSelectLV;
	private Texture selectGunBar;
	private Texture pauseLabel;
	private Texture helpLabel;
	private Texture backToMenuLabel;
	private Texture lvCompleteLabel;
	private Texture lvFailedLabel;
	private Texture profitLabel;
	private Texture profitMiniLabel;
	private Texture HPpipe;
	private Texture HPred;
	private Texture HPblue;
	private Texture labelMinigun;
	private Texture labelShotgun;
	private Texture labelCannon;
	private Texture labelRocket;
	private Texture labelMinigun_unuse;
	private Texture labelShotgun_unuse;
	private Texture labelCannon_unuse;
	private Texture labelRocket_unuse;
	private Texture labelMinigun_lock;
	private Texture labelShotgun_lock;
	private Texture labelCannon_lock;
	private Texture labelRocket_lock;
	private Texture labelTextPrice;
	private Texture labelTextArmor;
	private Texture labelTextSpeed;
	private Texture labelTextMinigun;
	private Texture labelTextShotgun;
	private Texture labelTextCannon;
	private Texture labelTextRocket;
	private Texture number;
	private Texture number_brown;
	private Texture number_no_gold;
	private boolean not_enough_gold;
	private int Label_X;
	private int Label_Y;
	private int miniLabel_Y;
	private int timeShowLabel;
	private static ArrayList<Entity> entities = new ArrayList<Entity>();
	private static ArrayList<Entity> removeList = new ArrayList<Entity>();
    //public Map map;
    public static MyTank player;
    static private int camera_x,camera_y,camera_w,camera_h;
    static private int camera_x_tmp,camera_y_tmp;
    static final int WORLD_W,WORLD_H;
    private float gunRotation = 0;
    float bodyAng = 0;
    private static long timerTicksPerSecond = Sys.getTimerResolution();
    private long delta;
    private long lastLoopTime = getTime();
    private long lastFpsTime;
    private int fps;
    public static SoundManager soundManager;
    public static int SOUND_HIT;
    public static int SOUND_FIRE_MINIGUN;
    public static int SOUND_FIRE_SHOTGUN;
    public static int SOUND_FIRE_CANNON;
    public static int SOUND_FIRE_ROCKET;
    public static int SOUND_CHANGE_GUN;
    public static int SOUND_SHOT_BRICK;
    public static int SOUND_SHOT_BOX;
    public static int SOUND_SHOT_TANK;
    public static int SOUND_BOMB_TANK;
    public static int SOUND_BOMB_BRICK;
    public static int SOUND_BOMB_BOX;
    public static int SOUND_BOMB_OILTANK;
    public static int SOUND_CLICK;
    public static int SOUND_RELEASE;
    public static int SOUND_PLAY;
    public static int SOUND_GOT_HP_POTION;
    public static int SOUND_GOT_GOLD;
    public static int SOUND_UPGRADE;
    public static int SOUND_NO_MONEY;
    
    public static int SOUND_BGM_INTRO;
    public static int SOUND_BGM_PLAY;
    public static int CURRENT_SOUND;
    
    public float initBulletX,initBulletY;
    
	private EnemyMinigun[] enemyMinigun;
	private EnemyShotgun[] enemyShotgun;
	private EnemyCannon[] enemyCannon;
	//private EnemyRocket[] enemyRocket;
	private EnemyMinigunTurret[] enemyMinigunTurret;
	private EnemyShotgunTurret[] enemyShotgunTurret;
	private EnemyCannonTurret[] enemyCannonTurret;
	public static int numEnemy;
	
	private Brick[] brick;
	private Brick_2[] brick_2;
	private Box[] box;
	private BombWall[] bmWall;
	private OilTank[] oilTank;
	private Turret[] turret;
	public static Gold[] gold;
	public static int goldIndex;
	
	//#
	public path_map map;
	private Path path;
	private AStarPathFinder finder;
	public int path_time;
    
    static{
    	//initial world size
        WORLD_W = 1920;
        WORLD_H = 1200;
    }
   
    public Game() throws IOException, LWJGLException{
    	path_time = 0;
    	//initialed value of camera
    	camera_x = 0;
        camera_y = 0;
        camera_w = 640;
        camera_h = 650;
        
        Display.setIcon(new ByteBuffer[] {
		        loadIcon("game.png")  // "res/game.png" size 32x32
		    });
        //Mouse.setGrabbed(true);

        //initial display
        Display.setDisplayMode(new DisplayMode(camera_w, camera_h));
        Display.create();
        
        entities.clear();
        soundManager = new SoundManager();
		soundManager.initialize(20);
		SOUND_FIRE_MINIGUN	= soundManager.addSound("fire_minigun.wav");
		SOUND_FIRE_SHOTGUN	= soundManager.addSound("fire_shotGun.wav");
		SOUND_FIRE_CANNON   = soundManager.addSound("fire_cannon.wav");
		SOUND_FIRE_ROCKET   = soundManager.addSound("fire_rocket.wav");
		SOUND_CHANGE_GUN	= soundManager.addSound("changeGun.wav");
		SOUND_SHOT_BRICK	= soundManager.addSound("shot_brick.wav");
		SOUND_SHOT_BOX	= soundManager.addSound("shot_box.wav");
		SOUND_SHOT_TANK	= soundManager.addSound("shot_tank.wav");
		SOUND_BOMB_TANK	= soundManager.addSound("bomb_tank.wav");
		SOUND_BOMB_BRICK	= soundManager.addSound("bomb_brick.wav");
		SOUND_BOMB_BOX	= soundManager.addSound("bomb_box.wav");
		SOUND_BOMB_OILTANK	= soundManager.addSound("bomb_oil.wav");
		SOUND_CLICK		= soundManager.addSound("mouse_click.wav");
		SOUND_RELEASE	= soundManager.addSound("mouse_release.wav");
		SOUND_PLAY		= soundManager.addSound("play.wav");
		SOUND_GOT_HP_POTION	= soundManager.addSound("gotHP.wav");
		SOUND_GOT_GOLD	= soundManager.addSound("gotGold.wav");
		SOUND_UPGRADE	= soundManager.addSound("upgrade.wav");
	    SOUND_NO_MONEY	= soundManager.addSound("noMoney.wav");
		
		SOUND_BGM_INTRO		= soundManager.addSound("BGM_intro.wav");
		SOUND_BGM_PLAY		= soundManager.addSound("BGM_play.wav");

		map = new path_map();
		finder = new AStarPathFinder(map,10,false);
		
		for(int i = 1;i <= maxLevel;i++){
			if(i == 1)
				unlockLV.put(i, true);
			else
				unlockLV.put(i, false);
		}
		loadResource();
		
        //initialization opengl code
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0 ,640 ,650 ,0 ,-1 , 1);
        //glOrtho(camera_x ,640+camera_x ,650+camera_y ,camera_y ,-1 , 1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        while(!Display.isCloseRequested()){
        	//clear screen
            glClear(GL_COLOR_BUFFER_BIT);
            
            delta = getTime() - lastLoopTime;
    		lastLoopTime = getTime();
    		lastFpsTime += delta;
    		fps++;

    		// update our FPS
    		if (lastFpsTime >= 1000) {
    			Display.setTitle("Awesome Tank (FPS: " + fps + ")");
    			lastFpsTime = 0;
    			fps = 0;
    		}
    		
    		switch(state){
    		case INTRO:
    		case UPGRADE:
    		case SELECT_LEVEL:
    			if(!soundManager.isPlayingSound() || CURRENT_SOUND != SOUND_BGM_INTRO){
    				soundManager.stopPlayingSound();
    				soundManager.playSound(SOUND_BGM_INTRO);
    				CURRENT_SOUND = SOUND_BGM_INTRO;
    			}
    			break;
			default:
				break;
    		}
    		
    		switch(state){
        	case INTRO:
        		draw(backgndIntro,camera_w/2,camera_h/2,camera_w,camera_h);
        		button_In_INTRO_state();
        		if(changingState)
        			fading();
        		else
        			recover();
    			break;
        	case UPGRADE:
        		draw(moneyBackgnd,camera_w/2,camera_h/2,camera_w,camera_h);
        		drawFont(String.valueOf(MyTank.myGold), 530, 85, 1, 1);
        		draw(backgndUpgrade,camera_w/2,camera_h/2,camera_w,camera_h);
        		button_In_UPGRADE_state();
        		if(changingState)
        			fading();
        		else
        			recover();
    			break;
        	case SELECT_LEVEL:
        		draw(backgndSelectLV,camera_w/2,camera_h/2,camera_w,camera_h);
        		button_In_SELECT_LEVEL_state();
        		if(changingState)
        			fading();
        		else
        			recover();
    			break;
    		case PLAY:
    		case PAUSE:
    		case HELP:
    		case BACKTOMENU:
    		case LVCOMPLETE:
    		case LVFAILED:
    			if(!soundManager.isPlayingSound() || CURRENT_SOUND != SOUND_BGM_PLAY){
    				soundManager.playSound(SOUND_BGM_PLAY);
    				CURRENT_SOUND = SOUND_BGM_PLAY;
    			}
    			if(numEnemy <= 0 && state != State.LVFAILED){
    				state = State.LVCOMPLETE;
    				if(currentLevel < maxLevel)
    					unlockLV.put(currentLevel+1, true);
        		}
        		initBulletX = (float)(player.x-Math.cos(0.0174532925*gunRotation)*player.width/1.5);
        		initBulletY = (float)(player.y-Math.sin(0.0174532925*gunRotation)*player.height/1.5);
        		
        		if(state != State.BACKTOMENU && state != State.PAUSE && state != State.HELP){
        			player.setDX(0);
        	        player.setDY(0);
        			if(state != State.LVFAILED)
        				input();
            		
                    //if(!soundManager.isPlayingSound()){
                    	for (int p = 0; p < entities.size(); p++) {
                    		Entity entity = entities.get(p);
                    		
                        	if(entity instanceof MyTank){
                        		player.move(delta, bodyAng);
                    		}else if(entity instanceof EnemyTank){
                    			map.setArrayMap(currentLevel,entities);
                    			if(state != State.LVFAILED)
                    				((EnemyTank)entity).move(map,delta,player.x,player.y);
                        	}
                        	else
                        		entity.move(delta);
                        	if(map.blocked((int)entity.x/map.TILE_SIZE, (int)entity.y/map.TILE_SIZE)){
                        		if(entity instanceof Gold){
                        			entity.setDX(0);
            						entity.setDY(0);
                            	} else if(entity instanceof Bullet){
                            		if(entity instanceof MyCannonBullet || entity instanceof MyRocketBullet){
            							entities.add(new BombEffect_BigBullet(this,entity.x,entity.y));
            							soundManager.playEffect(SOUND_BOMB_TANK);
            							if(entity instanceof MyRocketBullet)
            								player.rocketReleased = false;
            						} else {
            							entities.add(new BulletShotEffect(this,entity.x,entity.y));
            							soundManager.playEffect(SOUND_SHOT_BRICK);
            						}
            						entity.setDX(0);
            						entity.setDY(0);
            						((Bullet) entity).used = true;
            						removeList.add(entity);
                				}
        					}
            			}
                    //}
                    	
                    
                    
                    // check collisions and move
            		for (int p = 0; p < entities.size(); p++) {
            			for (int s = p + 1; s < entities.size(); s++) {
            				Entity me = entities.get(p);
            				Entity him = entities.get(s);
            				if (me.collidesWith(him)) {
            					me.collidedWith(him);
            					him.collidedWith(me);
            				}
            			}
            		}
            		//set camera
                    setCamera();
        		}
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                glOrtho(camera_x ,640+camera_x ,650+camera_y ,camera_y ,-1 , 1);
                
        		entities.removeAll(removeList);
        		removeList.clear();

        		if(state == State.PAUSE || state == State.BACKTOMENU || state == State.HELP || 
        				state == State.LVCOMPLETE || state == State.LVFAILED)
        			glColor3f(0.5f, 0.5f, 0.5f);
        		
                //draw
                map.draw();
                for ( Entity entity : entities ) {
                	if(entity instanceof Gold || entity instanceof HPpotion)
                		entity.draw();
    			}
                for ( Entity entity : entities ) {
                	if(!(entity instanceof Effect) && !(entity instanceof Gold) && !(entity instanceof HPpotion))
                		entity.draw();
    			}
                for ( Entity entity : entities ) {
                	if(entity instanceof Effect)
                		entity.draw();
    			}
                button_In_PLAY_state();
                if(changingState)
        			fading();
                glColor3f(1, 1, 1);
    			break;
    		}
            
            Display.update();
            Display.sync(60);
        }
        soundManager.destroy();
        Display.destroy();
        System.exit(0);
    }
    
    private void recover() {
    	if(state == State.PLAY)
    		fade = 0;
    	if (fade > 0) {
            fade -= 10f;
        } else {
        	fade = 0;
            glColor3f(1, 1, 1);
        }
    	glPushMatrix();
        glColor4f(1, 1, 1, (float) Math.sin(Math.toRadians(fade)));
        draw(fadeTex,camera_w/2,camera_h/2,camera_w,camera_h);
        glColor3f(1, 1, 1);
        glPopMatrix();
    }
    
    private void fading() {
    	if(state == State.BACKTOMENU || state == State.LVCOMPLETE || state == State.LVFAILED)
    		fade = 90;
    	if (fade < 90) {
            fade += 10f;
        } else {
        	fade = 90;
            state = nextState;
            switch(state){
    		case UPGRADE:
    			move_to_UPGRADE_state();
    			break;
    		case SELECT_LEVEL:
    			move_to_SELECT_LEVEL_state();
    			break;
    		case PLAY:
    			move_to_PLAY_state(nextLV);
    			break;
			default:
				break;
            }
            changingState = false;
        }
    	glPushMatrix();
        glColor4f(1, 1, 1, (float) Math.sin(Math.toRadians(fade)));
        draw(fadeTex,camera_w/2,camera_h/2,camera_w,camera_h);
        glColor3f(1, 1, 1);
        glPopMatrix();
    }
    
    private void move_to_UPGRADE_state() {
    	btn = new Button[8];            	        		
		btn[0] = new Button(this,CurrentButton.MENU,215,545);
		btn[1] = new Button(this,CurrentButton.PLAY,430,545);
		btn[2] = new Button(this,CurrentButton.UPGRD_MINIGUN,374,254);
		btn[3] = new Button(this,CurrentButton.UPGRD_SHOTGUN,504,254);
		btn[4] = new Button(this,CurrentButton.UPGRD_CANNON,374,378);
		btn[5] = new Button(this,CurrentButton.UPGRD_ROCKET,504,378);
		btn[6] = new Button(this,CurrentButton.UPGRD_ARMOR,180,252);
		btn[7] = new Button(this,CurrentButton.UPGRD_SPEED,180,370);
		state = State.UPGRADE;
    }
    
    private void move_to_SELECT_LEVEL_state() {
    	btn = new Button[11];            	        		
    	btn[0] = new Button(this,CurrentButton.BACKTOUPGRADE,320,550);
    	btn[1] = new Button(this,CurrentButton.LV1,120,250);
    	btn[2] = new Button(this,CurrentButton.LV2,220,250);
    	btn[3] = new Button(this,CurrentButton.LV3,320,250);
    	btn[4] = new Button(this,CurrentButton.LV4,420,250);
    	btn[5] = new Button(this,CurrentButton.LV5,520,250);
    	btn[6] = new Button(this,CurrentButton.LV6,120,370);
    	btn[7] = new Button(this,CurrentButton.LV7,220,370);
    	btn[8] = new Button(this,CurrentButton.LV8,320,370);
    	btn[9] = new Button(this,CurrentButton.LV9,420,370);
    	btn[10] = new Button(this,CurrentButton.LV10,520,370);
    	state = State.SELECT_LEVEL;
    }
    
    private void move_to_PLAY_state(int lv) {
    	SetGame(lv);
    	btn = new Button[7];            	        		
    	btn[0] = new Button(this,CurrentButton.MENU,143,610);
    	btn[1] = new Button(this,CurrentButton.PAUSE,533,610);
    	btn[2] = new Button(this,CurrentButton.CONTINUE,320,350);
    	btn[3] = new Button(this,CurrentButton.BCK_TO_MENU_YES,250,350);
    	btn[4] = new Button(this,CurrentButton.BCK_TO_MENU_NO,390,350);
    	btn[5] = new Button(this,CurrentButton.HELP,570,610);
    	btn[6] = new Button(this,CurrentButton.PLAY,320,480);
    	soundManager.stopPlayingSound();
    	soundManager.playEffect(SOUND_PLAY);
    	state = State.PLAY;
    }

	private void button_In_INTRO_state() {
    	btn = new Button[1];	
		btn[0] = new Button(this,CurrentButton.PLAY_INTRO,320,495);
		if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY()))
			btn[0].draw_OnMouseOver();
		else
			btn[0].draw();
		while (Mouse.next()) {
    	    if (Mouse.getEventButtonState()) {
    	        switch (Mouse.getEventButton()) {
    	        case 0:
    	        	if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        		soundManager.playEffect(SOUND_CLICK);
    	        		currentButton = CurrentButton.PLAY_INTRO;
    	        	} else
            			currentButton = CurrentButton.NONE;
	        		break;
    	        }
    	    } else {	// mouse release
    	    	switch (Mouse.getEventButton()) {
    	    	case 0:
    	    		if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			soundManager.playEffect(SOUND_RELEASE);
    	    			if(currentButton == CurrentButton.PLAY_INTRO){
    	    				nextState = State.UPGRADE;
    	    				changingState = true;
    	    				//move_to_UPGRADE_state();
	        			}
            		} else
        				currentButton = CurrentButton.NONE;
        			break;
    	    	}
    	    }
    	}
	}

	private void button_In_UPGRADE_state() {
    	for(int i = 0;i < btn.length;i++){
			if(btn[i].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
				switch(btn[i].thisButton){
				case UPGRD_MINIGUN:
				case UPGRD_SHOTGUN:
				case UPGRD_CANNON:
				case UPGRD_ROCKET:
				case UPGRD_ARMOR:
				case UPGRD_SPEED:
					if(!btn[i].maxUpgrd)
						drawPriceLabel(btn[i].thisButton, String.valueOf(btn[i].priceToUpgrd), (int)btn[i].x, (int)btn[i].y-(btn[i].width/2+3));
					break;
				default:
					break;
				}
				btn[i].draw_OnMouseOver();
			} else
    			btn[i].draw();
		}
		while (Mouse.next()) {
    	    if (Mouse.getEventButtonState()) {
    	        switch (Mouse.getEventButton()) {
    	        case 0:
    	        	if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        		soundManager.playEffect(SOUND_CLICK);
    	        		currentButton = CurrentButton.MENU;
    	        	} else if(btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        		soundManager.playEffect(SOUND_CLICK);
    	        		currentButton = CurrentButton.PLAY;
    	        	} else if(btn[2].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        		soundManager.playEffect(SOUND_CLICK);
    	        		currentButton = CurrentButton.UPGRD_MINIGUN;
    	        	} else if(btn[3].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        		soundManager.playEffect(SOUND_CLICK);
    	        		currentButton = CurrentButton.UPGRD_SHOTGUN;
    	        	} else if(btn[4].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        		soundManager.playEffect(SOUND_CLICK);
    	        		currentButton = CurrentButton.UPGRD_CANNON;
    	        	} else if(btn[5].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        		soundManager.playEffect(SOUND_CLICK);
    	        		currentButton = CurrentButton.UPGRD_ROCKET;
    	        	} else if(btn[6].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        		soundManager.playEffect(SOUND_CLICK);
    	        		currentButton = CurrentButton.UPGRD_ARMOR;
    	        	} else if(btn[7].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        		soundManager.playEffect(SOUND_CLICK);
    	        		currentButton = CurrentButton.UPGRD_SPEED;
    	        	} else
    	    			currentButton = CurrentButton.NONE;
	        		break;
    	        }
    	    } else {	// mouse release
    	    	switch (Mouse.getEventButton()) {
    	    	case 0:
    	    		if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			soundManager.playEffect(SOUND_RELEASE);
    	    			if(currentButton == CurrentButton.MENU){
    	    				nextState = State.INTRO;
    	    				changingState = true;
    	    			}            	        		
    	    		} else if(btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			soundManager.playEffect(SOUND_RELEASE);
    	    			if(currentButton == CurrentButton.PLAY){
    	    				nextState = State.SELECT_LEVEL;
    	    				changingState = true;
    	    				//move_to_SELECT_LEVEL_state();
    	    			}
    	    		} else if(btn[2].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			if(currentButton == CurrentButton.UPGRD_MINIGUN){
    	    				if(MyTank.myGold > btn[2].priceToUpgrd){
    	    					btn[2].upgrade();
    	    					soundManager.playEffect(SOUND_UPGRADE);
    	    				} else {
    	    					not_enough_gold = true;
    	    					soundManager.playEffect(SOUND_NO_MONEY);
    	    				}
    	    			}
    	    		} else if(btn[3].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			if(currentButton == CurrentButton.UPGRD_SHOTGUN){
    	    				if(MyTank.myGold > btn[3].priceToUpgrd){
    	    					btn[3].upgrade();
    	    					soundManager.playEffect(SOUND_UPGRADE);
    	    				} else {
    	    					not_enough_gold = true;
    	    					soundManager.playEffect(SOUND_NO_MONEY);
    	    				}
    	    			}
    	    		} else if(btn[4].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			if(currentButton == CurrentButton.UPGRD_CANNON){
    	    				if(MyTank.myGold > btn[4].priceToUpgrd){
    	    					btn[4].upgrade();
    	    					soundManager.playEffect(SOUND_UPGRADE);
    	    				} else {
    	    					not_enough_gold = true;
    	    					soundManager.playEffect(SOUND_NO_MONEY);
    	    				}
    	    			}
    	    		} else if(btn[5].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			if(currentButton == CurrentButton.UPGRD_ROCKET){
    	    				if(MyTank.myGold > btn[5].priceToUpgrd){
    	    					btn[5].upgrade();
    	    					soundManager.playEffect(SOUND_UPGRADE);
    	    				} else {
    	    					not_enough_gold = true;
    	    					soundManager.playEffect(SOUND_NO_MONEY);
    	    				}
    	    			}
    	    		} else if(btn[6].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			if(currentButton == CurrentButton.UPGRD_ARMOR){
    	    				if(MyTank.myGold > btn[6].priceToUpgrd){
    	    					btn[6].upgrade();
    	    					soundManager.playEffect(SOUND_UPGRADE);
    	    				} else {
    	    					not_enough_gold = true;
    	    					soundManager.playEffect(SOUND_NO_MONEY);
    	    				}
    	    			}
    	    		} else if(btn[7].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			if(currentButton == CurrentButton.UPGRD_SPEED){
    	    				if(MyTank.myGold > btn[7].priceToUpgrd){
    	    					btn[7].upgrade();
    	    					soundManager.playEffect(SOUND_UPGRADE);
    	    				} else {
    	    					not_enough_gold = true;
    	    					soundManager.playEffect(SOUND_NO_MONEY);
    	    				}
    	    			}
    	    		} else
    	    			currentButton = CurrentButton.NONE;
        			break;
    	    	}
    	    }
    	}
	}

	private void button_In_SELECT_LEVEL_state() {
		// button BACKTOUPGRADE
		if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY()))
			btn[0].draw_OnMouseOver();
		else
			btn[0].draw();
		// button LV1 - LVmax
		for(int i = 1;i < btn.length;i++){
			if(unlockLV.get(i)){
				if(btn[i].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())) {
					btn[i].increaseBtnSize();
					btn[i].draw();
				} else {
					btn[i].decreaseBtnSize();
					btn[i].draw();
				}
			} else {
				if(btn[i].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())) {
					btn[i].increaseBtnSize();
					btn[i].draw_LVlock();
				} else {
					btn[i].decreaseBtnSize();
					btn[i].draw_LVlock();
				}
			}
		}
		while (Mouse.next()) {
    	    if (Mouse.getEventButtonState()) {
    	        switch (Mouse.getEventButton()) {
    	        case 0:
    	        	boolean btnLVclicked = false;
    	        	for(int i = 1;i <= maxLevel;i++){
    	    			if(btn[i].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY()) && unlockLV.get(i)){
    	    				soundManager.playEffect(SOUND_CLICK);
    	    				switch(i){
    	    				case 1:
    	    					currentButton = CurrentButton.LV1;
    	    					break;
    	    				case 2:
    	    					currentButton = CurrentButton.LV2;
    	    					break;
    	    				case 3:
    	    					currentButton = CurrentButton.LV3;
    	    					break;
    	    				case 4:
    	    					currentButton = CurrentButton.LV4;
    	    					break;
    	    				case 5:
    	    					currentButton = CurrentButton.LV5;
    	    					break;
    	    				case 6:
    	    					currentButton = CurrentButton.LV6;
    	    					break;
    	    				case 7:
    	    					currentButton = CurrentButton.LV7;
    	    					break;
    	    				case 8:
    	    					currentButton = CurrentButton.LV8;
    	    					break;
    	    				case 9:
    	    					currentButton = CurrentButton.LV9;
    	    					break;
    	    				case 10:
    	    					currentButton = CurrentButton.LV10;
    	    					break;
    	    				}
    	    				btnLVclicked = true;
    	    			}
    	    		}
    	        	if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        		soundManager.playEffect(SOUND_CLICK);
    	        		currentButton = CurrentButton.BACKTOUPGRADE;
    	        	} else if(btnLVclicked){
    	        	} else
    	    			currentButton = CurrentButton.NONE;
	        		break;
    	        }
    	    } else {	// mouse release
    	    	switch (Mouse.getEventButton()) {
    	    	case 0:
    	    		boolean btnLVreleased = false;
    	        	for(int i = 1;i <= maxLevel;i++){
    	    			if(btn[i].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY()) && unlockLV.get(i)){
    	    				soundManager.playEffect(SOUND_RELEASE);
    	    				btnLVreleased = true;
    	    				if(currentButton == CurrentButton.LV1 || currentButton == CurrentButton.LV2 || 
    	    						currentButton == CurrentButton.LV3 || currentButton == CurrentButton.LV4 || 
    	    						currentButton == CurrentButton.LV5 || currentButton == CurrentButton.LV6 || 
    	    						currentButton == CurrentButton.LV7 || currentButton == CurrentButton.LV8 || 
    	    						currentButton == CurrentButton.LV9 || currentButton == CurrentButton.LV10){
    	    					nextState = State.PLAY;
    	    					nextLV = i;
        	    				changingState = true;
    	    					//move_to_PLAY_state(i);
        	    				break;
        	    			}
    	    			}
    	    		}
    	    		if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			soundManager.playEffect(SOUND_RELEASE);
    	    			if(currentButton == CurrentButton.BACKTOUPGRADE){
    	    				nextState = State.UPGRADE;
    	    				changingState = true;
    	    				//move_to_UPGRADE_state();
    	    			}            	        		
    	    		} else if(btnLVreleased){
    	    		} else
    	    			currentButton = CurrentButton.NONE;
        			break;
    	    	}
    	    }
    	}
	}
	
	private void button_In_PLAY_state() {
		glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
		glOrtho(0 ,640 ,650 ,0 ,-1 , 1);
		
		setTimeToShowProfitMiniLabel();
		draw(profitMiniLabel, 170, 585-miniLabel_Y, 209, 26);
		drawFont(String.valueOf(MyTank.profit), 270, 576-miniLabel_Y, 0.7f, 1);
		
		draw(selectGunBar, 330, 610, 542, 81);
		drawHPpipe(360,635,260,21);
		// ########### btn menu ###########
		if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY()))
			btn[0].draw_OnMouseOver();
		else
			btn[0].draw();
		// ########### gun label ###########
		if(!player.unlockGun.get(TankEntity.GunType.MINIGUN)){
			draw(labelMinigun_lock, 258,600,44,42);
		} else if(player.gunType == TankEntity.GunType.MINIGUN){
			draw(labelMinigun, 258,600,44,42);
		} else
			draw(labelMinigun_unuse, 258,600,44,42);
		
		if(!player.unlockGun.get(TankEntity.GunType.SHOTGUN)){
			draw(labelShotgun_lock, 327,600,44,42);
		} else if(player.gunType == TankEntity.GunType.SHOTGUN){
			draw(labelShotgun, 327,600,44,42);
		} else
			draw(labelShotgun_unuse, 327,600,44,42);
		
		if(!player.unlockGun.get(TankEntity.GunType.CANNON)){
			draw(labelCannon_lock, 392,600,44,42);
		} else if(player.gunType == TankEntity.GunType.CANNON){
			draw(labelCannon, 392,600,44,42);
		} else
			draw(labelCannon_unuse, 392,600,44,42);
		
		if(!player.unlockGun.get(TankEntity.GunType.ROCKET)){
			draw(labelRocket_lock, 462,600,44,42);
		} else if(player.gunType == TankEntity.GunType.ROCKET){
			draw(labelRocket, 462,600,44,42);
		} else
			draw(labelRocket_unuse, 462,600,44,42);
		// ########### btn pause ###########
		if(state == State.PAUSE){
			btn[1].draw_Clicked();
		} else {
			if(btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY()))
				btn[1].draw_OnMouseOver();
			else
				btn[1].draw();
		}
		// ########### btn help ###########
		if(state == State.HELP){
			btn[5].draw_Clicked();
		} else {
			if(btn[5].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY()))
				btn[5].draw_OnMouseOver();
			else
				btn[5].draw();
		}
		
		glColor3f(1, 1, 1);
		// ########### btn continue ###########
		if(state == State.PAUSE){
			draw(pauseLabel, 320, 273, 161, 46);
			if(btn[2].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY()))
				btn[2].draw_OnMouseOver();
    		else
    			btn[2].draw();
		}
		// ########### btn play ###########
		if(state == State.HELP){
			draw(helpLabel, 320, 273, 282, 526);
			if(btn[6].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY()))
				btn[6].draw_OnMouseOver();
    		else
    			btn[6].draw();
		}
		// ########### btn yes,no ###########
		if(state == State.BACKTOMENU){
			draw(backToMenuLabel, 320, 303, 321, 194);
			for(int i = 3;i <= 4;i++){
				if(btn[i].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY()))
					btn[i].draw_OnMouseOver();
	    		else
	    			btn[i].draw();
			}
		}
		if(state == State.LVCOMPLETE){
			setTimeToShowEndgameLabel();
			draw(lvCompleteLabel, 320+Label_X, 150+Label_Y, 361, 92);
			draw(profitLabel, 320-Label_X, 490-Label_Y, 359, 74);
			drawFont(String.valueOf(MyTank.profit), 455-Label_X, 480-Label_Y, 1, 1);
		}
		if(state == State.LVFAILED){
			setTimeToShowEndgameLabel();
			draw(lvFailedLabel, 320+Label_X, 150+Label_Y, 361, 92);
			draw(profitLabel, 320-Label_X, 490-Label_Y, 359, 74);
			drawFont(String.valueOf(MyTank.profit), 455-Label_X, 480-Label_Y, 1, 1);
		}
		while (Mouse.next()) {
    	    if (Mouse.getEventButtonState()) {
    	        switch (Mouse.getEventButton()) {
    	        case 0:
    	        	if(state == State.PLAY){
    	        		if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
        	        		soundManager.playEffect(SOUND_CLICK);
        	        		currentButton = CurrentButton.MENU;
        	        	} else if(btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
        	        		soundManager.playEffect(SOUND_CLICK);
        	        		currentButton = CurrentButton.PAUSE;
        	        	} else if(btn[5].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
        	        		soundManager.playEffect(SOUND_CLICK);
        	        		currentButton = CurrentButton.HELP;
        	        	} else
        	    			currentButton = CurrentButton.NONE;
    	        	} else if(state == State.PAUSE){
    	        		if(btn[2].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        			soundManager.playEffect(SOUND_CLICK);
    	        			currentButton = CurrentButton.CONTINUE;
    	        		} else
        	    			currentButton = CurrentButton.NONE;
    	        	} else if(state == State.HELP){
    	        		if(btn[6].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        			soundManager.playEffect(SOUND_CLICK);
    	        			currentButton = CurrentButton.PLAY;
    	        		} else
        	    			currentButton = CurrentButton.NONE;
    	        	} else if(state == State.BACKTOMENU){
    	        		if(btn[3].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
        	        		soundManager.playEffect(SOUND_CLICK);
        	        		currentButton = CurrentButton.BCK_TO_MENU_YES;
        	        	} else if(btn[4].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
        	        		soundManager.playEffect(SOUND_CLICK);
        	        		currentButton = CurrentButton.BCK_TO_MENU_NO;
        	        	} else
        	    			currentButton = CurrentButton.NONE;
    	        	}
	        		break;
    	        }
    	    } else {	// mouse release
    	    	switch (Mouse.getEventButton()) {
    	    	case 0:
    	    		if(state == State.PLAY){
    	    			if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
        	    			soundManager.playEffect(SOUND_RELEASE);
        	    			if(currentButton == CurrentButton.MENU){
        	    				state = State.BACKTOMENU;
        	    			}
        	    		} else if(btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
        	    			soundManager.playEffect(SOUND_RELEASE);
        	    			if(currentButton == CurrentButton.PAUSE){
                				state = State.PAUSE;
        	    			}
        	    		} else if(btn[5].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
        	    			soundManager.playEffect(SOUND_RELEASE);
        	    			if(currentButton == CurrentButton.HELP){
                				state = State.HELP;
        	    			}
        	    		} else
        	    			currentButton = CurrentButton.NONE;
    	        	} else if(state == State.PAUSE){
    	        		if(btn[2].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        			soundManager.playEffect(SOUND_RELEASE);
    	        			if(currentButton == CurrentButton.CONTINUE){
    	        				state = State.PLAY;
        	    			}
    	        		} else
        	    			currentButton = CurrentButton.NONE;
    	        	} else if(state == State.HELP){
    	        		if(btn[6].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        			soundManager.playEffect(SOUND_RELEASE);
    	        			if(currentButton == CurrentButton.PLAY){
    	        				state = State.PLAY;
        	    			}
    	        		} else
        	    			currentButton = CurrentButton.NONE;
    	        	} else if(state == State.BACKTOMENU){
    	        		if(btn[3].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
        	        		soundManager.playEffect(SOUND_RELEASE);
        	        		if(currentButton == CurrentButton.BCK_TO_MENU_YES){
        	        			MyTank.profit = 0;
        	        			nextState = State.UPGRADE;
        	    				changingState = true;
        	        			//move_to_UPGRADE_state();
        	    			}
        	        	} else if(btn[4].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
        	        		soundManager.playEffect(SOUND_RELEASE);
        	        		if(currentButton == CurrentButton.BCK_TO_MENU_NO){
    	        				state = State.PLAY;
        	    			}
        	        	} else
        	    			currentButton = CurrentButton.NONE;
    	        	}
        			break;
    	    	}
    	    }
    	}
	}
	
	private void setTimeToShowEndgameLabel() {
		timeShowLabel += delta;
		if(timeShowLabel > 50 && timeShowLabel < 250){
			Label_Y += 3;
		} else if(timeShowLabel >= 2400 && timeShowLabel < 2600){
			Label_X -= (2600 - timeShowLabel)/20;
		} else if(timeShowLabel >= 2600 && timeShowLabel < 3100){
			Label_X += (timeShowLabel-2600)/10;
		} else if(timeShowLabel > 3100){
			timeShowLabel = 0;
			Label_X = 0;
			Label_Y = 0;
			MyTank.myGold += MyTank.profit;
			MyTank.profit = 0;
			nextState = State.UPGRADE;
			changingState = true;
			//move_to_UPGRADE_state();
			glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
			glOrtho(0 ,640 ,650 ,0 ,-1 , 1);
		}
	}
	
	private void setTimeToShowProfitMiniLabel() {
		if(MyTank.gotGold){
			if(miniLabel_Y < 27){
				miniLabel_Y += 3;
			}
		} else {
			if(miniLabel_Y > 0){
				miniLabel_Y -= 3;
			}
		}
	}

	private void input() {
		boolean KEY_W = Keyboard.isKeyDown(Keyboard.KEY_W);
    	boolean KEY_S = Keyboard.isKeyDown(Keyboard.KEY_S);
    	boolean KEY_D = Keyboard.isKeyDown(Keyboard.KEY_D);
    	boolean KEY_A = Keyboard.isKeyDown(Keyboard.KEY_A);
    	boolean KEY_1 = Keyboard.isKeyDown(Keyboard.KEY_1);
    	boolean KEY_2 = Keyboard.isKeyDown(Keyboard.KEY_2);
    	boolean KEY_3 = Keyboard.isKeyDown(Keyboard.KEY_3);
    	boolean KEY_4 = Keyboard.isKeyDown(Keyboard.KEY_4);
    	
        if(KEY_W && !KEY_S){
        	bodyAng = 90;
        	if(KEY_A)
        		bodyAng = 45;
        	if(KEY_D)
        		bodyAng = 135;
        	player.setDY(-player.speed);
        }
        if(KEY_S && !KEY_W){
        	bodyAng = 270;
        	if(KEY_A)
        		bodyAng = 315;
        	if(KEY_D)
        		bodyAng = 225;
        	player.setDY(player.speed);
        }
        if(KEY_D && !KEY_A){
        	bodyAng = 180;
        	if(KEY_W)
        		bodyAng = 135;
        	if(KEY_S)
        		bodyAng = 225;
        	player.setDX(player.speed);
        }
        if(KEY_A && !KEY_D){
        	bodyAng = 0;
        	if(KEY_W)
        		bodyAng = 45;
        	if(KEY_S)
        		bodyAng = 315;
        	player.setDX(-player.speed);
        }
        if(KEY_1 || KEY_2 || KEY_3 || KEY_4){
        	while (Keyboard.next()) {
        	    if (Keyboard.getEventKeyState()) {
        	        switch (Keyboard.getEventKey()) {
        	        	case Keyboard.KEY_1:
        	        		player.setGun(TankEntity.GunType.MINIGUN);
        	        		break;
        	        	case Keyboard.KEY_2:
        	        		player.setGun(TankEntity.GunType.SHOTGUN);
        	        		break;
        	        	case Keyboard.KEY_3:
        	        		player.setGun(TankEntity.GunType.CANNON);
        	        		break;
        	        	case Keyboard.KEY_4:
        	        		player.setGun(TankEntity.GunType.ROCKET);
        	        		break;
        	        }
        	    }
        	}
        	if(player.rocketReleased){
        		player.rocketReleased = false;
        		removeEntity(player.myRocketBullet);
        		entities.add(new BombEffect_BigBullet(this,player.myRocketBullet.x,player.myRocketBullet.y));
        		soundManager.playEffect(SOUND_BOMB_TANK);
        	}
        }
        /*if(KEY_1)
        	player.setGun(player.gunType.MINIGUN);
        if(KEY_2)
        	player.setGun(player.gunType.SHOTGUN);
        if(KEY_3)
        	player.setGun(player.gunType.RICOCHET);
        if(KEY_3)
        	player.setGun(player.gunType.CANNON);
        if(KEY_4){
        	//while (Mouse.next())
        	player.setGun(player.gunType.ROCKET);
        }
        if(KEY_6)
        	player.setGun(player.gunType.LASER);
        */
        
        //if(!soundManager.isPlayingSound()){
        	if(!btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())
        			&& !btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())
        			&& !btn[5].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
        		if(player.gunType == TankEntity.GunType.ROCKET){
	        		while (Mouse.next()) {
	            	    if (Mouse.getEventButtonState()) {
	            	        switch (Mouse.getEventButton()) {
	            	        	case 0:
	            	        		player.Fire(player.x,player.y,gunRotation);
	            	        		break;
	            	        }
	            	    }
	            	}
	        	/*} else if(player.gunType == player.gunType.RICOCHET){
	        		while (Mouse.next()) {
	            	    if (Mouse.getEventButtonState()) {
	            	        switch (Mouse.getEventButton()) {
	            	        	case 0:
	            	        		player.Fire(initBulletX,initBulletY,gunRotation);
	            	        		break;
	            	        }
	            	    } else {	// mouse release
	            	    	switch (Mouse.getEventButton()) {
	        	        		case 0:
	        	        			player.Fire(initBulletX,initBulletY,gunRotation);
	        	        			break;
	            	    	}
	            	    }
	            	}*/
	        	} else if(Mouse.isButtonDown(0)){
	            	player.Fire(initBulletX,initBulletY,gunRotation);
	            }
        	}
        //}
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
        	if(state != State.LVCOMPLETE && state != State.LVFAILED)
        		state = State.PAUSE;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_C)){
        	MyTank.myGold = 999999;
        }
	}
    
    private void setCamera(){
    	// mouse position (0,0) at center screen
    	float mouseX = (Mouse.getX() - camera_w/2)*0.5f;
    	float mouseY = (Mouse.getY() - camera_h/2)*0.5f;
    	
    	//String str = String.valueOf(gunRotation);
    	
    	// Mouse.getX(),Mouse.getY() position (0,0) at bottom left
    	//Display.setTitle(str);
    	//gunRotation = 57.2957795f*(float)Math.atan2(camera_h/2 - Mouse.getY(),Mouse.getX() - camera_w/2);
    	gunRotation = (float) (180/Math.PI*(float)Math.atan2(camera_h/2 - Mouse.getY(),Mouse.getX() - camera_w/2));
    	gunRotation += 180;
    	player.setGunAngle(gunRotation);
    	if(player.rocketReleased){
    		player.myRocketBullet.setDX((float)-Math.cos(0.0174532925*gunRotation)*player.myRocketBullet.moveSpeed);
    		player.myRocketBullet.setDY((float)-Math.sin(0.0174532925*gunRotation)*player.myRocketBullet.moveSpeed);
    		camera_x = (int)(player.myRocketBullet.get_centerX() - camera_w/2);
        	camera_y = (int)(player.myRocketBullet.get_centerY() - camera_h/2);
        	if(Math.abs(camera_x_tmp - camera_x) > 5){
        		if(camera_x > camera_x_tmp){
        			camera_x = camera_x_tmp + 5;
        			camera_x_tmp += 5;
        		} else {
        			camera_x = camera_x_tmp - 5;
        			camera_x_tmp -= 5;
        		}
        	} else {
        		camera_x_tmp = camera_x;
        	}
        	if(Math.abs(camera_y_tmp - camera_y) > 5){
        		if(camera_y > camera_y_tmp){
        			camera_y = camera_y_tmp + 5;
        			camera_y_tmp += 5;
        		} else {
        			camera_y = camera_y_tmp - 5;
        			camera_y_tmp -= 5;
        		}
        	} else {
            	camera_y_tmp = camera_y;
        	}
    	} else {
    		camera_x = (int)mouseX + (int)(player.get_centerX() - camera_w/2);
        	camera_y = -(int)mouseY + (int)(player.get_centerY() - camera_h/2);
        	if(Math.abs(camera_x_tmp - camera_x) > 25){
        		if(camera_x > camera_x_tmp){
        			camera_x = camera_x_tmp + 25;
        			camera_x_tmp += 25;
        		} else {
        			camera_x = camera_x_tmp - 25;
        			camera_x_tmp -= 25;
        		}
        	} else {
        		camera_x_tmp = camera_x;
        	}
        	if(Math.abs(camera_y_tmp - camera_y) > 25){
        		if(camera_y > camera_y_tmp){
        			camera_y = camera_y_tmp + 25;
        			camera_y_tmp += 25;
        		} else {
        			camera_y = camera_y_tmp - 25;
        			camera_y_tmp -= 25;
        		}
        	} else {
            	camera_y_tmp = camera_y;
        	}
    	}
    }
    
    public static void addEntity(Entity entity) {
    	entities.add(entity);
	}
    
    public static void removeEntity(Entity entity) {
    	if(entity instanceof MyTank)
    		state = State.LVFAILED;
		removeList.add(entity);
	}
    
    private void loadResource() {
    	fadeTex = loadTexture("black.png");
    	backgndIntro = loadTexture("intro.png");
    	backgndUpgrade = loadTexture("upgrade.png");
    	moneyBackgnd = loadTexture("moneyBackgnd.png");
    	backgndSelectLV = loadTexture("selectLV.png");
    	selectGunBar = loadTexture("selectGunBar.png");
    	pauseLabel = loadTexture("pauseLabel.png");
    	helpLabel = loadTexture("helpLabel.png");
    	backToMenuLabel = loadTexture("backToMenuLabel.png");
    	lvCompleteLabel = loadTexture("LVcomplete.png");
    	lvFailedLabel = loadTexture("LVfailed.png");
    	profitLabel = loadTexture("profitLabel.png");
    	profitMiniLabel = loadTexture("profitMiniLabel.png");
    	HPpipe = loadTexture("HPpipe.png");
		HPred = loadTexture("HPred.png");
		HPblue = loadTexture("HPblue.png");
		labelMinigun = loadTexture("labelMinigun.png");
		labelShotgun = loadTexture("labelShotgun.png");
		labelCannon = loadTexture("labelCannon.png");
		labelRocket = loadTexture("labelRocket.png");
		labelMinigun_unuse = loadTexture("labelMinigun_unuse.png");
		labelShotgun_unuse = loadTexture("labelShotgun_unuse.png");
		labelCannon_unuse = loadTexture("labelCannon_unuse.png");
		labelRocket_unuse = loadTexture("labelRocket_unuse.png");
		labelMinigun_lock = loadTexture("labelMinigun_lock.png");
		labelShotgun_lock = loadTexture("labelShotgun_lock.png");
		labelCannon_lock = loadTexture("labelCannon_lock.png");
		labelRocket_lock = loadTexture("labelRocket_lock.png");
		number = loadTexture("number/num.png");
		number_brown = loadTexture("number/num_brown.png");
		number_no_gold = loadTexture("number/num_noGold.png");
		labelTextPrice = loadTexture("labelPrice.png");
		labelTextArmor = loadTexture("labelTextArmor.png");
		labelTextSpeed = loadTexture("labelTextSpeed.png");
		labelTextMinigun = loadTexture("labelTextMinigun.png");
		labelTextShotgun = loadTexture("labelTextShotgun.png");
		labelTextCannon = loadTexture("labelTextCannon.png");
		labelTextRocket = loadTexture("labelTextRocket.png");
    	player = new MyTank(this,200);
    	brick = new Brick[213];
    	brick_2 = new Brick_2[31];
    	box = new Box[27];
    	bmWall = new BombWall[15];
    	oilTank = new OilTank[8];
    	enemyMinigun = new EnemyMinigun[21];
    	enemyShotgun = new EnemyShotgun[10];
    	enemyCannon = new EnemyCannon[10];
    	//enemyRocket = new EnemyRocket[10];
    	enemyMinigunTurret = new EnemyMinigunTurret[10];
    	enemyShotgunTurret = new EnemyShotgunTurret[10];
    	enemyCannonTurret = new EnemyCannonTurret[10];
    	turret = new Turret[6];
    	gold = new Gold[200];
    	for(int i = 0;i < brick.length;i++){
        	brick[i] = new Brick(this,30);
    	}
    	for(int i = 0;i < brick_2.length;i++){
        	brick_2[i] = new Brick_2(this,30);
    	}
    	for(int i = 0;i < box.length;i++){
    		box[i] = new Box(this,15);
    	}
    	for(int i = 0;i < bmWall.length;i++){
        	bmWall[i] = new BombWall(this,30);
        }
    	for(int i = 0;i < oilTank.length;i++){
        	oilTank[i] = new OilTank(this,15);
    	}
    	for (int i = 0; i < enemyMinigun.length; i++) {
			enemyMinigun[i] = new EnemyMinigun(this,50);
    	}
    	for (int i = 0; i < enemyShotgun.length; i++) {
    		enemyShotgun[i] = new EnemyShotgun(this,50);
    	}
    	for (int i = 0; i < enemyCannon.length; i++) {
    		enemyCannon[i] = new EnemyCannon(this,50);
    	}
    	/*for (int i = 0; i < enemyRocket.length; i++) {
    		enemyRocket[i] = new EnemyRocket(this,50);
    	}*/
    	for (int i = 0; i < enemyMinigunTurret.length; i++) {
			enemyMinigunTurret[i] = new EnemyMinigunTurret(this,50);
    	}
    	for (int i = 0; i < enemyShotgunTurret.length; i++) {
    		enemyShotgunTurret[i] = new EnemyShotgunTurret(this,50);
    	}
    	for (int i = 0; i < enemyCannonTurret.length; i++) {
    		enemyCannonTurret[i] = new EnemyCannonTurret(this,50);
    	}
    	for(int i = 0;i < turret.length;i++){
    		turret[i] = new Turret(this, 50);
    	}
    	for(int i = 0;i < gold.length;i++){
    		gold[i] = new Gold(this);
    	}
	}
    
    private void SetGame(int LV){
    	entities.clear();
    	camera_x_tmp = 0;
    	camera_y_tmp = 0;
    	currentLevel = LV;
    	switch (LV) {
		case 1:
			map.createMap(1);
			player.setHP(player.maxHP);
	        player.setPositionToMap(2,3);
	        player.reset();
	        player.setGun(TankEntity.GunType.MINIGUN);
	        player.rocketReleased = false;
	        entities.add(player);
	        brick[0].setPositionToMap(9, 1);
	        brick[1].setPositionToMap(10, 1);
	        brick[2].setPositionToMap(11, 1);
	        brick[3].setPositionToMap(5, 5);
	        brick[4].setPositionToMap(7, 5);
	        brick[5].setPositionToMap(8, 5);
	        brick[6].setPositionToMap(9, 5);
	        brick[7].setPositionToMap(10, 5);
	        brick[8].setPositionToMap(11, 5);
	        for(int i = 0;i < 9;i++){
	        	brick[i].setHP(30);
	        	brick[i].reset();
	        	entities.add(brick[i]);
	        }
	        box[0].setPositionToMap(6, 1);
	        box[1].setPositionToMap(7, 1);
	        for(int i = 0;i < 2;i++){
	        	box[i].setHP(15);
	        	box[i].reset();
	        	entities.add(box[i]);
	        }
	        bmWall[0].setPositionToMap(4, 2);
	        bmWall[1].setPositionToMap(4, 3);
	        bmWall[2].setPositionToMap(4, 4);
	        bmWall[3].setPositionToMap(11, 2);
	        bmWall[4].setPositionToMap(11, 3);
	        bmWall[5].setPositionToMap(11, 4);
	        for(int i = 0;i < 6;i++){
	        	bmWall[i].setHP(30);
	        	bmWall[i].reset();
	        	entities.add(bmWall[i]);
	        }
	        oilTank[0].setPositionToMap(13, 1);
	        oilTank[1].setPositionToMap(6, 5);
	        for(int i = 0;i < 2;i++){
	        	oilTank[i].setHP(15);
	        	oilTank[i].reset();
	        	entities.add(oilTank[i]);
	        }
	        enemyMinigun[0].setPositionToMap(6, 4);
	        enemyMinigun[1].setPositionToMap(9, 3);
	        enemyMinigun[2].setPositionToMap(15, 2);
	        enemyMinigun[3].setPositionToMap(15, 3);
	        enemyMinigun[4].setPositionToMap(15, 4);
	        enemyMinigun[5].setPositionToMap(13, 2);
	        enemyMinigun[6].setPositionToMap(14, 4);
			for (int i = 0; i < 7; i++) {
				enemyMinigun[i].setHP(50);
				enemyMinigun[i].setBodyAngle(39);
				enemyMinigun[i].setMinigunAttck(1);
				enemyMinigun[i].reset();
				entities.add(enemyMinigun[i]);
			}
			turret[0].setHP(50);
			turret[0].setPositionToMap(14, 3);
			turret[0].reset();
			entities.add(turret[0]);
			numEnemy = 7 + 1;
			break;
		case 2:
			map.createMap(2);
			player.setHP(player.maxHP);
	        player.setPositionToMap(2,2);
	        player.reset();
	        player.setGun(TankEntity.GunType.MINIGUN);
	        player.rocketReleased = false;
	        entities.add(player);
	        brick[0].setPositionToMap(6, 1);
	        brick[1].setPositionToMap(6, 2);
	        brick[2].setPositionToMap(6, 3);
	        brick[3].setPositionToMap(6, 4);
	        brick[4].setPositionToMap(4, 8);
	        brick[5].setPositionToMap(5, 8);
	        brick[6].setPositionToMap(6, 8);
	        for(int i = 0;i < 7;i++){
	        	brick[i].setHP(30);
	        	brick[i].reset();
	        	entities.add(brick[i]);
	        }
			bmWall[0].setPositionToMap(4, 2);
	        bmWall[1].setPositionToMap(4, 3);
	        bmWall[2].setPositionToMap(4, 1);
	        for(int i = 0;i < 3;i++){
	        	bmWall[i].setHP(30);
	        	bmWall[i].reset();
	        	entities.add(bmWall[i]);
	        }
	        oilTank[0].setPositionToMap(2, 6);
	        oilTank[1].setPositionToMap(4, 6);
	        oilTank[2].setPositionToMap(6, 6);
	        oilTank[3].setPositionToMap(8, 6);
	        for(int i = 0;i < 4;i++){
	        	oilTank[i].setHP(15);
	        	oilTank[i].reset();
	        	entities.add(oilTank[i]);
	        }
	        enemyMinigun[0].setPositionToMap(8, 2);
	        enemyMinigun[1].setPositionToMap(4, 5);
	        enemyMinigun[2].setPositionToMap(6, 5);
	        enemyMinigun[3].setPositionToMap(7, 5);
	        enemyMinigun[4].setPositionToMap(2, 9);
	        enemyMinigun[5].setPositionToMap(3, 9);
	        enemyMinigun[6].setPositionToMap(4, 9);
	        enemyMinigun[7].setPositionToMap(5, 9);
	        enemyMinigun[8].setPositionToMap(6, 9);
			for (int i = 0; i < 9; i++) {
				enemyMinigun[i].setHP(60);
				enemyMinigun[i].setBodyAngle(39);
				enemyMinigun[i].setMinigunAttck(1);
				enemyMinigun[i].reset();
				entities.add(enemyMinigun[i]);
			}
	        turret[0].setHP(60);
			turret[0].setPositionToMap(2, 8);
			turret[0].reset();
			entities.add(turret[0]);
	        numEnemy = 9 + 1;
			break;
		case 3:
			map.createMap(3);
			player.setHP(player.maxHP);
	        player.setPositionToMap(5,12);
	        player.reset();
	        player.setGun(TankEntity.GunType.MINIGUN);
	        player.rocketReleased = false;
	        entities.add(player);
	        brick[0].setPositionToMap(4, 1);
	        brick[1].setPositionToMap(6, 1);
	        brick[2].setPositionToMap(4, 2);
	        brick[3].setPositionToMap(6, 2);
	        brick[4].setPositionToMap(1, 3);
	        brick[5].setPositionToMap(3, 3);
	        brick[6].setPositionToMap(4, 3);
	        brick[7].setPositionToMap(6, 3);
	        brick[8].setPositionToMap(7, 3);
	        brick[9].setPositionToMap(9, 3);
	        brick[10].setPositionToMap(1, 5);
	        brick[11].setPositionToMap(1, 6);
	        brick[12].setPositionToMap(1, 7);
	        brick[13].setPositionToMap(2, 5);
	        brick[14].setPositionToMap(2, 6);
	        brick[15].setPositionToMap(2, 7);
	        brick[16].setPositionToMap(4, 6);
	        brick[17].setPositionToMap(4, 7);
	        brick[18].setPositionToMap(5, 7);
	        brick[19].setPositionToMap(6, 6);
	        brick[20].setPositionToMap(6, 7);
	        brick[21].setPositionToMap(8, 5);
	        brick[22].setPositionToMap(8, 6);
	        brick[23].setPositionToMap(8, 7);
	        brick[24].setPositionToMap(9, 5);
	        brick[25].setPositionToMap(9, 6);
	        brick[26].setPositionToMap(9, 7);
	        for(int i = 0;i < 27;i++){
	        	brick[i].setHP(30);
	        	brick[i].reset();
	        	entities.add(brick[i]);
	        }
	        box[0].setPositionToMap(1, 1);
	        box[1].setPositionToMap(3, 4);
	        for(int i = 0;i < 2;i++){
	        	box[i].setHP(15);
	        	box[i].reset();
	        	entities.add(box[i]);
	        }
	        oilTank[0].setPositionToMap(4, 5);
	        oilTank[1].setPositionToMap(6, 5);
	        for(int i = 0;i < 2;i++){
	        	oilTank[i].setHP(15);
	        	oilTank[i].reset();
	        	entities.add(oilTank[i]);
	        }
	        enemyMinigun[0].setPositionToMap(2, 8);
	        enemyMinigun[1].setPositionToMap(8, 8);
	        enemyMinigun[2].setPositionToMap(5, 6);
	        enemyMinigun[3].setPositionToMap(5, 1);
	        enemyMinigun[4].setPositionToMap(5, 2);
	        enemyMinigun[5].setPositionToMap(5, 3);
	        enemyMinigun[6].setPositionToMap(5, 4);
			for (int i = 0; i < 7; i++) {
				enemyMinigun[i].setHP(70);
				enemyMinigun[i].setBodyAngle(39);
				enemyMinigun[i].setMinigunAttck(2);
				enemyMinigun[i].reset();
				entities.add(enemyMinigun[i]);
			}
			enemyShotgun[0].setPositionToMap(2, 4);
	        enemyShotgun[1].setPositionToMap(8, 4);
	        for (int i = 0; i < 2; i++) {
	        	enemyShotgun[i].setHP(70);
	        	enemyShotgun[i].setBodyAngle(39);
	        	enemyShotgun[i].setShotgunAttck(1);
				enemyShotgun[i].reset();
				entities.add(enemyShotgun[i]);
			}
	        turret[0].setPositionToMap(2, 2);
	        turret[1].setPositionToMap(8, 2);
			for (int i = 0; i < 2; i++) {
				turret[0].setHP(70);
				turret[0].reset();
				entities.add(turret[0]);
			}
	        numEnemy = 9 + 1;
			break;
		case 4:
			map.createMap(4);
			player.setHP(player.maxHP);
	        player.setPositionToMap(1,2);
	        player.reset();
	        player.setGun(TankEntity.GunType.MINIGUN);
	        player.rocketReleased = false;
	        entities.add(player);
	        brick[0].setPositionToMap(4, 1);
	        brick[1].setPositionToMap(4, 2);
	        brick[2].setPositionToMap(6, 2);
	        brick[3].setPositionToMap(6, 3);
	        brick[4].setPositionToMap(10, 4);
	        brick[5].setPositionToMap(11, 4);
	        brick[6].setPositionToMap(12, 4);
	        brick[7].setPositionToMap(9, 5);
	        brick[8].setPositionToMap(9, 6);
	        brick[9].setPositionToMap(7, 6);
	        brick[10].setPositionToMap(5, 5);
	        brick[11].setPositionToMap(5, 6);
	        brick[12].setPositionToMap(3, 6);
	        brick[13].setPositionToMap(1, 8);
	        brick[14].setPositionToMap(2, 8);
	        brick[15].setPositionToMap(3, 9);
	        brick[16].setPositionToMap(3, 10);
	        brick[17].setPositionToMap(4, 9);
	        brick[18].setPositionToMap(5, 9);
	        brick[19].setPositionToMap(5, 11);
	        brick[20].setPositionToMap(6, 11);
	        brick[21].setPositionToMap(7, 11);
	        brick[22].setPositionToMap(8, 11);
	        brick[23].setPositionToMap(8, 10);
	        for(int i = 0;i < 24;i++){
	        	brick[i].setHP(30);
	        	brick[i].reset();
	        	entities.add(brick[i]);
	        }
	        oilTank[0].setPositionToMap(8, 2);
	        oilTank[1].setPositionToMap(7, 7);
	        oilTank[2].setPositionToMap(1, 7);
	        oilTank[3].setPositionToMap(1, 11);
	        oilTank[4].setPositionToMap(6, 9);
	        for(int i = 0;i < 5;i++){
	        	oilTank[i].setHP(15);
	        	oilTank[i].reset();
	        	entities.add(oilTank[i]);
	        }
	        enemyMinigun[0].setPositionToMap(11, 2);
	        enemyMinigun[1].setPositionToMap(11, 6);
	        enemyMinigun[2].setPositionToMap(2, 5);
	        enemyMinigun[3].setPositionToMap(2, 6);
	        enemyMinigun[4].setPositionToMap(2, 7);
	        enemyMinigun[5].setPositionToMap(7, 10);
			for (int i = 0; i < 6; i++) {
				enemyMinigun[i].setHP(80);
				enemyMinigun[i].setBodyAngle(39);
				enemyMinigun[i].setMinigunAttck(2);
				enemyMinigun[i].reset();
				entities.add(enemyMinigun[i]);
			}
			enemyShotgun[0].setPositionToMap(6, 5);
	        enemyShotgun[1].setPositionToMap(7, 5);
	        enemyShotgun[2].setPositionToMap(8, 5);
	        enemyShotgun[3].setPositionToMap(4, 11);
	        enemyShotgun[4].setPositionToMap(9, 10);
	        enemyShotgun[5].setPositionToMap(10, 10);
	        for (int i = 0; i < 6; i++) {
	        	enemyShotgun[i].setHP(80);
	        	enemyShotgun[i].setBodyAngle(39);
	        	enemyShotgun[i].setShotgunAttck(2);
				enemyShotgun[i].reset();
				entities.add(enemyShotgun[i]);
			}
	        turret[0].setPositionToMap(10, 2);
	        turret[1].setPositionToMap(11, 10);
			for (int i = 0; i < 2; i++) {
				turret[0].setHP(80);
				turret[0].reset();
				entities.add(turret[0]);
			}
	        numEnemy = 12 + 1;
			break;
		case 5:
			map.createMap(5);
			player.setHP(player.maxHP);
	        player.setPositionToMap(2,2);
	        player.reset();
	        player.setGun(TankEntity.GunType.MINIGUN);
	        player.rocketReleased = false;
	        entities.add(player);
	        brick_2[0].setPositionToMap(6, 2);
	        brick_2[1].setPositionToMap(8, 2);
	        brick_2[2].setPositionToMap(10, 2);
	        brick_2[3].setPositionToMap(6, 4);
	        brick_2[4].setPositionToMap(12, 4);
	        brick_2[5].setPositionToMap(6, 6);
	        brick_2[6].setPositionToMap(8, 6);
	        brick_2[7].setPositionToMap(10, 6);
	        brick_2[8].setPositionToMap(12, 6);
	        brick_2[9].setPositionToMap(6, 11);
	        brick_2[10].setPositionToMap(7, 11);
	        brick_2[11].setPositionToMap(11, 11);
	        brick_2[12].setPositionToMap(12, 11);
	        brick_2[13].setPositionToMap(6, 12);
	        brick_2[14].setPositionToMap(12, 12);
	        brick_2[15].setPositionToMap(6, 13);
	        brick_2[16].setPositionToMap(12, 13);
	        brick_2[17].setPositionToMap(6, 15);
	        brick_2[18].setPositionToMap(12, 15);
	        brick_2[19].setPositionToMap(6, 16);
	        brick_2[20].setPositionToMap(12, 16);
	        brick_2[21].setPositionToMap(6, 17);
	        brick_2[22].setPositionToMap(7, 17);
	        brick_2[23].setPositionToMap(8, 17);
	        brick_2[24].setPositionToMap(11, 17);
	        brick_2[25].setPositionToMap(12, 17);
	        for(int i = 0;i < 26;i++){
	        	brick_2[i].setHP(70);
	        	brick_2[i].reset();
	        	entities.add(brick_2[i]);
	        }
	        bmWall[0].setPositionToMap(4, 2);
	        bmWall[1].setPositionToMap(4, 3);
	        bmWall[2].setPositionToMap(4, 4);
	        bmWall[3].setPositionToMap(8, 9);
	        bmWall[4].setPositionToMap(9, 9);
	        bmWall[5].setPositionToMap(10, 9);
	        for(int i = 0;i < 6;i++){
	        	bmWall[i].setHP(30);
	        	bmWall[i].reset();
	        	entities.add(bmWall[i]);
	        }
	        box[0].setPositionToMap(2, 7);
	        box[1].setPositionToMap(2, 9);
	        box[2].setPositionToMap(2, 11);
	        box[3].setPositionToMap(2, 13);
	        box[4].setPositionToMap(2, 15);
	        box[5].setPositionToMap(2, 17);
	        for(int i = 0;i < 6;i++){
	        	box[i].setHP(15);
	        	box[i].reset();
	        	entities.add(box[i]);
	        }
	        oilTank[0].setPositionToMap(8, 13);
	        oilTank[1].setPositionToMap(9, 14);
	        for(int i = 0;i < 2;i++){
	        	oilTank[i].setHP(15);
	        	oilTank[i].reset();
	        	entities.add(oilTank[i]);
	        }
	        enemyMinigun[0].setPositionToMap(7, 3);
	        enemyMinigun[1].setPositionToMap(9, 3);
	        enemyMinigun[2].setPositionToMap(11, 3);
	        enemyMinigun[3].setPositionToMap(7, 5);
	        enemyMinigun[4].setPositionToMap(9, 5);
	        enemyMinigun[5].setPositionToMap(11, 5);
	        enemyMinigun[6].setPositionToMap(7, 7);
	        enemyMinigun[7].setPositionToMap(9, 7);
	        enemyMinigun[8].setPositionToMap(11, 7);
			for (int i = 0; i < 9; i++) {
				enemyMinigun[i].setHP(90);
				enemyMinigun[i].setBodyAngle(39);
				enemyMinigun[i].setMinigunAttck(3);
				enemyMinigun[i].reset();
				entities.add(enemyMinigun[i]);
			}
			enemyShotgun[0].setPositionToMap(7, 12);
	        enemyShotgun[1].setPositionToMap(11, 12);
	        enemyShotgun[2].setPositionToMap(7, 15);
	        enemyShotgun[3].setPositionToMap(8, 15);
	        enemyShotgun[4].setPositionToMap(9, 15);
	        enemyShotgun[5].setPositionToMap(7, 13);
	        for (int i = 0; i < 6; i++) {
	        	enemyShotgun[i].setHP(90);
	        	enemyShotgun[i].setBodyAngle(39);
	        	enemyShotgun[i].setShotgunAttck(2);
				enemyShotgun[i].reset();
				entities.add(enemyShotgun[i]);
			}
	        turret[0].setPositionToMap(8, 4);
	        turret[1].setPositionToMap(10, 4);
	        turret[2].setPositionToMap(11, 16);
	        for(int i = 0;i < 3;i++){
	        	turret[i].setHP(90);
				turret[i].reset();
				entities.add(turret[i]);
	        }
	        numEnemy = 15 + 3;
			break;
		case 6:
			map.createMap(6);
			player.setHP(player.maxHP);
	        player.setPositionToMap(1,15);
	        player.reset();
	        player.setGun(TankEntity.GunType.MINIGUN);
	        player.rocketReleased = false;
	        entities.add(player);
	        brick[0].setPositionToMap(9, 3);
	        brick[1].setPositionToMap(10, 3);
	        brick[2].setPositionToMap(11, 3);			
	        brick[3].setPositionToMap(8, 13);
	        brick[4].setPositionToMap(8, 14);
	        brick[5].setPositionToMap(8, 15);
	        for(int i = 0;i < 6;i++){
	        	brick[i].setHP(30);
	        	brick[i].reset();
	        	entities.add(brick[i]);
	        }
	        bmWall[0].setPositionToMap(8, 5);
	        bmWall[1].setPositionToMap(8, 6);
	        bmWall[2].setPositionToMap(8, 7);
	        bmWall[3].setPositionToMap(3, 12);
	        bmWall[4].setPositionToMap(4, 12);
	        bmWall[5].setPositionToMap(5, 12);
			
	        for(int i = 0;i < 6;i++){
	        	bmWall[i].setHP(30);
	        	bmWall[i].reset();
	        	entities.add(bmWall[i]);
	        }
	        oilTank[0].setPositionToMap(10, 5);
	        oilTank[1].setPositionToMap(10, 7);
	        oilTank[2].setPositionToMap(10, 9);
	        oilTank[3].setPositionToMap(10, 11);			
	        oilTank[4].setPositionToMap(2, 2);
	        oilTank[5].setPositionToMap(4, 2);
	        oilTank[6].setPositionToMap(6, 2);
			for(int i = 0;i < 7;i++){
	        	oilTank[i].setHP(15);
	        	oilTank[i].reset();
	        	entities.add(oilTank[i]);
	        }
	        for (int i = 0; i < 6; i++) {
				enemyMinigun[i].setHP(110);
				enemyMinigun[i].setPositionToMap(i+1, 5);
				enemyMinigun[i].setMinigunAttck(3);
				enemyMinigun[i].setBodyAngle(39);
				enemyMinigun[i].reset();
				entities.add(enemyMinigun[i]);
			}
	        enemyShotgunTurret[0].setPositionToMap(6, 7);
	        enemyShotgunTurret[1].setPositionToMap(2, 10);
	        for(int i = 0;i < 2;i++){
	        	enemyShotgunTurret[i].setHP(210);
	        	enemyShotgunTurret[i].setShotgunAttck(3);
				enemyShotgunTurret[i].reset();
				entities.add(enemyShotgunTurret[i]);
	        }			
	        turret[0].setPositionToMap(6, 4);			
	        turret[1].setPositionToMap(2, 4);
	        turret[2].setPositionToMap(2, 8);
	        for(int i = 0;i < 3;i++){
	        	turret[i].setHP(110);
				turret[i].reset();
				entities.add(turret[i]);
	        }
	        box[0].setPositionToMap(9, 1);
	        box[1].setPositionToMap(11, 1);
			box[2].setPositionToMap(10, 7);
			for(int i = 0;i < 3;i++){
			box[i].setHP(15);
			box[i].reset();
			entities.add(box[1]);
			}
	        numEnemy = 11;
	        break;
		case 7:
			map.createMap(7);
			player.setHP(player.maxHP);
	        player.setPositionToMap(9,12);
	        player.reset();
	        player.setGun(TankEntity.GunType.MINIGUN);
	        player.rocketReleased = false;
	        entities.add(player);
	        brick[0].setPositionToMap(7, 2);
	        brick[1].setPositionToMap(8, 2);
	        brick[2].setPositionToMap(14, 3);
	        brick[3].setPositionToMap(16, 9);
	        brick[4].setPositionToMap(3, 14);
	        brick[5].setPositionToMap(4, 14);
	        brick[6].setPositionToMap(7, 15);			
	        brick[7].setPositionToMap(4, 15);
	        brick[8].setPositionToMap(11, 15);
	        brick[9].setPositionToMap(14, 15);
	        brick[10].setPositionToMap(15, 15);			
	        brick[11].setPositionToMap(7, 16);
	        brick[12].setPositionToMap(11, 16);
	        brick[13].setPositionToMap(14, 16);
	        brick[14].setPositionToMap(7, 17);
	        brick[15].setPositionToMap(11, 17);			
	        brick[16].setPositionToMap(7, 18);
	        brick[17].setPositionToMap(11, 18);
	        for(int i = 0;i < 18;i++){
	        	brick[i].setHP(30);
	        	brick[i].reset();
	        	entities.add(brick[i]);
	        }
			brick_2[0].setPositionToMap(9, 16);
			brick_2[0].setHP(25);
			brick_2[0].reset();
			entities.add(brick_2[0]);
			
			bmWall[0].setPositionToMap(8, 14);
	        bmWall[1].setPositionToMap(9, 14);
	        bmWall[2].setPositionToMap(10, 14);
	        for(int i = 0;i < 3;i++){
	        	bmWall[i].setHP(30);
	        	bmWall[i].reset();
	        	entities.add(bmWall[i]);
	        }
	        oilTank[0].setPositionToMap(11, 4);
	        oilTank[1].setPositionToMap(4, 7);
			oilTank[2].setPositionToMap(17, 7);
	        oilTank[3].setPositionToMap(1, 12);
	        oilTank[4].setPositionToMap(16, 14);
	        for(int i = 0;i < 5;i++){
	        	oilTank[i].setHP(15);
	        	oilTank[i].reset();
	        	entities.add(oilTank[i]);
	        }
	        for (int i = 0; i < 5; i++) {
				enemyMinigun[i].setHP(130);
				enemyMinigun[i].setPositionToMap(6, i+1);
				enemyMinigun[i].setMinigunAttck(3);
				enemyMinigun[i].setBodyAngle(39);
				enemyMinigun[i].reset();
				entities.add(enemyMinigun[i]);
			}
			enemyShotgunTurret[0].setPositionToMap(3, 5);
	        enemyShotgunTurret[1].setPositionToMap(15, 7);
	        for(int i = 0;i < 2;i++){
	        	enemyShotgunTurret[i].setHP(230);
	        	enemyShotgunTurret[i].setShotgunAttck(3);
				enemyShotgunTurret[i].reset();
				entities.add(enemyShotgunTurret[i]);
	        }
	        enemyCannonTurret[0].setPositionToMap(17, 11);	        
			enemyCannonTurret[0].setHP(230);
			enemyCannonTurret[0].setCannonAttck(30);
			enemyCannonTurret[0].reset();
			entities.add(enemyCannonTurret[0]);
	        	
			turret[0].setPositionToMap(10, 2);
			turret[1].setPositionToMap(2, 10);
			turret[2].setPositionToMap(3, 13);
			turret[3].setPositionToMap(15, 14);
			for (int i = 0; i < 4; i++) {
	        turret[i].setHP(130);			
			turret[i].reset();
			entities.add(turret[i]);
			}
	        box[0].setPositionToMap(14, 8);
			box[1].setPositionToMap(6, 17);
			for(int i = 0;i < 2;i++){
			box[i].setHP(15);
			box[i].reset();
			entities.add(box[1]);
			}
	        numEnemy = 5 + 7;
			break;
		case 8:
			map.createMap(8);
			player.setHP(player.maxHP);
	        player.setPositionToMap(1,13);
	        player.reset();
	        player.setGun(TankEntity.GunType.MINIGUN);
	        player.rocketReleased = false;
	        entities.add(player);
			
	        brick[0].setPositionToMap(14, 2);
	        brick[1].setPositionToMap(15, 3);
	        brick[2].setPositionToMap(16, 3);
	        brick[3].setPositionToMap(17, 3);
	        brick[4].setPositionToMap(16, 6);
	        brick[5].setPositionToMap(17, 9);
	        brick[6].setPositionToMap(16, 9);
			brick[7].setPositionToMap(16, 10);
	        brick[8].setPositionToMap(17, 11);
	        brick[9].setPositionToMap(5, 11);
	        brick[10].setPositionToMap(6, 11);
	        brick[11].setPositionToMap(5, 12);
	        brick[12].setPositionToMap(6, 12);
	        brick[13].setPositionToMap(5, 13);
			brick[14].setPositionToMap(6, 13);
	        for(int i = 0;i < 15;i++){
	        	brick[i].setHP(30);
	        	brick[i].reset();
	        	entities.add(brick[i]);
	        }
			bmWall[0].setPositionToMap(7, 3);
	        bmWall[1].setPositionToMap(7, 4);
	        bmWall[2].setPositionToMap(7, 5);
			bmWall[3].setPositionToMap(3, 7);
	        bmWall[4].setPositionToMap(4, 7);
	        bmWall[5].setPositionToMap(5, 7);
			bmWall[6].setPositionToMap(9, 7);
	        bmWall[7].setPositionToMap(10, 7);
	        bmWall[8].setPositionToMap(11, 7);
			bmWall[9].setPositionToMap(15, 7);
	        bmWall[10].setPositionToMap(16, 7);
	        bmWall[11].setPositionToMap(17, 7);
			bmWall[12].setPositionToMap(13, 10);
	        bmWall[13].setPositionToMap(13, 11);
	        bmWall[14].setPositionToMap(13, 12);
	        for(int i = 0;i < 15;i++){
	        	bmWall[i].setHP(30);
	        	bmWall[i].reset();
	        	entities.add(bmWall[i]);
	        }
	        oilTank[0].setPositionToMap(4, 4);			
			oilTank[1].setPositionToMap(10, 2);
	        oilTank[2].setPositionToMap(8, 6);
			oilTank[3].setPositionToMap(14, 8);
	        oilTank[4].setPositionToMap(12, 12);
			
	        for(int i = 0;i < 5;i++){
	        	oilTank[i].setHP(15);
	        	oilTank[i].reset();
	        	entities.add(oilTank[i]);
	        }
			enemyMinigun[0].setPositionToMap(2, 5);
			enemyMinigun[1].setPositionToMap(4, 5);
			enemyMinigun[2].setPositionToMap(4, 5);
			enemyMinigun[3].setPositionToMap(5, 5);
			enemyMinigun[4].setPositionToMap(9, 5);
			enemyMinigun[5].setPositionToMap(10, 5);
			enemyMinigun[6].setPositionToMap(11, 5);
			enemyMinigun[7].setPositionToMap(12, 5);			
			enemyMinigun[8].setPositionToMap(14, 5);
			enemyMinigun[9].setPositionToMap(15, 5);
			enemyMinigun[10].setPositionToMap(16, 5);
			enemyMinigun[11].setPositionToMap(17, 5);			
			enemyMinigun[12].setPositionToMap(18, 5);
			enemyMinigun[13].setPositionToMap(8, 13);
			enemyMinigun[14].setPositionToMap(9, 13);
			enemyMinigun[15].setPositionToMap(14, 10);			
			enemyMinigun[16].setPositionToMap(11, 13);
			enemyMinigun[17].setPositionToMap(14, 13);
			enemyMinigun[18].setPositionToMap(15, 13);
			enemyMinigun[19].setPositionToMap(16, 13);
			enemyMinigun[20].setPositionToMap(17, 13);
	        for (int i = 0; i < 21; i++) {
				enemyMinigun[i].setHP(150);
				enemyMinigun[i].setBodyAngle(39);
				enemyMinigun[i].setMinigunAttck(4);
				enemyMinigun[i].reset();
				entities.add(enemyMinigun[i]);
			}
			enemyCannonTurret[0].setPositionToMap(17, 4);
	        enemyCannonTurret[1].setPositionToMap(16, 12);
	        for(int i = 0;i < 2;i++){
	        	enemyCannonTurret[i].setHP(250);
				enemyCannonTurret[i].setCannonAttck(50);
				enemyCannonTurret[i].reset();
				entities.add(enemyCannonTurret[i]);
	        }
			enemyShotgunTurret[0].setPositionToMap(8, 8);
			enemyShotgunTurret[0].setShotgunAttck(4);
			enemyShotgunTurret[0].setHP(250);
			enemyShotgunTurret[0].reset();
			entities.add(enemyShotgunTurret[0]);
			
			turret[0].setPositionToMap(2, 2);
			turret[1].setPositionToMap(11, 2);
			turret[2].setPositionToMap(15, 2);
			turret[3].setPositionToMap(9, 12);
			for (int i = 0; i < 4; i++) {
	        turret[i].setHP(150);
			turret[i].reset();
			entities.add(turret[i]);
			}
			box[0].setPositionToMap(12, 9);
			box[1].setPositionToMap(12, 10);
			box[2].setPositionToMap(17, 10);
			for(int i = 0;i < 3;i++){
			box[i].setHP(15);
			box[i].reset();
			entities.add(box[i]);
			}
	        numEnemy = 21 +2+1+ 4;
			break;
		case 9:
			map.createMap(9);
			player.setHP(player.maxHP);
	        player.setPositionToMap(7,1);
	        player.reset();
	        player.setGun(TankEntity.GunType.MINIGUN);
	        player.rocketReleased = false;
	        entities.add(player);
	        brick[0].setPositionToMap(4, 9);
	        brick[1].setPositionToMap(5, 9);
	        brick[2].setPositionToMap(10, 9);
	        brick[3].setPositionToMap(4, 10);
	        brick[4].setPositionToMap(5, 10);
	        brick[5].setPositionToMap(9, 10);
	        brick[6].setPositionToMap(10, 10);
	        brick[7].setPositionToMap(4, 11);
	        brick[8].setPositionToMap(9, 11);
	        brick[9].setPositionToMap(10, 11);
	        brick[10].setPositionToMap(5, 16);
	        brick[11].setPositionToMap(6, 16);
	        brick[12].setPositionToMap(8, 16);
	        brick[13].setPositionToMap(9, 16);
	        brick[14].setPositionToMap(6, 17);
	        brick[15].setPositionToMap(4, 18);
	        brick[16].setPositionToMap(6, 18);
	        brick[17].setPositionToMap(10, 18);
	        brick[18].setPositionToMap(4, 19);
	        brick[19].setPositionToMap(8, 19);
	        brick[20].setPositionToMap(9, 19);
	        brick[21].setPositionToMap(10, 19);
	        brick[22].setPositionToMap(4, 20);
	        brick[23].setPositionToMap(5, 20);
	        brick[24].setPositionToMap(6, 20);
	        brick[25].setPositionToMap(10, 20);
	        brick[26].setPositionToMap(8, 22);
	        for(int i = 0;i < 27;i++){
	        	brick[i].setHP(30);
	        	brick[i].reset();
	        	entities.add(brick[i]);
	        }
			bmWall[0].setPositionToMap(6, 5);
	        bmWall[1].setPositionToMap(7, 5);
	        bmWall[2].setPositionToMap(8, 5);
			bmWall[3].setPositionToMap(6, 14);
	        bmWall[4].setPositionToMap(7, 14);
	        bmWall[5].setPositionToMap(8, 14);
	        for(int i = 0;i < 6;i++){
	        	bmWall[i].setHP(30);
	        	bmWall[i].reset();
	        	entities.add(bmWall[i]);
	        }
	        oilTank[0].setPositionToMap(3, 12);
	        oilTank[1].setPositionToMap(10, 12);			
	        oilTank[2].setPositionToMap(4, 16);
	        oilTank[3].setPositionToMap(10, 16);
	        oilTank[4].setPositionToMap(9, 22);
			
	        for(int i = 0;i < 5;i++){
	        	oilTank[i].setHP(15);
	        	oilTank[i].reset();
	        	entities.add(oilTank[i]);
	        }
			enemyMinigun[0].setPositionToMap(2, 7);
			enemyMinigun[1].setPositionToMap(3, 7);
			enemyMinigun[2].setPositionToMap(4, 7);
			enemyMinigun[3].setPositionToMap(10, 7);
			enemyMinigun[4].setPositionToMap(11, 7);
			enemyMinigun[5].setPositionToMap(12, 7);
			enemyMinigun[6].setPositionToMap(3, 22);
			enemyMinigun[7].setPositionToMap(4, 22);
			enemyMinigun[8].setPositionToMap(5, 22);
			enemyMinigun[9].setPositionToMap(6, 23);
			enemyMinigun[10].setPositionToMap(8, 23);
			enemyMinigun[11].setPositionToMap(9, 23);			
			enemyMinigun[12].setPositionToMap(10, 23);
	        for (int i = 0; i < 13; i++) {
				enemyMinigun[i].setHP(170);
				enemyMinigun[i].setBodyAngle(39);
				enemyMinigun[i].setMinigunAttck(4);
				enemyMinigun[i].reset();
				entities.add(enemyMinigun[i]);
			}
			
			turret[0].setPositionToMap(2, 5);
			turret[1].setPositionToMap(12, 5);
			turret[2].setPositionToMap(7, 11);
			turret[3].setPositionToMap(8, 21);
			for (int i = 0; i < 4; i++) {
	        turret[i].setHP(150);
			turret[i].reset();
			entities.add(turret[i]);
			}
			box[0].setPositionToMap(9, 7);
			box[1].setPositionToMap(13, 9);
			box[2].setPositionToMap(4, 21);
			box[3].setPositionToMap(5, 21);
			for(int i = 0;i < 4;i++){
			box[i].setHP(15);
			box[i].reset();
			entities.add(box[i]);
			}
			enemyCannonTurret[0].setPositionToMap(2, 8);
	        enemyCannonTurret[1].setPositionToMap(12, 8);
			enemyCannonTurret[2].setPositionToMap(6, 22);
	        for(int i = 0;i < 3;i++){
	        	enemyCannonTurret[i].setHP(250);
	        	enemyCannonTurret[i].setCannonAttck(50);
				enemyCannonTurret[i].reset();
				entities.add(enemyCannonTurret[i]);
	        }
	        numEnemy = 20;
			break;
		case 10:
			map.createMap(10);
			player.setHP(player.maxHP);
	        player.setPositionToMap(5,5);
	        player.reset();
	        player.setGun(TankEntity.GunType.MINIGUN);
	        player.rocketReleased = false;
	        entities.add(player);
	        brick[0].setPositionToMap(1, 1);
	        brick[1].setPositionToMap(2, 1);
	        brick[2].setPositionToMap(3, 1);
	        brick[3].setPositionToMap(4, 1);
	        brick[4].setPositionToMap(5, 1);
	        brick[5].setPositionToMap(6, 1);
	        brick[6].setPositionToMap(7, 1);
	        brick[7].setPositionToMap(8, 1);
	        brick[8].setPositionToMap(9, 1);
	        brick[9].setPositionToMap(10, 1);
	        brick[10].setPositionToMap(11, 1);
	        brick[11].setPositionToMap(12, 1);
	        brick[12].setPositionToMap(13, 1);
	        brick[13].setPositionToMap(14, 1);
	        brick[14].setPositionToMap(15, 1);		
			
	        brick[15].setPositionToMap(1, 2);
	        brick[16].setPositionToMap(3, 2);
	        brick[17].setPositionToMap(5, 2);
	        brick[18].setPositionToMap(6, 2);
	        brick[19].setPositionToMap(7, 2);
	        brick[20].setPositionToMap(8, 2);
	        brick[21].setPositionToMap(9, 2);
	        brick[22].setPositionToMap(11, 2);
	        brick[23].setPositionToMap(12, 2);
	        brick[24].setPositionToMap(13, 2);
	        brick[25].setPositionToMap(14, 2);
	        brick[26].setPositionToMap(15, 2);	
	
	        brick[27].setPositionToMap(1, 3);
	        brick[28].setPositionToMap(2, 3);
	        brick[29].setPositionToMap(4, 3);
	        brick[30].setPositionToMap(5, 3);
	        brick[31].setPositionToMap(6, 3);
	        brick[33].setPositionToMap(8, 3);
	        brick[34].setPositionToMap(9, 3);
	        brick[35].setPositionToMap(10, 3);
	        brick[36].setPositionToMap(11, 3);
	        brick[37].setPositionToMap(12, 3);
	        brick[38].setPositionToMap(13, 3);
	        brick[39].setPositionToMap(14, 3);
			
	        brick[40].setPositionToMap(4, 4);
	        brick[41].setPositionToMap(5, 4);
	        brick[42].setPositionToMap(6, 4);
	        brick[43].setPositionToMap(7, 4);
	        brick[44].setPositionToMap(8, 4);
	        brick[45].setPositionToMap(9, 4);
	        brick[46].setPositionToMap(10, 4);
	        brick[47].setPositionToMap(11, 4);
	        brick[48].setPositionToMap(12, 4);
	        brick[49].setPositionToMap(13, 4);
	        brick[50].setPositionToMap(14, 4);
	        brick[51].setPositionToMap(15, 4);
			
	        brick[52].setPositionToMap(4, 5);
	        brick[53].setPositionToMap(6, 5);
	        brick[54].setPositionToMap(7, 5);
	        brick[55].setPositionToMap(8, 5);
	        brick[56].setPositionToMap(9, 5);
	        brick[57].setPositionToMap(10, 5);
	        brick[58].setPositionToMap(11, 5);
	        brick[59].setPositionToMap(12, 5);
	        brick[60].setPositionToMap(13, 5);
	        brick[61].setPositionToMap(14, 5);
	        brick[62].setPositionToMap(15, 5);
			
	        brick[63].setPositionToMap(4, 6);
	        brick[64].setPositionToMap(5, 6);
	        brick[65].setPositionToMap(6, 6);
	        brick[66].setPositionToMap(10, 6);
	        brick[67].setPositionToMap(11, 6);
	        brick[68].setPositionToMap(12, 6);
			
	        brick[69].setPositionToMap(1, 7);
	        brick[70].setPositionToMap(2, 7);
	        brick[71].setPositionToMap(3, 7);
	        brick[72].setPositionToMap(4, 7);
	        brick[73].setPositionToMap(5, 7);
	        brick[74].setPositionToMap(6, 7);
	        brick[75].setPositionToMap(10, 7);
	        brick[76].setPositionToMap(11, 7);
	        brick[77].setPositionToMap(12, 7);
			
	        brick[78].setPositionToMap(1, 8);
	        brick[79].setPositionToMap(3, 8);
	        brick[80].setPositionToMap(4, 8);
	        brick[81].setPositionToMap(5, 8);
	        brick[82].setPositionToMap(6, 8);
	        brick[83].setPositionToMap(10, 8);
	        brick[84].setPositionToMap(11, 8);
	        brick[85].setPositionToMap(12, 8);
			
	        brick[86].setPositionToMap(1, 9);
	        brick[87].setPositionToMap(2, 9);
	        brick[88].setPositionToMap(3, 9);
	        brick[89].setPositionToMap(4, 9);
	        brick[90].setPositionToMap(5, 9);
	        brick[91].setPositionToMap(6, 9);
	        brick[92].setPositionToMap(7, 9);
	        brick[93].setPositionToMap(8, 9);
	        brick[94].setPositionToMap(9, 9);
	        brick[95].setPositionToMap(13, 9);
	        brick[96].setPositionToMap(14, 9);
	        brick[97].setPositionToMap(15, 9);
			
	        brick[98].setPositionToMap(4, 10);
	        brick[99].setPositionToMap(5, 10);
	        brick[100].setPositionToMap(7, 10);
	        brick[101].setPositionToMap(8, 10);
	        brick[102].setPositionToMap(9, 10);
	        brick[103].setPositionToMap(13, 10);
	        brick[104].setPositionToMap(14, 10);
	        brick[105].setPositionToMap(15, 10);	
			
	        brick[106].setPositionToMap(4, 11);
	        brick[107].setPositionToMap(5, 11);
	        brick[108].setPositionToMap(6, 11);
	        brick[109].setPositionToMap(7, 11);
	        brick[110].setPositionToMap(8, 11);
	        brick[111].setPositionToMap(9, 11);
	        brick[112].setPositionToMap(13, 11);
			
	        brick[113].setPositionToMap(4, 12);
	        brick[114].setPositionToMap(5, 12);
	        brick[115].setPositionToMap(6, 12);
	        brick[116].setPositionToMap(7, 12);
	        brick[117].setPositionToMap(8, 12);
	        brick[118].setPositionToMap(9, 12);
	        brick[119].setPositionToMap(10, 12);
	        brick[121].setPositionToMap(12, 12);
	        brick[122].setPositionToMap(13, 12);
	        brick[123].setPositionToMap(14, 12);
	        brick[124].setPositionToMap(15, 12);
			
	        brick[125].setPositionToMap(1, 13);
	        brick[126].setPositionToMap(2, 13);
	        brick[127].setPositionToMap(3, 13);
	        brick[128].setPositionToMap(4, 13);
	        brick[129].setPositionToMap(6, 13);
	        brick[130].setPositionToMap(7, 13);
	        brick[131].setPositionToMap(8, 13);
	        brick[132].setPositionToMap(9, 13);
	        brick[133].setPositionToMap(10, 13);
	        brick[134].setPositionToMap(11, 13);
	        brick[135].setPositionToMap(12, 13);
	        brick[136].setPositionToMap(13, 13);
	        brick[137].setPositionToMap(14, 13);
	        brick[138].setPositionToMap(15, 13);
			
	        brick[139].setPositionToMap(1, 14);
	        brick[140].setPositionToMap(2, 14);
	        brick[141].setPositionToMap(3, 14);
	        brick[142].setPositionToMap(4, 14);
	        brick[143].setPositionToMap(5, 14);
	        brick[144].setPositionToMap(6, 14);
	        brick[145].setPositionToMap(7, 14);
	        brick[146].setPositionToMap(8, 14);
	        brick[147].setPositionToMap(9, 14);
	        brick[148].setPositionToMap(11, 14);
	        brick[149].setPositionToMap(12, 14);
	        brick[150].setPositionToMap(14, 14);
	        brick[151].setPositionToMap(15, 14);
			
	        brick[152].setPositionToMap(1, 15);
	        brick[153].setPositionToMap(2, 15);
	        brick[154].setPositionToMap(3, 15);
	        brick[155].setPositionToMap(4, 15);
	        brick[156].setPositionToMap(5, 15);
	        brick[157].setPositionToMap(6, 15);
	        brick[158].setPositionToMap(7, 15);
	        brick[159].setPositionToMap(11, 15);
	        brick[160].setPositionToMap(12, 15);
	        brick[161].setPositionToMap(13, 15);
	        brick[162].setPositionToMap(14, 15);
	        brick[163].setPositionToMap(15, 15);
			
	        brick[167].setPositionToMap(1, 16);
	        brick[168].setPositionToMap(7, 16);
	        brick[169].setPositionToMap(13, 16);
	        brick[170].setPositionToMap(14, 16);
	        brick[171].setPositionToMap(15, 16);	
			
	        brick[172].setPositionToMap(1, 17);
	        brick[173].setPositionToMap(2, 17);
	        brick[174].setPositionToMap(6, 17);
	        brick[175].setPositionToMap(7, 17);
	        brick[176].setPositionToMap(8, 17);
	        brick[177].setPositionToMap(9, 17);
	        brick[178].setPositionToMap(13, 17);
	        brick[179].setPositionToMap(14, 17);
	        brick[180].setPositionToMap(15, 17);
			
	        brick[181].setPositionToMap(1, 18);
	        brick[182].setPositionToMap(2, 18);
	        brick[183].setPositionToMap(6, 18);
	        brick[184].setPositionToMap(7, 18);
	        brick[185].setPositionToMap(8, 18);
	        brick[186].setPositionToMap(9, 18);
	        brick[187].setPositionToMap(13, 18);
	        brick[188].setPositionToMap(14, 18);
	        brick[189].setPositionToMap(15, 18);
			
	        for(int i = 0;i < 190;i++){
	        	if(brick[i].x != 0) {
	        		brick[i].setHP(30);
		        	brick[i].reset();
		        	entities.add(brick[i]);
	        	}
	        }
	        oilTank[0].setPositionToMap(8, 7);
	        oilTank[1].setPositionToMap(14, 7);
	        oilTank[2].setPositionToMap(2, 11);
	        oilTank[3].setPositionToMap(9, 16);
	        for(int i = 0;i < 4;i++){
	        	oilTank[i].setHP(15);
	        	oilTank[i].reset();
	        	entities.add(oilTank[i]);
	        }
	        box[0].setPositionToMap(2, 2);
			box[1].setPositionToMap(10, 2);
			box[2].setPositionToMap(7, 3);
			box[3].setPositionToMap(2, 8);			
			box[4].setPositionToMap(6, 10);
			box[5].setPositionToMap(13, 14);
			box[6].setPositionToMap(2, 16);
			box[7].setPositionToMap(6, 16);
			box[8].setPositionToMap(11, 18);
			for(int i = 0;i < 9;i++){
				box[i].setHP(15);
				box[i].reset();
				entities.add(box[i]);
			}
			enemyMinigun[0].setPositionToMap(2, 4);
			enemyMinigun[1].setPositionToMap(1, 5);
			enemyMinigun[2].setPositionToMap(3, 5);
			enemyMinigun[3].setPositionToMap(2, 6);
			enemyMinigun[4].setPositionToMap(11, 9);
			enemyMinigun[5].setPositionToMap(10, 10);
			enemyMinigun[6].setPositionToMap(12, 10);
			enemyMinigun[7].setPositionToMap(11, 12);
			enemyMinigun[8].setPositionToMap(4, 16);
			enemyMinigun[9].setPositionToMap(11, 16);
			enemyMinigun[10].setPositionToMap(3, 17);
			enemyMinigun[11].setPositionToMap(5, 17);
			enemyMinigun[12].setPositionToMap(10, 17);
			enemyMinigun[13].setPositionToMap(12, 17);
			enemyMinigun[14].setPositionToMap(4, 18);
			enemyMinigun[15].setPositionToMap(12, 18);
	        for (int i = 0; i < 16; i++) {
				enemyMinigun[i].setHP(170);
				enemyMinigun[i].setBodyAngle(39);
				enemyMinigun[i].setMinigunAttck(4);
				enemyMinigun[i].reset();
				entities.add(enemyMinigun[i]);
			}
				enemyCannonTurret[0].setPositionToMap(4, 2);
				enemyCannonTurret[1].setPositionToMap(5, 13);
			 for(int i = 0;i < 2;i++){
	        	enemyCannonTurret[i].setHP(270);
	        	enemyCannonTurret[i].setCannonAttck(50);
				enemyCannonTurret[i].reset();
				entities.add(enemyCannonTurret[i]);
	        }
			/*enemyRocketTurret[0].setPositionToMap(5, 13);	
	        enemyRocketTurret[0].setHP(50);
			enemyRocketTurret[0].reset();
			entities.add(enemyRocketTurret[0]);*/
			turret[0].setPositionToMap(2, 5);
			turret[1].setPositionToMap(11, 10);
			turret[2].setPositionToMap(4, 17);
			turret[3].setPositionToMap(11, 17);
			for (int i = 0; i < 4; i++) {
				turret[i].setHP(170);
				turret[i].reset();
				entities.add(turret[i]);
			}
	        numEnemy = 22;
			break;
		}
    }
    
    public void dropGold(int x, int y) {
    	int amount = new Random().nextInt(10) + 5;	// random 5 to 15
		int i;
		for(i = goldIndex;i < amount + goldIndex;i++){
			if(i >= gold.length){
				amount += goldIndex - i;
				goldIndex = 0;
				i = 0;
			}
			gold[i].setXY(x, y);
			gold[i].setDX(new Random().nextInt(7) - 3);	// random -3 to 3
			gold[i].setDY(new Random().nextInt(7) - 3);
			addEntity(gold[i]);
		}
		goldIndex = i;
    }
    
    public static long getTime() {
		return (Sys.getTime() * 1000) / timerTicksPerSecond;
	}

	private ByteBuffer loadIcon(String key) throws IOException {
        InputStream is = new FileInputStream(new File("res/"+key));
        try {
            PNGDecoder decoder = new PNGDecoder(is);
            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
            decoder.decode(bb, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
            bb.flip();
            return bb;
        } finally {
            is.close();
        }
    }
	
	public static Texture loadTexture(String key){
		/*try {
			PNGDecoder decoder = new PNGDecoder(new FileInputStream("res/"+key));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		try {
			if(key.indexOf(".jpg") != -1){
				return TextureLoader.getTexture("JPG", new FileInputStream(new File("res/"+key)));
			} else
				return TextureLoader.getTexture("PNG", new FileInputStream(new File("res/"+key)));
    	} catch (FileNotFoundException e) {
            e.printStackTrace();
    	} catch (IOException e) {
            e.printStackTrace();
    	}
    	return null;
	}
	
	public void draw(Texture tex,int x,int y,int width,int height) {
		glPushMatrix();
		tex.bind();
        glBegin(GL_QUADS);
        	glTexCoord2f(0,0);
        	glVertex2f(x-width/2 ,y-height/2);//upper left
        	glTexCoord2f(tex.getWidth(),0);
        	glVertex2f(x+width/2 ,y-height/2);//upper right
        	glTexCoord2f(tex.getWidth(),tex.getHeight());
        	glVertex2f(x+width/2 ,y+height/2);//bottom right
        	glTexCoord2f(0,tex.getHeight());
        	glVertex2f(x-width/2 ,y+height/2);//bottom left
        glEnd();
        glPopMatrix();
	}
	
	/**
	 * 
	 * @param money
	 * @param x
	 * @param y
	 * @param size
	 * @param type 1 = normal, 2 = draw num brown
	 */
	private void drawFont(String money, int x, int y, float size, int type){
		money += "$";
		float left = 0,right = 0;
		int index;
		if(not_enough_gold){
			number_no_gold.bind();
			not_enough_gold = false;
		} else if(type == 2)
			number_brown.bind();
		else
			number.bind();
		glPushMatrix();
        glTranslatef(0, 0, 0);
        glBegin(GL_QUADS);
        for (int i = money.length()-1; i >= 0; i--) {
        	if(i == money.length()-1)
        		index = 10;
        	else
        		index = (int) money.charAt(i)-48;
            switch(index){
            case 0:
            	left = 0;
            	right = 21/256f;
            	break;
            case 1:
            	left = 21/256f;
            	right = 34/256f;
            	break;
            case 2:
            	left = 34/256f;
            	right = 54/256f;
            	break;
            case 3:
            	left = 54/256f;
            	right = 73/256f;
            	break;
            case 4:
            	left = 73/256f;
            	right = 95/256f;
            	break;
            case 5:
            	left = 95/256f;
            	right = 114/256f;
            	break;
            case 6:
            	left = 114/256f;
            	right = 134/256f;
            	break;
            case 7:
            	left = 134/256f;
            	right = 153/256f;
            	break;
            case 8:
            	left = 153/256f;
            	right = 173/256f;
            	break;
            case 9:
            	left = 173/256f;
            	right = 194/256f;
            	break;
            case 10:
            	left = 194/256f;
            	right = 211/256f;
            	break;
            }            
            glTexCoord2f(right, 0);
            glVertex2f(x, y);
            
            glTexCoord2f(left, 0);
            glVertex2f(x - (right-left)*256*size, y);
            
            glTexCoord2f(left, 0 + number.getHeight());
            glVertex2f(x - (right-left)*256*size, y + number.getImageHeight()*size);
            
            glTexCoord2f(right, 0 + number.getHeight());
            glVertex2f(x, y + number.getImageHeight()*size);
            x -= (right-left)*256*size;
        }
        glEnd();
        glPopMatrix();
	}

	private float getBitmapFontWidth(String str, float size){
		str += "$";
		float left = 0,right = 0;
		float width = 0;
		int index;
        for (int i = str.length()-1; i >= 0; i--) {
        	if(i == str.length()-1)
        		index = 10;
        	else
        		index = (int) str.charAt(i)-48;
            switch(index){
            case 0:
            	left = 0;
            	right = 21/256f;
            	break;
            case 1:
            	left = 21/256f;
            	right = 34/256f;
            	break;
            case 2:
            	left = 34/256f;
            	right = 54/256f;
            	break;
            case 3:
            	left = 54/256f;
            	right = 73/256f;
            	break;
            case 4:
            	left = 73/256f;
            	right = 95/256f;
            	break;
            case 5:
            	left = 95/256f;
            	right = 114/256f;
            	break;
            case 6:
            	left = 114/256f;
            	right = 134/256f;
            	break;
            case 7:
            	left = 134/256f;
            	right = 153/256f;
            	break;
            case 8:
            	left = 153/256f;
            	right = 173/256f;
            	break;
            case 9:
            	left = 173/256f;
            	right = 194/256f;
            	break;
            case 10:
            	left = 194/256f;
            	right = 211/256f;
            	break;
            }
            width += (right-left)*256*size;
        }
		return width;
	}
	
	private void drawPriceLabel(Game.CurrentButton btn, String price, int x, int y){
		float BitmapFontWidth;
		BitmapFontWidth = getBitmapFontWidth(price, 0.5f);
		switch(btn){
		case UPGRD_MINIGUN:
			draw(labelTextMinigun, x, y-labelTextMinigun.getImageHeight()/2, labelTextMinigun.getImageWidth(), labelTextMinigun.getImageHeight());
			y -= (labelTextMinigun.getImageHeight()/2 + labelTextPrice.getImageHeight()/2 + 23);
			break;
		case UPGRD_SHOTGUN:
			draw(labelTextShotgun, x, y-labelTextShotgun.getImageHeight()/2, labelTextShotgun.getImageWidth(), labelTextShotgun.getImageHeight());
			y -= (labelTextShotgun.getImageHeight()/2 + labelTextPrice.getImageHeight()/2 + 23);
			break;
		case UPGRD_CANNON:
			draw(labelTextCannon, x, y-labelTextCannon.getImageHeight()/2, labelTextCannon.getImageWidth(), labelTextCannon.getImageHeight());
			y -= (labelTextCannon.getImageHeight()/2 + labelTextPrice.getImageHeight()/2 + 23);
			break;
		case UPGRD_ROCKET:
			draw(labelTextRocket, x, y-labelTextRocket.getImageHeight()/2, labelTextRocket.getImageWidth(), labelTextRocket.getImageHeight());
			y -= (labelTextRocket.getImageHeight()/2 + labelTextPrice.getImageHeight()/2 + 23);
			break;
		case UPGRD_ARMOR:
			draw(labelTextArmor, x, y-labelTextArmor.getImageHeight()/2, labelTextArmor.getImageWidth(), labelTextArmor.getImageHeight());
			y -= (labelTextArmor.getImageHeight()/2 + labelTextPrice.getImageHeight()/2 + 23);
			break;
		case UPGRD_SPEED:
			draw(labelTextSpeed, x, y-labelTextSpeed.getImageHeight()/2, labelTextSpeed.getImageWidth(), labelTextSpeed.getImageHeight());
			y -= (labelTextSpeed.getImageHeight()/2 + labelTextPrice.getImageHeight()/2 + 25);
			break;
		default:
			break;
		}
		draw(labelTextPrice, x, y+8, (int)BitmapFontWidth+15, labelTextPrice.getImageHeight());
		drawFont(price, (int)(x+BitmapFontWidth/2), y, 0.5f, 2);
	}
	
	public void drawHPpipe(int x,int y,int width,int height) {
		HPred.bind();
		glPushMatrix();
		glBegin(GL_QUADS);
			glVertex2f(x-width/2 ,y-height/2);
			glVertex2f((x-width/2)+(width-2)*player.HP/player.maxHP ,y-height/2);
			glVertex2f((x-width/2)+(width-2)*player.HP/player.maxHP ,y+height/2);
			glVertex2f(x-width/2 ,y+height/2);
		glEnd();
		glPopMatrix();
		
		HPblue.bind();
		glPushMatrix();
		glBegin(GL_QUADS);
			glVertex2f((x-width/2)+(width-2)*player.HP/player.maxHP ,y-height/2);
			glVertex2f(x+width/2 ,y-height/2);
			glVertex2f(x+width/2 ,y+height/2);
			glVertex2f((x-width/2)+(width-2)*player.HP/player.maxHP ,y+height/2);
		glEnd();
		glPopMatrix();
		
		HPpipe.bind();
		glPushMatrix();
		glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2f(x-width/2 ,y-height/2);
			glTexCoord2f(HPpipe.getWidth(),0);
			glVertex2f(x+width/2 ,y-height/2);
			glTexCoord2f(HPpipe.getWidth(),HPpipe.getHeight());
			glVertex2f(x+width/2 ,y+height/2);
			glTexCoord2f(0,HPpipe.getHeight());
			glVertex2f(x-width/2 ,y+height/2);
		glEnd();
		glPopMatrix();
		y += height/2;
	}
   
    public static void main(String[] args) throws IOException, LWJGLException {
            new Game();
    }
}