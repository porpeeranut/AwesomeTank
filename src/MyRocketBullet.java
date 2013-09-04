import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;


public class MyRocketBullet extends MyBullet {

	public MyRocketBullet(Game ingame,int speed,int atck) {
		super(ingame,"rocket.png");
		moveSpeed = speed;
		attack = atck;
		width = (int)(ingame.map.TILE_SIZE*0.43);
        height = (int)(ingame.map.TILE_SIZE*0.14);
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
