package pathplan;

import java.util.ArrayList;

import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;
import world.WorldSpatial.Direction;
import world.WorldSpatial.RelativeDirection;

public class Route {
	ArrayList<Coordinate> path;
	private int steps=0;
	private int curStep=0;
	private boolean finished=false;
	private WorldSpatial.Direction direction;
	Route(ArrayList<Coordinate> path){
		this.path=path;
		this.steps = path.size();
	}

	private WorldSpatial.Direction findPathDirection(){

		int step = curStep;
		int thisX = path.get(step).x, thisY = path.get(step).y;
		step++;
		int nextX = path.get(step).x, nextY = path.get(step).y;

		int diffX = nextX -thisX, diffY = nextY-thisY;
		WorldSpatial.Direction direction = null;

		if(diffX>0){
			direction = WorldSpatial.Direction.EAST;
		}else if(diffX <0){
			direction = WorldSpatial.Direction.WEST;
		}
		if(diffY<0){
			direction = WorldSpatial.Direction.SOUTH;
		}else if (diffY>0){
			direction = WorldSpatial.Direction.NORTH;
		}
		return direction;
	}



	private boolean doesDirectionAlign(Car car, WorldSpatial.Direction direction) throws Exception{
		if(direction ==null){
			//System.out.println("Something is wrong");
			throw new Exception("Wrong");
		}

		if(car.getOrientation().equals(direction)){
			//System.out.println("Car Orientation: "+car.getOrientation() + "Path Orientation" + direction);
			return true;
		}else{
			//System.out.println("WRONG Car Orientation: "+car.getOrientation() + "Path Orientation" + direction);
			return false;
		}

	}



	public WorldSpatial.RelativeDirection getTurningDirection(Car car) throws Exception{
		Coordinate carCoord = new Coordinate(car.getPosition());
		if(carCoord.equals(path.get(curStep))){
			curStep++;
		}else{
			//System.out.println("Car coordinate: "+ carCoord.toString()+ "Next Tile Coordinate: "+ path.get(curStep).toString());
		}


		WorldSpatial.Direction direction = findPathDirection();

		if(!doesDirectionAlign(car,direction)){

			WorldSpatial.RelativeDirection turn = WorldSpatial.RelativeDirection.LEFT;

			if(WorldSpatial.getDirection(turn, car.getOrientation()).equals(direction)){
				//System.out.println("Direction To Turn: "+turn);
				return turn;
			}else{
				turn = WorldSpatial.RelativeDirection.RIGHT;
				//System.out.println("Direction To Turn: "+turn);
				return turn;
			}
		}
		return null;

	}


	Coordinate getNextStep(){
		steps++;
		return path.get(steps-1);

	}

	public boolean isRouteDone(Car car){
		Coordinate end = path.get(path.size()-1);
		Coordinate carCoord= new Coordinate(car.getPosition());
		if(end.equals(carCoord)){
			return true;
		}else{
			return false;
		}
	}

	public ArrayList<Coordinate> getPath(){
		return path;
	}

	public boolean isDone(){
		return finished;
	}

	public void printRoute(){
		System.out.println("New Route");
		for(int i=0;i<steps;i++){
			System.out.println(path.get(i).toString());
		}

	}



}
