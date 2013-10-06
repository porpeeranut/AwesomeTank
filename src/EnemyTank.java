
import java.util.Random;

import pathFinding.*;


public class EnemyTank extends TankEntity {
	
	protected boolean died = false;
	private int to_x,to_y;
	float setAng=0;
	int speed = 2;
	private Path path;
	private boolean is_evade = false;
	private boolean is_walk = false;
	private boolean is_mission = false;
	private int time=0,max_time;
	private boolean active=false;
	private path_map map;
	private boolean is_wait = false;
	private int wait_time;
	private int max_waitTime; 
	
	public EnemyTank(Game ingame) {
		super(ingame);
		max_time = 2000+(new Random().nextInt(10)*300);
		maxHP = HP;
		//init no mission to this tank
		is_walk = false;
		wait_time = 0;
	}
	
	
	public void move(path_map map ,long delta){
		//set map
		this.map = map;
		/* Create one and done mission method
		 * 1. check if path not null and mission is null(previous mission is completed ) if not ,pass to 3. 
		 * 2. create new mission
		 * 3. do mission
		 * check to create mission
		 * [path_1]----[path_2] ,this method is actived
		 */
		//capture time
		if( is_wait == true ){
			wait_time += (int) (delta);
			if( wait_time > max_waitTime){
				wait_time = 0;
				is_wait = false;
			}
			
		}else{
			manage_time(delta);
			if(shoted == true ){
				//evading(map);
				active = true;
				
			}
			
			if(active == true){
				evading(map);
				if(time == 0){
					active = false;
				}
			}
			
			if(path != null && path.getLength()>=2 ){
				//create new mission 
				if( is_mission == false){
					is_mission = true;
					//where is tank going? ,set destiny
					to_x = path.getX(1)*path_map.TILE_SIZE + path_map.TILE_SIZE/2;
					to_y = path.getY(1)*path_map.TILE_SIZE + path_map.TILE_SIZE/2; 
					
					if( this.x  > to_x && Math.abs(to_y - this.y) < path_map.TILE_SIZE/20 ){
						setAng = 0;
					}
						
					if( this.x  < to_x && Math.abs(to_y - this.y) < path_map.TILE_SIZE/20){
						setAng = 180;
					}
						
					if( this.y  > to_y && Math.abs(to_x - this.x) < path_map.TILE_SIZE/20 ){
						setAng = 90;
					}
					if( this.y  < to_y && Math.abs(to_x - this.x) < path_map.TILE_SIZE/20 ){
						setAng = 270;
					}
					
					
					float tmpX = (float) (-speed*Math.cos(Math.PI*setAng/180));
					float tmpY = (float) (-speed*Math.sin(Math.PI*setAng/180));
					super.setDX(tmpX);	
					super.setDY(tmpY);
					
					is_walk = true;
				}	
			}
			
			if( is_walk == true ){
				walking(delta);	
			}
		}
	}
	
	private void walking(long delta){
		/*
		 *if walk is actived ,this will handle how to go ,when to stop   
		 */
		System.out.println(is_mission);
		float preX = this.x;
		float preY = this.y;
		//do mission
		//move to
		super.move(delta, setAng);
		
		if(Math.abs(this.x - preX) < 1.0 && Math.abs(this.y - preY) < 1.0){
			is_walk = false;
		}
		
		//check to complete this mission
		if(Math.abs(to_x - this.x) < path_map.TILE_SIZE/20  && Math.abs(to_y - this.y) < path_map.TILE_SIZE/20  ){
			//System.out.println("#to "+to_x+" "+to_y+" mission is completed");
			is_walk = false;
		}
		
		//handle when mission has completed 
		if(is_walk == false){
			is_mission = false;
		
		}
	
	}
	
	
	public void gun_rotation(float targetX ,float targetY){
		//enemy gun rotation 
        float dx = this.x - targetX;
        float dy = this.y - targetY;
        float enemy_gunRotation = (float) (180*Math.atan2(dy, dx)/Math.PI);
    	this.setGunAngle(enemy_gunRotation);
    	//System.out.println(this.gunAngle);b
  
	}
	
	@Override
	public void collidedWith(Entity other) {
		if(other instanceof Bullet){
			shoted = true;
		}

		if(!(other instanceof Bullet) && !(other instanceof Effect)  && !(other instanceof Gold)){
			moveBack();
			//is_walk = false;
			is_wait  = true;
			max_waitTime = (new Random().nextInt(4))*300;
			//new_moveBack();
			/*
			int cen_x = (int) (this.x/path_map.TILE_SIZE);
			int cen_y = (int) (this.y/path_map.TILE_SIZE);
			this.x = cen_x*path_map.TILE_SIZE + path_map.TILE_SIZE/2;
			this.y = cen_y*path_map.TILE_SIZE + path_map.TILE_SIZE/2;
			*/
		}
	}
	
	public float get_gunAng(){
		return gunAngle;
	}
	
	/* 
	 * <evade> method is designed to handle event when it no path to go ,then its target come to this tank 
	 * 
	 */
	private void evading(path_map map){
		//evade
		int tx = -1;
		int ty = -1;
		int local_x = (int) (this.x/map.TILE_SIZE);
		int local_y = (int) (this.y/map.TILE_SIZE);
		int loopTime = 0;
		//random direction
		if( is_walk == false ){
			do{
				do{
					tx = new Random().nextInt(2);
					if( tx == 0 ){
						tx = -1;
					}else{
						tx = 1;
					}
					tx += local_x;
				}while( tx >= map.getWidthInTiles() || tx < 0);
					
				do{
					ty = new Random().nextInt(2);
					if( ty == 0 ){
						ty = -1;
					}else{
						ty = 1;
					}
					ty += local_y;
				}while( ty >= map.getHeightInTiles() || ty < 0);
				
				loopTime++;
				if(loopTime > 20 ){
					tx = -1;
					ty = -1;
					break;
				}
				
			}while( map.blocked(new UnitMover(), tx, ty) );
			
		}
		if( tx != -1 && ty != -1){
			path = new AStarPathFinder(map,10,false).findPath(new UnitMover(), local_x, local_y, tx, ty);
		}
		
	}
	
	public void setPath(Path path){
		if(is_mission == false ){
			this.path = path;
		}
	}
	
	public float get_setAng(){
		return setAng;
	}
	/*
	private void new_moveBack(Entity other){
		float other_top = other.y- other.height/2;
		float other_bott = other.y + other.height/2;
		float other_right = other.x + other.width/2;
		float other_left = other.x - other.width/2 ;
		
		float me_top = this.y - this.height/2;
		float me_bott = this.y + this.height/2;
		float me_right = this.x + this.width/2;
		float me_left = this.x - this.width/2;
	
		//collide @butt's other
		if( me_top < other_bott ){
			//set this @ edge's other
			this.y = other_bott + this.height/2; 
		}else if( me_bott > other_top ){
			this.y = other_top - this.height/2;
		}
		 
		if( me_left < other_right ){
			this.x = other_right + this.width/2; 
		}else if( me_right > other_left){
			this.x = other_left - this.width/2; 
		}
		
	}*/
	
	private void new_moveBack(){
		
	}
	
	private void manage_time(long delta){
		time += (int) (delta);
		if( time > max_time){                                               
			time = 0;
		}
	}
	

}

