package map;

import java.util.HashMap;


import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.World;
import world.WorldSpatial;

public class MapAnalyser<Cooridnate> {
	int wallSensitivity = 2;
	
	HashMap<Coordinate, MapTile> map = new HashMap<>();
	
	Coordinate wallCoord;
	
	public void update(HashMap<Coordinate, MapTile> submap){
		for(Coordinate key: submap.keySet()){
			if(!map.containsKey(key))
				map.put(key, submap.get(key));		
		}
	}
	
	public Coordinate getWallCoord(){
		Coordinate coord = wallCoord;
		wallCoord =null;
		
		return  coord;
	}
	
	//Returns null if there is no blocking
	public Coordinate getBlockingAt(int dist, Car car, WorldSpatial.Direction direction){
		Coordinate checkCoord = getCoordinateAt(dist, car, direction);
		if(map.containsKey(checkCoord)&& (map.get(checkCoord).getName().equals(World.WALL)||
				map.get(checkCoord).getName().equals(World.TRAP))){
			return checkCoord;
		}
		return null;
	}
	
	//Get the coordinate of the cell at distance: dist from the car in direction: Direction
	Coordinate getCoordinateAt(int dist, Car car ,WorldSpatial.Direction direction){
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
		
		Coordinate carCoord = new Coordinate(car.getPosition());
		Coordinate coord = new Coordinate(carCoord.x+i*dist, carCoord.y+j*dist);
		return coord;
	}
	
	//Is there blocking of type  Trap within the distance
	
	public boolean isBlockingWithin(int dist, Car car, WorldSpatial.Direction direction, String trap){
		
		for(int i =1;i<= dist;i++){
			Coordinate checkCoord = getCoordinateAt(i, car, direction);
			if(map.containsKey(checkCoord)&& map.get(checkCoord).getName().equals(trap)){
				return true;
			}
		}
		return false;
		
	}
	
	
	public Coordinate getWallInfront(){
		return wallCoord;
	}
	
	public boolean hasBeen(Coordinate coord){
		if(map.containsKey(coord))
			return true;
		else
			return false;	
	}
	
}
