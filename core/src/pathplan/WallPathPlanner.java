package pathplan;

import java.util.ArrayList;

import map.MapAnalyser;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.World;
import world.WorldSpatial;

public class WallPathPlanner implements PathPlanner{
	public enum RelativeDirection {Parallel, Prependicular}

	private int lengthLimit =5;
	
	@Override
	public Route findNewRoute(Car car, MapAnalyser mapAnalyser, Coordinate wallCoord) {
		// TODO Auto-generated method stub
		
		System.out.println("Creating a new Path");
		
		
		
		
		ArrayList<Coordinate> coordToFollow = new ArrayList<>();
		Coordinate carCoord = new Coordinate(car.getPosition());
		int x = carCoord.x;
		int y = carCoord.y;
		int a=0,b=0;
		switch(car.getOrientation()){
		case EAST:
			x+=1;		
			a+=1;
			break;
		case WEST:
			x-=1;
			a-=1;
			break;
		case NORTH:
			y+=1;
			b+=1;
			break;
		case SOUTH:
			y-=1;
			b-=1;
			break;
		default:
			break;
		}
		coordToFollow.add(new Coordinate(x,y));
		
		
		if(wallCoord == null){
			for(int i =0;i<lengthLimit;i++){
				x+=a;
				y+=b;
				coordToFollow.add(new Coordinate(x,y));
			}
			
		}else{
		
			int i=0,j=0;
			WorldSpatial.RelativeDirection relDirect = null;
			WorldSpatial.Direction turnedDirection = WorldSpatial.getDirection(WorldSpatial.RelativeDirection.LEFT, car.getOrientation());
			if(mapAnalyser.isBlockingWithin(2,car, turnedDirection,World.WALL)||mapAnalyser.isBlockingWithin(2,car, turnedDirection,World.TRAP)){
				relDirect=WorldSpatial.RelativeDirection.RIGHT;
			}else{
				relDirect=WorldSpatial.RelativeDirection.LEFT;
			}
			
			switch( WorldSpatial.getDirection(relDirect, car.getOrientation())){
			case EAST:
				i+=1;					
				break;
			case WEST:
				i-=1;
				break;
			case NORTH:
				j+=1;
				break;
			case SOUTH:
				j-=1;
				break;
			default:
				break;
			}
			for(int k=0;k<lengthLimit;k++){
				x+=i;
				y+=j;
				coordToFollow.add(new Coordinate(x,y));
			}
			
			/*if(i!=0){
				
			}else{
				for(int k=0;k<lengthLimit;k++){
					y+=j;
					coordToFollow.add(new Coordinate(x,y));
				}
			}*/
		}
		
		
		
		
		return new Route(coordToFollow);
	}
	

	@Override
	public Boolean isLogicalAddition(Coordinate[] additionalCoord, Route route) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
