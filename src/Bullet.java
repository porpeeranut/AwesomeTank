import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.Random;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;


public class Bullet extends Entity {
	
	public int moveSpeed;
	public int attack;
	protected Texture bull;
	protected boolean used;

	public Bullet(Game ingame,String pic) {
		game = ingame;
		this.dx = moveSpeed;
		this.dy = moveSpeed;
		bull = loadTexture(pic);
		width = (int)(ingame.map.TILE_SIZE*0.17);
        height = (int)(ingame.map.TILE_SIZE*0.17);
		halfSize = width/2;
	}
	
	public int getMoveSpeed() {
		return moveSpeed;
	}
	
	public void reinitialize(float x, float y,float dx,float dy) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		used = false;
	}
	
	public void draw() {
		super.draw(bull);
	}

	@Override
	public void collidedWith(Entity other){
		int amount = new Random().nextInt(15) + 5;	// random 5 to 15
		int i;
		if (used|| other instanceof Bullet 
				|| other instanceof Effect
				|| other instanceof HPpotion
				|| other instanceof Gold) {
			return;
		}
		if(this instanceof MyBullet && other instanceof MyTank)
			return;
		if(this instanceof EnemyBullet && other instanceof EnemyTank)
			return;
		setDX(0);
		setDY(0);
		used = true;
		game.removeEntity(this);
		if(this instanceof MyCannonBullet || this instanceof MyRocketBullet){
			game.addEntity(new BombEffect_BigBullet(game,x,y));
			game.soundManager.playEffect(game.SOUND_BOMB_TANK);
			if(this instanceof MyRocketBullet)
				game.player.rocketReleased = false;
		} else {
			game.addEntity(new BulletShotEffect(game,x,y));
		}
			
		other.damage(attack);
		if(other.getHP() <= 0){
			game.removeEntity(other);
			if(other instanceof BombWall){
				if(!((BombWall) other).died){
					((BombWall) other).died = true;
					game.addEntity(new BombEffect_BombWall(game,other.x,other.y));
					game.soundManager.playEffect(game.SOUND_BOMB_BRICK);
				}
			} else if (other instanceof OilTank){
				if(!((OilTank) other).died){
					((OilTank) other).died = true;
					game.addEntity(new BombEffect_OilTank(game,other.x,other.y));
					game.soundManager.playEffect(game.SOUND_BOMB_OILTANK);
				}
			} else if (other instanceof Box){
				if(!((Box) other).died){
					((Box) other).died = true;
					game.addEntity(new BombEffect_basic(game,other.x,other.y));
					
					//############ random item ############################
					//game.addEntity(new HPpotion(game,other.x,other.y));
					
					for(i = game.goldIndex;i < amount+game.goldIndex;i++){
						if(i >= game.gold.length){
							amount += game.goldIndex - i;
							game.goldIndex = 0;
							i = 0;
						}
						game.gold[i].setXY((int)other.x,(int)other.y);
						game.gold[i].setDX(new Random().nextInt(7) - 3);	// random -3 to 3
						game.gold[i].setDY(new Random().nextInt(7) - 3);
						game.addEntity(game.gold[i]);
						System.out.println(i);
					}
					game.goldIndex = i;
					
					game.soundManager.playEffect(game.SOUND_BOMB_BOX);
				}
			} else if(other instanceof EnemyTank || other instanceof Turret || other instanceof MyTank){
				if(!other.died){
					other.died = true;
					game.addEntity(new BombEffect_basic(game,other.x,other.y));
					game.soundManager.playEffect(game.SOUND_BOMB_TANK);
					game.numEnemy--;
					
					if(!(other instanceof MyTank)){
						for(i = game.goldIndex;i < amount+game.goldIndex;i++){
							if(i >= game.gold.length){
								amount += game.goldIndex - i;
								game.goldIndex = 0;
								i = 0;
							}
							game.gold[i].setXY((int)other.x,(int)other.y);
							game.gold[i].setDX(new Random().nextInt(7) - 3);	// random -3 to 3
							game.gold[i].setDY(new Random().nextInt(7) - 3);
							game.addEntity(game.gold[i]);
							System.out.println(i);
						}
						game.goldIndex = i;
					}
				}
			} else {
				game.addEntity(new BombEffect_basic(game,other.x,other.y));
				game.soundManager.playEffect(game.SOUND_BOMB_BRICK);
			}
		}
	}
}
