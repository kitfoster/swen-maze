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

	private int lengthLimit =16;
	
	@Override
	public Route findNewRoute(Car car, MapAnalyser mapAnalyser, Coordinate wallCoord, WorldSpatial.RelativeDirection turn) {
		// TODO Auto-generated method stub
		
		System.out.println("Creating a new Path");
		WorldSpatial.Direction carDirection = car.getOrientation();
		if(turn!=null){
			carDirection  = WorldSpatial.getDirection(turn, carDirection);
		}
		
		System.out.println("The direction of me: " + carDirection);
		
		
		
		ArrayList<Coordinate> coordToFollow = new ArrayList<>();
		Coordinate carCoord = new Coordinate(car.getPosition());
		int x = carCoord.x;
		int y = carCoord.y;
		int a=0,b=0;
		/*switch(carDirection){
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
		coordToFollow.add(new Coordinate(x,y));*/
		
		
		if(wallCoord == null){
			System.out.println("WallPathPlanner: No blocking infront");
			for(int i =0;i<lengthLimit;i++){
				
				coordToFollow.add(new Coordinate(x,y));
				x+=a;
				y+=b;
			}
			
		}else{
			
			
			
			System.out.println("WallPathPlanner: Blocking Infront" + mapAnalyser.getTileName(wallCoord));
			System.out.println("The direction of me: " + carDirection);
			int i=0,j=0;
			WorldSpatial.RelativeDirection relDirect = null;
			WorldSpatial.Direction turnedDirection = WorldSpatial.getDirection(WorldSpatial.RelativeDirection.LEFT, carDirection);
			//Check if the direction I turn in has blockings infront of me
			if(mapAnalyser.isBlockingWithin(2,car, turnedDirection)){
				System.out.println("Turn Right");
				relDirect=WorldSpatial.RelativeDirection.RIGHT;
			}else{
				System.out.println("Turn Left");
				relDirect=WorldSpatial.RelativeDirection.LEFT;
			}
			//The following path I will take
			switch( WorldSpatial.getDirection(relDirect,carDirection )){
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
				
				coordToFollow.add(new Coordinate(x,y));
				x+=i;
				y+=j;
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
