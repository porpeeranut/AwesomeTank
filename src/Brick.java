import org.newdawn.slick.opengl.Texture;

	public class Brick extends Entity {
	
	protected Texture brick;	

	public Brick(Game ingame,int hp) {
		game = ingame;
		brick = loadTexture("brick.jpg");
		width = game.map.TILE_SIZE;
        height = game.map.TILE_SIZE;
		halfSize = width/2;
		HP = hp;
	}
	
	public void draw() {
		brick.bind();
		super.draw();
	}

	@Override
	public void collidedWith(Entity other) {
		
	}

}
