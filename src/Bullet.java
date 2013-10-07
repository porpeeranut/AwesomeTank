import java.util.Random;

import org.newdawn.slick.opengl.Texture;

public class Bullet extends Entity {
	
	public float moveSpeed;
	public int attack;
	protected Texture bull;
	protected boolean used;

	public Bullet(Game ingame,String pic) {
		game = ingame;
		this.dx = moveSpeed;
		this.dy = moveSpeed;
		bull = Game.loadTexture(pic);
		width = (int)(path_map.TILE_SIZE*0.17);
        height = (int)(path_map.TILE_SIZE*0.17);
		halfSize = width/2;
	}
	
	public float getMoveSpeed() {
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
		Game.removeEntity(this);
		if(this instanceof MyCannonBullet || this instanceof MyRocketBullet){
			Game.addEntity(new BombEffect_BigBullet(game,x,y));
			Game.soundManager.playEffect(Game.SOUND_BOMB_TANK);
			if(this instanceof MyRocketBullet)
				Game.player.rocketReleased = false;
		} else {
			Game.addEntity(new BulletShotEffect(game,x,y));
		}
			
		other.damage(attack);
		if(other.getHP() <= 0){
			Game.removeEntity(other);
			if(other instanceof BombWall){
				if(!((BombWall) other).died){
					((BombWall) other).died = true;
					Game.addEntity(new BombEffect_BombWall(game,other.x,other.y));
					Game.soundManager.playEffect(Game.SOUND_BOMB_BRICK);
				}
			} else if (other instanceof OilTank){
				if(!((OilTank) other).died){
					((OilTank) other).died = true;
					Game.addEntity(new BombEffect_OilTank(game,other.x,other.y));
					Game.soundManager.playEffect(Game.SOUND_BOMB_OILTANK);
				}
			} else if (other instanceof Box){
				if(!((Box) other).died){
					((Box) other).died = true;
					Game.addEntity(new BombEffect_basic(game,other.x,other.y));
					
					switch(new Random().nextInt(2)){
					case 0:
						Game.addEntity(new HPpotion(game,other.x,other.y));
						break;
					case 1:
						game.dropGold((int)other.x,(int)other.y);
						break;
					}
					
					Game.soundManager.playEffect(Game.SOUND_BOMB_BOX);
				}
			} else if(other instanceof EnemyTank || other instanceof Turret || other instanceof MyTank){
				if(!other.died){
					other.died = true;
					Game.addEntity(new BombEffect_basic(game,other.x,other.y));
					Game.soundManager.playEffect(Game.SOUND_BOMB_TANK);
					Game.numEnemy--;
					
					if(!(other instanceof MyTank)){
						game.dropGold((int)other.x,(int)other.y);
					}
				}
			} else {
				Game.addEntity(new BombEffect_basic(game,other.x,other.y));
				Game.soundManager.playEffect(Game.SOUND_BOMB_BRICK);
			}
		}
	}
}
