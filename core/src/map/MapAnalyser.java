package map;

import java.util.HashMap;


import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.World;
import world.WorldSpatial;

public class MapAnalyser<Cooridnate> {
	int wallSensitivity = 3;
	
	HashMap<Coordinate, MapTile> map = new HashMap<>();
	
	Coordinate wallCoord;
	
	public void update(HashMap<Coordinate, MapTile> submap){
		for(Coordinate key: submap.keySet()){
			if(!map.containsKey(key))
				map.put(key, submap.get(key));		
		}
	}
	
	public Coordinate getWallCoord(){
		return this.wallCoord;
	}
	
	public boolean checkWall(Car car, WorldSpatial.Direction direction){
		int i=0,j=0;
		switch(direction){
		case NORTH:
			j++;
			break;
		case SOUTH:
			j--;
			break;
		case WEST:
			i--;
			break;
		case EAST:
			i++;
			break;
		default:
			break;		
		}
		
		System.out.println("Current Orientation: "+ direction);
		
		Coordinate currentPos = new Coordinate( car.getPosition());
		
		int x=currentPos.x+i, y=currentPos.y+j;
		for(int k=0; k<wallSensitivity; k++){
			
			Coordinate coor = new Coordinate(x,y);
	
			if( !map.containsKey(coor))
				continue;
			if(map.get(coor).getName().equals(World.WALL)){
				System.out.println("MapAnalyser:CheckInfront:  There is Wall");
				wallCoord = coor;
				return true;
			}
			x+=i;

		}
		
		for(int k=0; k<wallSensitivity; k++ ){
			Coordinate coor = new Coordinate(x,y);
			if( !map.containsKey(coor))
				continue;
			
			if(map.get(coor).getName().equals(World.WALL)){
				System.out.println("MapAnalyser:CheckInfront:  There is Wall");
				wallCoord = coor;
				return true;
			}
			y+=j;
		}
		
		System.out.println("MapAnalyser:CheckInfront:  There is NO Wall");
		return false;	
	}
	
	public boolean hasBeen(Coordinate coord){
		if(map.containsKey(coord))
			return true;
		else
			return false;	
	}
	
	
private boolean checkFollowingWall(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView) {
		
		switch(orientation){
		case EAST:
			return checkNorth(currentView);
		case NORTH:
			return checkWest(currentView);
		case SOUTH:
			return checkEast(currentView);
		case WEST:
			return checkSouth(currentView);
		default:
			return false;
		}
		
	}
	
	
	
}
