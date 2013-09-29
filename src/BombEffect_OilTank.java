import java.util.Random;

import org.newdawn.slick.opengl.Texture;


public class BombEffect_OilTank extends Effect{
	
	private Texture[] bombEffect = new Texture[14];
	private long lastFrameChange;
	private long frameDuration = 50;
	private int frameNumber;
	public int attack = 30;

	public BombEffect_OilTank(Game ingame,float x, float y) {
		game = ingame;
		this.x = x;
		this.y = y;
		for(int i = 1;i <= bombEffect.length;i++){
			bombEffect[i-1] = Game.loadTexture("BombEffect_1/big/"+i+".png");
		}
		width = (int)(game.map.TILE_SIZE*3);
        height = (int)(game.map.TILE_SIZE*3);
		halfSize = width/2;
	}
	
	public void move(long delta) {
		lastFrameChange += delta;
		if (lastFrameChange > frameDuration) {
			lastFrameChange = 0;
			frameNumber++;
			if (frameNumber >= bombEffect.length) {
				Game.removeEntity(this);
			}
		}
	}
	
	public void draw() {
        super.draw(bombEffect[frameNumber]);
	}

	@Override
	public void collidedWith(Entity other) {
		if (other instanceof Bullet 
				|| other instanceof Effect
				|| other instanceof HPpotion
				|| other instanceof Gold) {
			return;
		}
		if(!other.touchedBombEffect && frameNumber < 5){
			other.touchedBombEffect = true;
			other.damage(attack);
		}
		if (frameNumber >= bombEffect.length) {
			System.out.println("asd");
			other.touchedBombEffect = false;
		}
		if(other.getHP() <= 0){
			Game.removeEntity(other);
			if(other instanceof BombWall){
				if(!((BombWall) other).died){
					((BombWall) other).died = true;
					Game.addEntity(new BombEffect_BombWall(game,other.x,other.y));
				}
			} else if (other instanceof OilTank){
				if(!((OilTank) other).died){
					((OilTank) other).died = true;
					Game.addEntity(new BombEffect_OilTank(game,other.x,other.y));
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
