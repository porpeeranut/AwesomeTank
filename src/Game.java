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
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
 
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
 
public class Game{
	private static enum State {
		INTRO,UPGRADE,SELECT_LEVEL,PLAY,BACKTOMENU,PAUSE,HELP,LVCOMPLETE,LVFAILED;
	}
	private static State state = State.INTRO;
	public static enum CurrentButton {
		NONE,MENU,PLAY,PAUSE,HELP,BACKTOUPGRADE,LV1,CONTINUE,BCK_TO_MENU_YES,BCK_TO_MENU_NO;
	}
	private Button[] btn;
	private CurrentButton currentButton = CurrentButton.NONE;
	private Texture backgndIntro;
	private Texture backgndUpgrade;
	private Texture backgndSelectLV;
	private Texture selectGunBar;
	private Texture pauseLabel;
	private Texture helpLabel;
	private Texture backToMenuLabel;
	private Texture lvCompleteLabel;
	private Texture lvFailedLabel;
	private int Label_X;
	private int Label_Y;
	private int timeShowLabel;
	private static ArrayList<Entity> entities = new ArrayList<Entity>();
	private static ArrayList<Entity> removeList = new ArrayList<Entity>();
    public Map map;
    private int level = 1;
    public MyTank player;
    static private int camera_x,camera_y,camera_w,camera_h;
    static private int camera_x_tmp,camera_y_tmp;
    static final int WORLD_W,WORLD_H;
    private float gunRotation = 0;
    float bodyAng = 0;
    private int maxSpeed = 20,speed = 2;
    private boolean keyControlRelease;
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
    public static int SOUND_SHOT_TANK;
    public static int SOUND_BOMB_TANK;
    public static int SOUND_BOMB_BRICK;
    public static int SOUND_BOMB_OILTANK;
    public static int SOUND_CLICK;
    public static int SOUND_RELEASE;
    public static int SOUND_PLAY;
    
    public float initBulletX,initBulletY;
    
	private EnemyTank[] enemyTank;
	public static int numEnemy;
	
	private Brick[] brick;
	private BombWall[] bmWall;
	private OilTank[] oilTank;
	private Turret turret;
    
    static{
    	//initial world size
        WORLD_W = 1920;
        WORLD_H = 1200;
    }
   
    public Game() throws IOException, LWJGLException{
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
		SOUND_SHOT_TANK	= soundManager.addSound("shot_tank.wav");
		SOUND_BOMB_TANK	= soundManager.addSound("bomb_tank.wav");
		SOUND_BOMB_BRICK	= soundManager.addSound("bomb_brick.wav");
		SOUND_BOMB_OILTANK	= soundManager.addSound("bomb_oil.wav");
		SOUND_CLICK		= soundManager.addSound("mouse_click.wav");
		SOUND_RELEASE	= soundManager.addSound("mouse_release.wav");
		SOUND_PLAY		= soundManager.addSound("play.wav");

		map = new Map();
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
    			//Display.setTitle("Awesome Tank (FPS: " + fps + ")");
    			lastFpsTime = 0;
    			fps = 0;
    		}
    		
    		
    		switch(state){
        	case INTRO:
        		draw(backgndIntro,camera_w/2,camera_h/2,camera_w,camera_h);
        		button_In_INTRO_state();
    			break;
        	case UPGRADE:
        		draw(backgndUpgrade,camera_w/2,camera_h/2,camera_w,camera_h);
        		button_In_UPGRADE_state();
    			break;
        	case SELECT_LEVEL:
        		draw(backgndSelectLV,camera_w/2,camera_h/2,camera_w,camera_h);
        		button_In_SELECT_LEVEL_state();
    			break;
    		case PLAY:
    		case PAUSE:
    		case HELP:
    		case BACKTOMENU:
    		case LVCOMPLETE:
    		case LVFAILED:
    			if(numEnemy <= 0 && state != State.LVFAILED){
    				state = State.LVCOMPLETE;
        		}
        		initBulletX = (float)(player.x-Math.cos(0.0174532925*gunRotation)*player.width/1.5);
        		initBulletY = (float)(player.y-Math.sin(0.0174532925*gunRotation)*player.height/1.5);
               
        		if(state != State.BACKTOMENU && state != State.PAUSE && state != State.HELP){
        			player.setDX(0);
        	        player.setDY(0);
        			if(state != State.LVCOMPLETE && state != State.LVFAILED)
        				input();
            		
                    if(!soundManager.isPlayingSound()){
                    	for (int p = 0; p < entities.size(); p++) {
                    		Entity entity = entities.get(p);
                        	if(entity instanceof MyTank)
                        		player.move(delta, bodyAng);
                        	else
                        		entity.move(delta);
                        	if(entity instanceof Bullet){
            					if(map.blocked((int)entity.x/map.TILE_SIZE, (int)entity.y/map.TILE_SIZE)){
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
                    }
                    
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
                	/*if(entity instanceof MyTank)
                		player.draw();
                	else*/
                		entity.draw();
    			}
                button_In_PLAY_state();
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
    
    private void move_to_UPGRADE_state() {
    	btn = new Button[2];            	        		
		btn[0] = new Button(this,CurrentButton.MENU,120,600);
		btn[1] = new Button(this,CurrentButton.PLAY,520,600);
		state = State.UPGRADE;
    }
    
    private void move_to_SELECT_LEVEL_state() {
    	btn = new Button[2];            	        		
    	btn[0] = new Button(this,CurrentButton.BACKTOUPGRADE,320,600);
    	btn[1] = new Button(this,CurrentButton.LV1,100,200);
    	state = State.SELECT_LEVEL;
    }
    
    private void move_to_PLAY_state() {
    	btn = new Button[7];            	        		
    	btn[0] = new Button(this,CurrentButton.MENU,93,610);
    	btn[1] = new Button(this,CurrentButton.PAUSE,533,610);
    	btn[2] = new Button(this,CurrentButton.CONTINUE,320,350);
    	btn[3] = new Button(this,CurrentButton.BCK_TO_MENU_YES,250,350);
    	btn[4] = new Button(this,CurrentButton.BCK_TO_MENU_NO,390,350);
    	btn[5] = new Button(this,CurrentButton.HELP,570,610);
    	btn[6] = new Button(this,CurrentButton.PLAY,320,450);
    	SetGame(1);
    	soundManager.playEffect(SOUND_PLAY);
    	state = State.PLAY;
    }

	private void button_In_INTRO_state() {
    	btn = new Button[1];	
		btn[0] = new Button(this,CurrentButton.PLAY,320,340);
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
    	        		currentButton = CurrentButton.PLAY;
    	        	}
    	        	else
            			currentButton = CurrentButton.NONE;
	        		break;
    	        }
    	    } else {	// mouse release
    	    	switch (Mouse.getEventButton()) {
    	    	case 0:
    	    		if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			soundManager.playEffect(SOUND_RELEASE);
    	    			if(currentButton == CurrentButton.PLAY){
    	    				move_to_UPGRADE_state();
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
			if(btn[i].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY()))
    			btn[i].draw_OnMouseOver();
    		else
    			btn[i].draw();
		}
		while (Mouse.next()) {
    	    if (Mouse.getEventButtonState()) {
    	        switch (Mouse.getEventButton()) {
    	        case 0:
    	        	if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        		soundManager.playEffect(SOUND_CLICK);
    	        		currentButton = CurrentButton.MENU;
    	        	}
    	        	else if(btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        		soundManager.playEffect(SOUND_CLICK);
    	        		currentButton = CurrentButton.PLAY;
    	        	}
    	    		else
    	    			currentButton = CurrentButton.NONE;
	        		break;
    	        }
    	    } else {	// mouse release
    	    	switch (Mouse.getEventButton()) {
    	    	case 0:
    	    		if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			soundManager.playEffect(SOUND_RELEASE);
    	    			if(currentButton == CurrentButton.MENU){
    	    				state = State.INTRO;
    	    			}            	        		
    	    		}else if(btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			soundManager.playEffect(SOUND_RELEASE);
    	    			if(currentButton == CurrentButton.PLAY){
    	    				move_to_SELECT_LEVEL_state();
    	    			}
    	    		} else
    	    			currentButton = CurrentButton.NONE;
        			break;
    	    	}
    	    }
    	}
	}

	private void button_In_SELECT_LEVEL_state() {
		for(int i = 0;i < btn.length;i++){
			if(btn[i].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY()))
    			btn[i].draw_OnMouseOver();
    		else
    			btn[i].draw();
		}
		while (Mouse.next()) {
    	    if (Mouse.getEventButtonState()) {
    	        switch (Mouse.getEventButton()) {
    	        case 0:
    	        	if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        		soundManager.playEffect(SOUND_CLICK);
    	        		currentButton = CurrentButton.BACKTOUPGRADE;
    	        	}
    	        	else if(btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	        		soundManager.playEffect(SOUND_CLICK);
    	        		currentButton = CurrentButton.PLAY;
    	        	}
    	    		else
    	    			currentButton = CurrentButton.NONE;
	        		break;
    	        }
    	    } else {	// mouse release
    	    	switch (Mouse.getEventButton()) {
    	    	case 0:
    	    		if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			soundManager.playEffect(SOUND_RELEASE);
    	    			if(currentButton == CurrentButton.BACKTOUPGRADE){
    	    				move_to_UPGRADE_state();
    	    			}            	        		
    	    		}else if(btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			soundManager.playEffect(SOUND_RELEASE);
    	    			if(currentButton == CurrentButton.PLAY){
    	    				move_to_PLAY_state();
    	    			}
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
		draw(selectGunBar, 320, 610, 592, 81);
		// ########### btn menu ###########
		if(btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY()))
			btn[0].draw_OnMouseOver();
		else
			btn[0].draw();
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
			setTimeToShowLabel();
			draw(lvCompleteLabel, 320+Label_X, 250+Label_Y, 361, 92);
		}
		if(state == State.LVFAILED){
			setTimeToShowLabel();
			draw(lvFailedLabel, 320+Label_X, 250+Label_Y, 361, 92);
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
        	        			move_to_UPGRADE_state();
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
	
	private void setTimeToShowLabel() {
		timeShowLabel += delta;
		if(timeShowLabel > 50 && timeShowLabel < 200){
			Label_Y += 3;
		} else if(timeShowLabel >= 1700 && timeShowLabel < 1900){
			Label_X -= (1900 - timeShowLabel)/20;
		} else if(timeShowLabel >= 1900 && timeShowLabel < 2400){
			Label_X += (timeShowLabel-1900)/10;
		} else if(timeShowLabel > 2400){
			timeShowLabel = 0;
			Label_X = 0;
			Label_Y = 0;
			btn = new Button[2];            	        		
			btn[0] = new Button(this,CurrentButton.MENU,120,600);
			btn[1] = new Button(this,CurrentButton.PLAY,520,600);
			glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
			glOrtho(0 ,640 ,650 ,0 ,-1 , 1);
			state = State.UPGRADE;
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
        	player.setDY(-speed);
        }
        if(KEY_S && !KEY_W){
        	bodyAng = 270;
        	if(KEY_A)
        		bodyAng = 315;
        	if(KEY_D)
        		bodyAng = 225;
        	player.setDY(speed);
        }
        if(KEY_D && !KEY_A){
        	bodyAng = 180;
        	if(KEY_W)
        		bodyAng = 135;
        	if(KEY_S)
        		bodyAng = 225;
        	player.setDX(speed);
        }
        if(KEY_A && !KEY_D){
        	bodyAng = 0;
        	if(KEY_W)
        		bodyAng = 45;
        	if(KEY_S)
        		bodyAng = 315;
        	player.setDX(-speed);
        }
        if(KEY_1 || KEY_2 || KEY_3 || KEY_4){
        	while (Keyboard.next()) {
        	    if (Keyboard.getEventKeyState()) {
        	        switch (Keyboard.getEventKey()) {
        	        	case Keyboard.KEY_1:
        	        		soundManager.playEffect(SOUND_CHANGE_GUN);
        	        		player.setGun(player.gunType.MINIGUN);
        	        		break;
        	        	case Keyboard.KEY_2:
        	        		soundManager.playEffect(SOUND_CHANGE_GUN);
        	        		player.setGun(player.gunType.SHOTGUN);
        	        		break;
        	        	case Keyboard.KEY_3:
        	        		soundManager.playEffect(SOUND_CHANGE_GUN);
        	        		player.setGun(player.gunType.CANNON);
        	        		break;
        	        	case Keyboard.KEY_4:
        	        		soundManager.playEffect(SOUND_CHANGE_GUN);
        	        		player.setGun(player.gunType.ROCKET);
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
        
        if(!soundManager.isPlayingSound()){
        	if(!btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())
        			&& !btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())
        			&& !btn[5].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
        		if(player.gunType == player.gunType.ROCKET){
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
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
        	/*soundManager.destroy();
        	Display.destroy();
            System.exit(0);*/
        	state = State.PAUSE;
        }
	}
    
    private void setCamera(){
    	// mouse position (0,0) at center screen
    	float mouseX = (Mouse.getX() - camera_w/2)*0.5f;
    	float mouseY = (Mouse.getY() - camera_h/2)*0.5f;
    	
    	String str = String.valueOf(gunRotation);
    	
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
        		}
        		else{
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
        		}
        		else{
        			camera_y = camera_y_tmp - 5;
        			camera_y_tmp -= 5;
        		}
        	} else {
            	camera_y_tmp = camera_y;
        	}
    	} else{
    		camera_x = (int)mouseX + (int)(player.get_centerX() - camera_w/2);
        	camera_y = -(int)mouseY + (int)(player.get_centerY() - camera_h/2);
        	if(Math.abs(camera_x_tmp - camera_x) > 25){
        		if(camera_x > camera_x_tmp){
        			camera_x = camera_x_tmp + 25;
        			camera_x_tmp += 25;
        		}
        		else{
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
        		}
        		else{
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
    	backgndIntro = loadTexture("intro.png");
    	backgndUpgrade = loadTexture("upgrade.png");
    	backgndSelectLV = loadTexture("selectLV.png");
    	selectGunBar = loadTexture("selectGunBar.png");
    	pauseLabel = loadTexture("pauseLabel.png");
    	helpLabel = loadTexture("helpLabel.png");
    	backToMenuLabel = loadTexture("backToMenuLabel.png");
    	lvCompleteLabel = loadTexture("LVcomplete.png");
    	lvFailedLabel = loadTexture("LVfailed.png");
    	player = new MyTank(this,20);
    	brick = new Brick[11];
    	bmWall = new BombWall[8];
    	oilTank = new OilTank[3];
    	enemyTank = new EnemyTank[5];
    	turret = new Turret(this, 50);
    	for(int i = 0;i < brick.length;i++){
        	brick[i] = new Brick(this,30);
    	}
    	for(int i = 0;i < bmWall.length;i++){
        	bmWall[i] = new BombWall(this,30);
        }
    	for(int i = 0;i < oilTank.length;i++){
        	oilTank[i] = new OilTank(this,15);
    	}
    	for (int i = 0; i < enemyTank.length; i++) {
			enemyTank[i] = new EnemyTank(this,50);
    	}
	}
    
    private void SetGame(int LV){
    	entities.clear();
    	camera_x_tmp = 0;
    	camera_y_tmp = 0;
    	switch (LV) {
		case 1:
			player.setHP(2220);
	        player.setPositionToMap(2,3);
	        player.reset();
	        player.setGun(player.gunType.MINIGUN);
	        player.rocketReleased = false;
	        entities.add(player);
	        for(int i = 0;i < brick.length;i++){
	        	brick[i].setHP(30);
	        	brick[i].setPositionToMap(i+3, 3);
	        	brick[i].reset();
	        	entities.add(brick[i]);
	        }
	        for(int i = 0;i < bmWall.length;i++){
	        	bmWall[i].setHP(30);
	        	bmWall[i].setPositionToMap(i+2, 4);
	        	bmWall[i].reset();
	        	entities.add(bmWall[i]);
	        }
	        for(int i = 0;i < oilTank.length;i++){
	        	oilTank[i].setHP(15);
	        	oilTank[i].setPositionToMap(i+10, 4);
	        	oilTank[i].reset();
	        	entities.add(oilTank[i]);
	        }
			for (int i = 0; i < enemyTank.length; i++) {
				enemyTank[i].setHP(50);
				enemyTank[i].setPositionToMap(i+5, 4);
				enemyTank[i].setBodyAngle(39);
				enemyTank[i].reset();
				entities.add(enemyTank[i]);
			}
			turret.setHP(50);
			turret.setPositionToMap(15, 4);
			turret.reset();
			entities.add(turret);
			//numEnemy = enemyTank.length + 1;
			numEnemy = 1;
			break;
		case 2:
			map.createMap(2);
			break;
		}
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
	
	private Texture loadTexture(String key){
    	try {
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
   
    public static void main(String[] args) throws IOException, LWJGLException {
            new Game();
    }
}