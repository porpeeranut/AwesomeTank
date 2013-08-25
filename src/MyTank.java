
public class MyTank extends TankEntity {

	public MyTank(Map gamemap) {
		map = gamemap;
		gun = loadTexture("gun.png");
		body = loadTexture("body.png");
		width = 40;
        height = 40;
		halfSize = width/2;
	}
	
	@Override
	public void collidedWith(Entity other) {
	}
}