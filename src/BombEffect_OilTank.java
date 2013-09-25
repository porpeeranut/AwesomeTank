import java.util.Random;

import org.newdawn.slick.opengl.Texture;


public class BombEffect_OilTank extends Effect{
	
	private Texture[] bombEffect = new Texture[6];
	private long lastFrameChange;
	private long frameDuration = 50;
	private int frameNumber;
	public int attack = 60;

	public BombEffect_OilTank(Game ingame,float x, float y) {
		game = ingame;
		this.x = x;
		this.y = y;
		for(int i = 1;i <= 6;i++){
			bombEffect[i-1] = loadTexture("BombEffect_1/"+i+".png");
		}
		width = (int)(ingame.map.TILE_SIZE*3);
        height = (int)(ingame.map.TILE_SIZE*3);
		halfSize = width/2;
	}
	
	public void move(long delta) {
		lastFrameChange += delta;
		if (lastFrameChange > frameDuration) {
			lastFrameChange = 0;
			frameNumber++;
			if (frameNumber >= bombEffect.length) {
				game.removeEntity(this);
			}
		}
	}
	
	public void draw() {
        super.draw(bombEffect[frameNumber]);
	}

	@Override
	public void collidedWith(Entity other) {
		int amount = new Random().nextInt(15) + 5;	// random 5 to 15
		int i;
		if (other instanceof Bullet 
				|| other instanceof Effect
				|| other instanceof HPpotion
				|| other instanceof Gold) {
			return;
		}
		if(!other.touchedBombEffect){
			other.touchedBombEffect = true;
			other.damage(attack);
		}
		if(other.getHP() <= 0){
			game.removeEntity(other);
			if(other instanceof BombWall){
				if(!((BombWall) other).died){
					((BombWall) other).died = true;
					game.addEntity(new BombEffect_BombWall(game,other.x,other.y));
				}
			} else if (other instanceof OilTank){
				if(!((OilTank) other).died){
					((OilTank) other).died = true;
					game.addEntity(new BombEffect_OilTank(game,other.x,other.y));
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
