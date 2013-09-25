import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class MyRocketBullet extends MyBullet {
	
	private long lastFrameChange;
	private long frameDuration = 100;

	public MyRocketBullet(Game ingame,int speed,int atck) {
		super(ingame,"rocket.png");
		moveSpeed = speed;
		attack = atck;
		width = (int)(Map.TILE_SIZE*0.43);
        height = (int)(Map.TILE_SIZE*0.14);
	}
	
	public void move(long delta) {
		lastFrameChange += delta;
		if (lastFrameChange > frameDuration) {
			float initBulletX,initBulletY;
			lastFrameChange = 0;
			initBulletX = (float)(x+Math.cos(0.0174532925*game.player.gunAngle)*width/1.5);
    		initBulletY = (float)(y+Math.sin(0.0174532925*game.player.gunAngle)*height/1.5);
			Game.addEntity(new Effect_smoke(game,initBulletX,initBulletY));
		}
		super.move(delta);
	}
	
	public void draw() {
		glPushMatrix();
		glTranslatef(x, y, 0);
        glRotatef(game.player.gunAngle, 0f, 0f, 1f);
        glTranslatef(-x, -y, 0);
        super.draw(bull);
        glPopMatrix();
	}
}
