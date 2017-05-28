package pathplan;

import java.util.ArrayList;

import map.MapAnalyser;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

public class WallPathPlanner implements PathPlanner{
	public enum RelativeDirection {Parallel, Prependicular}

	private int lengthLimit =5;
	
	@Override
	public Route findNewRoute(Car car, MapAnalyser mapAnalyser, Coordinate wallCoord) {
		// TODO Auto-generated method stub
		ArrayList<Coordinate> coordToFollow = new ArrayList<>();
		Coordinate carCoord = new Coordinate(car.getPosition());
		int x = carCoord.x;
		int y = carCoord.y;
		
		switch(car.getOrientation()){
		case EAST:
			x+=1;					
			break;
		case WEST:
			x-=1;
			break;
		case NORTH:
			y-=1;
			break;
		case SOUTH:
			y+=1;
			break;
		default:
			break;
		}
		coordToFollow.add(new Coordinate(x,y));
		
		int i=0,j=0;
		WorldSpatial.Direction directionOfWall=null;
		if(mapAnalyser.checkWall(car, WorldSpatial.getDirection(WorldSpatial.RelativeDirection.LEFT, car.getOrientation()))){
			
			switch( WorldSpatial.getDirection(WorldSpatial.RelativeDirection.RIGHT, car.getOrientation())){
			case EAST:
				i+=1;					
				break;
			case WEST:
				i-=1;
				break;
			case NORTH:
				j-=1;
				break;
			case SOUTH:
				j+=1;
				break;
			default:
				break;
			}
			
		}
		
		
		
		return null;
	}
	

	@Override
	public Boolean isLogicalAddition(Coordinate[] additionalCoord, Route route) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
