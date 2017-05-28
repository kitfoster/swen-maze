package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import controller.CarController;
import controller.AIController.State;
import map.MapAnalyser;
import map.PotentialRoom;
import pathplan.Action;
import pathplan.Action.Move;
import pathplan.PathPlanner;
import pathplan.ReRouter;
import pathplan.Route;
import pathplan.WallPathPlanner;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

public class MyAIController extends CarController{

	

	public enum State {Start, Planning, Going};
	public static enum Direction {NORTH, SOUTH, EAST, WEST};
	
	private MapAnalyser mapAnalyser = new MapAnalyser();
	private PathPlanner pathPlanner;
	private Coordinate destination;
	private Coordinate position;
	private PotentialRoom currentRoom;
	private ReRouter reRouter;
	private Route currentRoute;
	private WorldSpatial.Direction direction;
	private LinkedList<Action> actionsToExcecute = new LinkedList<Action>();
	private Action nextAction;
	private Car car;
	// How many minimum units the wall is away from the player.
	private int wallSensitivity = 2;
	
	public State state= State.Start;
	
	private boolean isFollowingWall = false; // This is initialized when the car sticks to a wall.
	private WorldSpatial.RelativeDirection lastTurnDirection = null; // Shows the last turn direction the car takes.
	private boolean isTurningLeft = false;
	private boolean isTurningRight = false; 
	private WorldSpatial.Direction previousState = null; // Keeps track of the previous state

	// Car Speed to move at
	private final float CAR_SPEED = 1.5f;
	private PathPlanner planner= new WallPathPlanner();
	private Coordinate tileToAvoid = null;
	
	// Offset used to differentiate between 0 and 360 degrees
	private int EAST_THRESHOLD = 3;
	private float delta = 150f;
	
	public MyAIController(Car car) {
		super(car);
		// TODO Auto-generated constructor stub
	}
	
	private pathplan.Route curRoute;
	
	

	@Override
	public void update(float delta) {
		
		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = getView();
		
		mapAnalyser.update(currentView);
		
		checkStateChange();
		


		// If you are not following a wall initially, find a wall to stick to!
		switch (this.state){
		case Start:
			if(getVelocity() < CAR_SPEED){
				applyForwardAcceleration();
			}
			
			if(mapAnalyser.isBlockingWithin(2, car, car.getOrientation())){
				// Turn right until we go back to east!
					this.state = State.Planning;
					tileToAvoid =mapAnalyser.getBlockingAt(2, car, car.getOrientation());
			}
			break;
		case Planning:
			
			
			curRoute = planner.findNewRoute(car, mapAnalyser,tileToAvoid,lastTurnDirection);
			tileToAvoid=null;
			this.state= State.Going;
			curRoute.printRoute();
			break;
		case Going:
			try{
				
				if( curRoute.getTurningDirection(car).equals(WorldSpatial.RelativeDirection.RIGHT)){
					if(curRoute.willBeOnTrack(car, WorldSpatial.RelativeDirection.RIGHT, delta)){
						curRoute.willBeOnTrack(car, WorldSpatial.RelativeDirection.RIGHT, delta);
						applyRightTurn(getOrientation(),delta);
						
					}
					isTurningRight=  true;
					lastTurnDirection= WorldSpatial.RelativeDirection.RIGHT;
					
					
				}else if(curRoute.getTurningDirection(car).equals(WorldSpatial.RelativeDirection.LEFT)){
					if(curRoute.willBeOnTrack(car, WorldSpatial.RelativeDirection.LEFT, delta)){
						curRoute.willBeOnTrack(car, WorldSpatial.RelativeDirection.LEFT, delta);
						applyLeftTurn(getOrientation(),delta);
						
					}
					isTurningLeft = true;
					lastTurnDirection= WorldSpatial.RelativeDirection.LEFT;
					
				}else {
					System.out.println("NOT Still Turning");
					isTurningLeft = false;
					isTurningRight = false;
					lastTurnDirection = null;
					applyForwardAcceleration();
				}
				
			}catch (Exception e){
			}


			
			readjust(lastTurnDirection,delta);
			if(curRoute.isRouteDone(car)){
				this.state=State.Planning;
			}
			if(curRoute.isRouteBlocked(mapAnalyser)){
				tileToAvoid = curRoute.getBlockedTile(mapAnalyser);
				this.state=State.Planning;
			}
			
			if(getVelocity() < CAR_SPEED){
				applyForwardAcceleration();
			}
			break;
		
		
		
		}


	}
	
	/**
	 * Readjust the car to the orientation we are in.
	 * @param lastTurnDirection
	 * @param delta
	 */
	private void readjust(WorldSpatial.RelativeDirection lastTurnDirection, float delta) {
		if(lastTurnDirection != null){
			if(!isTurningRight && lastTurnDirection.equals(WorldSpatial.RelativeDirection.RIGHT)){
				adjustRight(getOrientation(),delta);
			}
			else if(!isTurningLeft && lastTurnDirection.equals(WorldSpatial.RelativeDirection.LEFT)){
				adjustLeft(getOrientation(),delta);
			}
		}
		
	}
	
	/**
	 * Try to orient myself to a degree that I was supposed to be at if I am
	 * misaligned.
	 */
	private void adjustLeft(WorldSpatial.Direction orientation, float delta) {
		
		switch(orientation){
		case EAST:
			if(getAngle() > WorldSpatial.EAST_DEGREE_MIN+EAST_THRESHOLD){
				turnRight(delta);
			}
			break;
		case NORTH:
			if(getAngle() > WorldSpatial.NORTH_DEGREE){
				turnRight(delta);
			}
			break;
		case SOUTH:
			if(getAngle() > WorldSpatial.SOUTH_DEGREE){
				turnRight(delta);
			}
			break;
		case WEST:
			if(getAngle() > WorldSpatial.WEST_DEGREE){
				turnRight(delta);
			}
			break;
			
		default:
			break;
		}
		
	}

	private void adjustRight(WorldSpatial.Direction orientation, float delta) {
		switch(orientation){
		case EAST:
			if(getAngle() > WorldSpatial.SOUTH_DEGREE && getAngle() < WorldSpatial.EAST_DEGREE_MAX){
				turnLeft(delta);
			}
			break;
		case NORTH:
			if(getAngle() < WorldSpatial.NORTH_DEGREE){
				turnLeft(delta);
			}
			break;
		case SOUTH:
			if(getAngle() < WorldSpatial.SOUTH_DEGREE){
				turnLeft(delta);
			}
			break;
		case WEST:
			if(getAngle() < WorldSpatial.WEST_DEGREE){
				turnLeft(delta);
			}
			break;
			
		default:
			break;
		}
		
	}
	
	/**
	 * Checks whether the car's state has changed or not, stops turning if it
	 *  already has.
	 */
	private void checkStateChange() {
		if(previousState == null){
			previousState = getOrientation();
		}
		else{
			if(previousState != getOrientation()){
				if(isTurningLeft){
					isTurningLeft = false;
				}
				if(isTurningRight){
					isTurningRight = false;
				}
				previousState = getOrientation();
			}
		}
	}
	
	/**
	 * Turn the car counter clock wise (think of a compass going counter clock-wise)
	 */
	private void applyLeftTurn(WorldSpatial.Direction orientation, float delta) {
		switch(orientation){
		case EAST:
			if(!getOrientation().equals(WorldSpatial.Direction.NORTH)){
				turnLeft(delta);
			}
			break;
		case NORTH:
			if(!getOrientation().equals(WorldSpatial.Direction.WEST)){
				turnLeft(delta);
			}
			break;
		case SOUTH:
			if(!getOrientation().equals(WorldSpatial.Direction.EAST)){
				turnLeft(delta);
			}
			break;
		case WEST:
			if(!getOrientation().equals(WorldSpatial.Direction.SOUTH)){
				turnLeft(delta);
			}
			break;
		default:
			break;
		
		}
		
	}
	
	/**
	 * Turn the car clock wise (think of a compass going clock-wise)
	 */
	private void applyRightTurn(WorldSpatial.Direction orientation, float delta) {
		switch(orientation){
		case EAST:
			if(!getOrientation().equals(WorldSpatial.Direction.SOUTH)){
				turnRight(delta);
			}
			break;
		case NORTH:
			if(!getOrientation().equals(WorldSpatial.Direction.EAST)){
				turnRight(delta);
			}
			break;
		case SOUTH:
			if(!getOrientation().equals(WorldSpatial.Direction.WEST)){
				turnRight(delta);
			}
			break;
		case WEST:
			if(!getOrientation().equals(WorldSpatial.Direction.NORTH)){
				turnRight(delta);
			}
			break;
		default:
			break;
		
		}
		
	}

	/**
	 * Check if you have a wall in front of you!
	 * @param orientation the orientation we are in based on WorldSpatial
	 * @param currentView what the car can currently see
	 * @return
	 */
	private boolean checkWallAhead(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView){
		switch(orientation){
		case EAST:
			return checkEast(currentView);
		case NORTH:
			return checkNorth(currentView);
		case SOUTH:
			return checkSouth(currentView);
		case WEST:
			return checkWest(currentView);
		default:
			return false;
		
		}
	}
	
	/**
	 * Check if the wall is on your left hand side given your orientation
	 * @param orientation
	 * @param currentView
	 * @return
	 */
	/**
	 * Method below just iterates through the list and check in the correct coordinates.
	 * i.e. Given your current position is 10,10
	 * checkEast will check up to wallSensitivity amount of tiles to the right.
	 * checkWest will check up to wallSensitivity amount of tiles to the left.
	 * checkNorth will check up to wallSensitivity amount of tiles to the top.
	 * checkSouth will check up to wallSensitivity amount of tiles below.
	 */
	public boolean checkEast(HashMap<Coordinate, MapTile> currentView){
		// Check tiles to my right
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x+i, currentPosition.y));
			if(tile.getName().equals("Wall")){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkWest(HashMap<Coordinate,MapTile> currentView){
		// Check tiles to my left
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x-i, currentPosition.y));
			if(tile.getName().equals("Wall")){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkNorth(HashMap<Coordinate,MapTile> currentView){
		// Check tiles to towards the top
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y+i));
			if(tile.getName().equals("Wall")){
				return true;
			}
		}
		return false;
	}
	
	public boolean checkSouth(HashMap<Coordinate,MapTile> currentView){
		// Check tiles towards the bottom
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= wallSensitivity; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y-i));
			if(tile.getName().equals("Wall")){
				return true;
			}
		}
		return false;
	}
	
	
	
	
	public Coordinate findDestTile() {
		
		return destination;
	}
	
	private void updateDestination(Coordinate destination) {
		this.destination = destination;
	}
	
	private void excecuteNextAction(LinkedList<Action> actions) {
		nextAction = actionsToExcecute.getFirst();
		if (nextAction.getMove() == Move.FORWARD) {
			// move forward
			this.car.applyForwardAcceleration();
		}
		if (nextAction.getMove() == Move.REVERSE) {
			// move backwards
			this.car.applyReverseAcceleration();
		}
		if (nextAction.getMove() == Move.LEFT_TURN) {
			// turn left
			this.car.turnLeft(delta);
		}
		if (nextAction.getMove() == Move.RIGHT_TURN) {
			// turn right
			this.car.turnRight(delta);
		}
		if (nextAction.getMove() == Move.LEFT_3_PT_TURN) {
			// left 3 pt turn
			car.applyReverseAcceleration();
			// maybe some sleep(1) or something here
			car.turnLeft(delta/2); // will do backwards 
			car.brake();
			car.applyForwardAcceleration();
			car.turnRight(delta/2);
			car.applyReverseAcceleration();
			car.turnLeft(delta/2); // needs to be facing the right direction here
			car.applyForwardAcceleration();
			
		}
		if (nextAction.getMove() == Move.RIGHT_3_PT_TURN) {
			// right 3 pt turn
			car.applyReverseAcceleration();
			// maybe some sleep(1) or something here
			car.turnRight(delta/2); // will do backwards 
			car.brake();
			car.applyForwardAcceleration();
			car.turnLeft(delta/2);
			car.applyReverseAcceleration();
			car.turnRight(delta/2); // needs to be facing the right direction here
			car.applyForwardAcceleration();
		}
	}
	
	private void setPathPlanner(PathPlanner pp) {
		this.pathPlanner = pp;
	}

}
