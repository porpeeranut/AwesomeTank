
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
	protected int numPathToActive;
	
	public EnemyTank(Game ingame) {
		super(ingame);
		max_time = 2000+(new Random().nextInt(10)*300);
		maxHP = HP;
		//init no mission to this tank
		is_walk = false;
		wait_time = 0;
		if(this instanceof EnemyMinigun || this instanceof EnemyShotgun
				|| this instanceof EnemyCannon || this instanceof EnemyRocket)
			numPathToActive = 4;
		else
			numPathToActive = 5;
	}
	
	public void move(path_map map ,long delta ,float playerX ,float playerY){
		//set map
		this.map = map;
		setPath(playerX,playerY);
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
					
					if(this instanceof EnemyMinigun || this instanceof EnemyShotgun
							|| this instanceof EnemyCannon || this instanceof EnemyRocket)
						is_walk = true;
				}	
			}
			
			if( is_walk == true ){
				walk(delta);	
			}
		}
	}
	
	private void walk(long delta){
		/*
		 *if walk is actived ,this will handle how to go ,when to stop   
		 */
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
			is_walk = false;
		}
		
		//handle when mission has completed 
		if(is_walk == false){
			is_mission = false;
		
		}
	}
	
	public void rotate_gun(float targetX ,float targetY){
		//enemy gun rotation 
        float dx = this.x - targetX;
        float dy = this.y - targetY;
        float enemy_gunRotation = (float) (180*Math.atan2(dy, dx)/Math.PI);
    	this.setGunAngle(enemy_gunRotation);
	}
	
	@Override
	public void collidedWith(Entity other) {
		if(other instanceof MyBullet){
			shoted = true;
			numPathToActive = 6;
			Game.soundManager.playEffect(Game.SOUND_SHOT_TANK);
		}

		if(!(other instanceof Bullet) && !(other instanceof Effect) 
				&& !(other instanceof Gold)  && !(other instanceof HPpotion)){
			if(this instanceof EnemyMinigun || this instanceof EnemyShotgun
					|| this instanceof EnemyCannon || this instanceof EnemyRocket)
				//moveBack();
			//is_walk = false;
			is_wait  = true;
			max_waitTime = (new Random().nextInt(2))*300;
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
	
	public void setPath(float playerX ,float playerY){
		Path tmp_path = new AStarPathFinder(map, numPathToActive, false).findPath(new UnitMover() 
		,(int) (this.x/map.TILE_SIZE) 
		,(int) (this.y/map.TILE_SIZE)
		,(int) playerX/map.TILE_SIZE 
		,(int) playerY/map.TILE_SIZE
		);
		if(tmp_path != null){
			tmp_path.removeFromLast(3);
			//set enemy_gun angle HERE!
			rotate_gun(playerX, playerY);
			if(tmp_path.getLength() < numPathToActive){
				float initBulletX = (float)(this.x-Math.cos(0.0174532925*gunAngle)*this.width/1.5);
        		float initBulletY = (float)(this.y-Math.sin(0.0174532925*gunAngle)*this.height/1.5);
        		
				Fire(initBulletX,initBulletY,this.gunAngle);
			}
		}

		if(is_mission == false ){
			this.path = tmp_path;
		}
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
	
	private void manage_time(long delta){
		time += (int) (delta);
		if( time > max_time){                                               
			time = 0;
		}
	}
	

}

