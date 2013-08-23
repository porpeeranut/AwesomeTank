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
 
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
 
public class testRPG{
        private Texture sprite;
        private Map map;
        private Entity player;
        static private int camera_x,camera_y,camera_w,camera_h;
        static final int WORLD_W,WORLD_H;
        private float gunRotation = 0;
        private Box box;
        private int maxSpeed = 20,speed = 0;
        private boolean keyControlRelease;
       
        
        static{
        	//initial world size
            WORLD_W = 1920;
            WORLD_H = 1200;
        }
       
        public testRPG(){
        	//initialed value of camera
        	camera_x = 0;
            camera_y = 0;
            camera_w = 640;
            camera_h = 480;
            try {
				Display.setIcon(new ByteBuffer[] {
				        loadIcon(getClass().getResource("gameIcon.png"))  // "bin/gameIcon.png" size 32x32
				    });
			} catch (IOException e1) {
				e1.printStackTrace();
			}

            //initial display
            try {
            	Display.setTitle("scrolling");
                Display.setDisplayMode(new DisplayMode(camera_w, camera_h));
                Display.create();
            } catch (LWJGLException e) {
            	e.printStackTrace();
            }
            
            //initial texture
            map = new Map();
            sprite = loadTexture("gun.png");
            player = new Entity(sprite, map, 81f, 81f);
           
            //initial box
            //box = new Box(0,0,100,50);
            //box = new Box(0,0,sprite.getImageWidth(),sprite.getImageHeight());
                   
            //initialization opengl code
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            //glOrtho(0 ,640 ,480 ,0 ,-1 , 1);
            glOrtho(camera_x ,640+camera_x ,480+camera_y ,camera_y ,-1 , 1);
            glMatrixMode(GL_MODELVIEW);
            glEnable(GL_TEXTURE_2D);
            
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            while(!Display.isCloseRequested()){
            	//clear screen
                glClear(GL_COLOR_BUFFER_BIT);
                Display.setTitle(String.valueOf(Mouse.getX())+"\n");
               
                input();
               
                /*sky.bind();
                glBegin(GL_QUADS);
                	glTexCoord2f(0,0);
                	glVertex2f(0 ,0);//upper left
                	glTexCoord2f(1,0);
                	glVertex2f(WORLD_W ,0);//upper right
                	glTexCoord2f(1,1);
                	glVertex2f(WORLD_W ,WORLD_H);//bottom right
                	glTexCoord2f(0,1);
                	glVertex2f(0 ,WORLD_H);//bottom left
                glEnd();*/
                
                //draw
                map.draw();
                
                sprite.bind();
                player.draw();
               
                //set camera
                setCamera();
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                glOrtho(camera_x ,640+camera_x ,480+camera_y ,camera_y ,-1 , 1);
                //glOrtho(0-camera_x ,640+camera_x ,480+camera_y ,0-camera_y ,-1 , 1);
                Display.update();
                Display.sync(60);
            }
            Display.destroy();
        }
       
        private void input() {
            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
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
            
            //control
            if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            	player.move(0, -5);
                camera_y -= 5; 
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            	player.move(0, 5);
                camera_y += 5;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            	player.move(5, 0);
                camera_x += 5;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            	player.move(-5, 0);
                camera_x -= 5;
            }
            //System.out.println("camera at "+camera_x+","+camera_y);
		}
        
        private void setCamera(){
        	float mouseX = (Mouse.getX() - camera_w/2)*0.5f;
        	float mouseY = (Mouse.getY() - camera_h/2)*0.5f;
        	
        	//Display.setTitle(String.valueOf(gunRotation));
        	gunRotation = 57.2957795f*(float)Math.atan2(camera_h/2 - Mouse.getY(),Mouse.getX() - camera_w/2);
        	gunRotation += 180;
        	player.setGunAngle(gunRotation);

        	camera_x = (int)mouseX + (player.get_centerX() - camera_w/2);
        	camera_y = -(int)mouseY + (player.get_centerY() - camera_h/2);
        }

		private Texture loadTexture(String key){
        	try {
                return TextureLoader.getTexture("JPG", new FileInputStream(new File("res/"+key)));
        	} catch (FileNotFoundException e) {
                e.printStackTrace();
        	} catch (IOException e) {
                e.printStackTrace();
        	}
        	return null;
        }
		
		private ByteBuffer loadIcon(URL url) throws IOException {
            InputStream is = url.openStream();
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
       
        private static class Box{
        	private int x,y;
            private int width,height;
           
            public Box(int x ,int y,int width ,int height){
            	this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
            }
           
            public void draw(){
            	glBegin(GL_QUADS);
                	glTexCoord2f(0,0);
                	glVertex2f(x ,y );//upper left
                	glTexCoord2f(1,0);
                	glVertex2f(x+width ,y);//upper right
                	glTexCoord2f(1,1);
                	glVertex2f(x+width ,y+height);//bottom right
                	glTexCoord2f(0,1);
                	glVertex2f(x ,y+height);//bottom left
                glEnd();
                //apply_surface( box.x - camera.x ,box.y - camera.y ,sprite ,screen);
            }
           
            public void move(int getX ,int getY){
            	x += getX;
                if(x<0){
                	x=0;
                }
                if(x>WORLD_W-width){
                	x=WORLD_W-width;
                }
                y += getY;
                if(y<0){
                	y=0;
                }
                if(y>WORLD_H-height){
                	y=WORLD_H-height;
                }
                //System.out.println(x +" "+y);
            }
           
            public int get_centerX(){
                    return x+width/2;
            }
           
            public int get_centerY(){
                    return y+height/2;
            }
           
            public int get_w(){
                    return width;
            }
           
            public int get_h(){
                    return height;
            }
        }
       
        public static void main(String[] args) {
                new testRPG();
        }
}