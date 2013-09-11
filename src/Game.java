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
		INTRO,UPGRADE,SELECT_LEVEL,PLAY,PAUSE;
	}
	private State state = State.INTRO;
	public static enum CurrentButton {
		NONE,MENU,PLAY,PAUSE,BACKTOUPGRADE,LV1;
	}
	private Button[] btn;
	private CurrentButton currentButton = CurrentButton.NONE;
	private Texture background;
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

		background = loadTexture("intro.png");
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
            
            long delta = getTime() - lastLoopTime;
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
        		drawBackground(background);
        		button_In_INTRO_state();
    			break;
        	case UPGRADE:
        		drawBackground(background);
        		button_In_UPGRADE_state();
    			break;
        	case SELECT_LEVEL:
        		drawBackground(background);
        		button_In_SELECT_LEVEL_state();
    			break;
    		case PLAY:
    		case PAUSE:
    			if(numEnemy == 0){
        			entities.clear();
        			level++;
        			//SetGame(level);
        		}
        		initBulletX = (float)(player.x-Math.cos(0.0174532925*gunRotation)*player.width/1.5);
        		initBulletY = (float)(player.y-Math.sin(0.0174532925*gunRotation)*player.height/1.5);
               
        		if(state != State.PAUSE){
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
        		
                //draw
                map.draw();
                for ( Entity entity : entities ) {
                	/*if(entity instanceof MyTank)
                		player.draw();
                	else*/
                		entity.draw();
    			}
                button_In_PLAY_state();
    			break;
    		}
            
            Display.update();
            Display.sync(60);
        }
        soundManager.destroy();
        Display.destroy();
        System.exit(0);
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
	        				background = loadTexture("upgrade.png");
	        				btn = new Button[2];            	        		
	        				btn[0] = new Button(this,CurrentButton.MENU,120,600);
            				btn[1] = new Button(this,CurrentButton.PLAY,520,600);
	        				state = State.UPGRADE;
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
    	    				background = loadTexture("intro.png");
    	    				state = State.INTRO;
    	    			}            	        		
    	    		}else if(btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			soundManager.playEffect(SOUND_RELEASE);
    	    			if(currentButton == CurrentButton.PLAY){
    	    				background = loadTexture("selectLV.png");
	        				btn = new Button[2];            	        		
	        				btn[0] = new Button(this,CurrentButton.BACKTOUPGRADE,320,600);
            				btn[1] = new Button(this,CurrentButton.LV1,100,200);
        	        		state = State.SELECT_LEVEL;
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
    	    				background = loadTexture("upgrade.png");
	        				btn = new Button[2];            	        		
	        				btn[0] = new Button(this,CurrentButton.MENU,120,600);
            				btn[1] = new Button(this,CurrentButton.PLAY,520,600);
	        				state = State.UPGRADE;
    	    			}            	        		
    	    		}else if(btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			soundManager.playEffect(SOUND_RELEASE);
    	    			if(currentButton == CurrentButton.PLAY){
	        				btn = new Button[2];            	        		
	        				btn[0] = new Button(this,CurrentButton.MENU,120,600);
            				btn[1] = new Button(this,CurrentButton.PAUSE,520,600);
            				SetGame(1);
            				soundManager.playEffect(SOUND_PLAY);
            				state = State.PLAY;
    	    			}
    	    		} else
    	    			currentButton = CurrentButton.NONE;
        			break;
    	    	}
    	    }
    	}
	}
	
	private void button_In_PLAY_state() {
		for(int i = 0;i < btn.length;i++){
			glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
			glOrtho(0 ,640 ,650 ,0 ,-1 , 1);
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
    	        		currentButton = CurrentButton.PAUSE;
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
    	    				background = loadTexture("upgrade.png");
	        				btn = new Button[2];            	        		
	        				btn[0] = new Button(this,CurrentButton.MENU,120,600);
            				btn[1] = new Button(this,CurrentButton.PLAY,520,600);
            				glMatrixMode(GL_PROJECTION);
                            glLoadIdentity();
            				glOrtho(0 ,640 ,650 ,0 ,-1 , 1);
	        				state = State.UPGRADE;
    	    			}            	        		
    	    		}else if(btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
    	    			soundManager.playEffect(SOUND_RELEASE);
    	    			if(currentButton == CurrentButton.PAUSE)
    	    				if(state == State.PLAY)
    	    					state = State.PAUSE;
    	    				else
    	    					state = State.PLAY;
    	    		} else
    	    			currentButton = CurrentButton.NONE;
        			break;
    	    	}
    	    }
    	}
	}
	
	private void input() {
    	switch(state){
    	case INTRO:
			break;
    	case UPGRADE:
			break;
    	case SELECT_LEVEL:
			break;
		case PLAY:
			boolean KEY_W = Keyboard.isKeyDown(Keyboard.KEY_W);
	    	boolean KEY_S = Keyboard.isKeyDown(Keyboard.KEY_S);
	    	boolean KEY_D = Keyboard.isKeyDown(Keyboard.KEY_D);
	    	boolean KEY_A = Keyboard.isKeyDown(Keyboard.KEY_A);
	    	boolean KEY_1 = Keyboard.isKeyDown(Keyboard.KEY_1);
	    	boolean KEY_2 = Keyboard.isKeyDown(Keyboard.KEY_2);
	    	boolean KEY_3 = Keyboard.isKeyDown(Keyboard.KEY_3);
	    	boolean KEY_4 = Keyboard.isKeyDown(Keyboard.KEY_4);
	    	
	    	player.setDX(0);
	        player.setDY(0);
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
	        	if(!btn[0].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY()) && !btn[1].On_Mouse_Over(Mouse.getX(), 650 - Mouse.getY())){
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
			break;
    	}
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
        	soundManager.destroy();
        	Display.destroy();
            System.exit(0);
        }
        
        /*if(keyControlRelease) {
        	//Display.setTitle("ss");
        	if(speed > 0)
        		speed -= 5;
        }*/
        
        /*while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_W) {
                	//########################################
                	if(speed < maxSpeed)
                    	speed += 5;
                    box.move(0, -speed);
                    keyControlRelease = false; 
                	Display.setTitle("w pres");
                	player.move(0, -5);
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_S) {
                	keyControlRelease = false;
                	player.move(0, 5);
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_D) {
                	keyControlRelease = false;
                	player.move(5, 0);
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                	keyControlRelease = false;
                	player.move(-5, 0);
                }
            }
            else {
                if (Keyboard.getEventKey() == Keyboard.KEY_W) {
                	Display.setTitle("w release");
                	keyControlRelease = true;
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_S) {
                	keyControlRelease = true;
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_D) {
                	keyControlRelease = true;
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                	keyControlRelease = true;
                }
            }
        }*/
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
		removeList.add(entity);
	}
    
    private void loadResource() {
    	player = new MyTank(this,200);
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
	        player.setPositionToMap(2,3);
	        player.setGun(player.gunType.MINIGUN);
	        player.rocketReleased = false;
	        for(int i = 0;i < brick.length;i++){
	        	brick[i].setHP(30);
	        	brick[i].setPositionToMap(i+3, 3);
	        	brick[i].showHP = false;
	        	brick[i].died = false;
	        	entities.add(brick[i]);
	        }
	        for(int i = 0;i < bmWall.length;i++){
	        	bmWall[i].setHP(30);
	        	bmWall[i].setPositionToMap(i+2, 4);
	        	bmWall[i].showHP = false;
	        	bmWall[i].died = false;
	        	entities.add(bmWall[i]);
	        }
	        for(int i = 0;i < oilTank.length;i++){
	        	oilTank[i].setHP(15);
	        	oilTank[i].setPositionToMap(i+10, 4);
	        	oilTank[i].showHP = false;
	        	oilTank[i].died = false;
	        	entities.add(oilTank[i]);
	        }
	        entities.add(player);
			for (int i = 0; i < enemyTank.length; i++) {
				enemyTank[i].setHP(50);
				enemyTank[i].setPositionToMap(i+5, 4);
				enemyTank[i].setBodyAngle(39);
				enemyTank[i].showHP = false;
				enemyTank[i].died = false;
				entities.add(enemyTank[i]);
			}
			turret.setHP(50);
			turret.setPositionToMap(15, 4);
			turret.showHP = false;
			turret.died = false;
			entities.add(turret);
			numEnemy = enemyTank.length + 1;
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
	
	public void drawBackground(Texture tex) {
		glPushMatrix();
		background.bind();
        glBegin(GL_QUADS);
        	glTexCoord2f(0,0);
        	glVertex2f(0 ,0);//upper left
        	glTexCoord2f(background.getWidth(),0);
        	glVertex2f(camera_w ,0);//upper right
        	glTexCoord2f(background.getWidth(),background.getHeight());
        	glVertex2f(camera_w ,camera_h);//bottom right
        	glTexCoord2f(0,background.getHeight());
        	glVertex2f(0 ,camera_h);//bottom left
        glEnd();
        glPopMatrix();
	}
   
    public static void main(String[] args) throws IOException, LWJGLException {
            new Game();
    }
}