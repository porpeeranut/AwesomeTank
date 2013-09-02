import org.newdawn.slick.opengl.Texture;


public class BombEffect_OilTank extends Entity {
	
	private Texture[] bombEffect = new Texture[6];
	private long lastFrameChange;
	private long frameDuration = 50;
	private int frameNumber;
	public int attack = 60;;

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
		if (other instanceof Bullet
				|| other instanceof BulletShotEffect
				|| other instanceof BombEffect_basic
				|| other instanceof BombEffect_BombWall
				|| other instanceof BombEffect_BigBullet) {
			return;
		}
		other.damage(attack);
		game.removeEntity(other);
		if(other.getHP() <= 0){
			game.removeEntity(other);
			if(other instanceof BombWall){
				game.addEntity(new BombEffect_BombWall(game,other.x,other.y));
				if(!((BombWall) other).died){
					((BombWall) other).died = true;
				}
			} 
			else {
				game.addEntity(new BombEffect_basic(game,other.x,other.y));
			}
			if(other instanceof EnemyTank){
				if(!((EnemyTank) other).died){
					((EnemyTank) other).died = true;
					game.numEnemy--;
				}
			}
			if(other instanceof Turret){
				if(!((Turret) other).died){
					((Turret) other).died = true;
					game.numEnemy--;
				}
			}
		}
	}
}
