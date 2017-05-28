package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import controller.CarController;
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

public class MyAIController extends CarController{
	
	public static enum State {START, EXPLORE, REROUTING, SOLVING};
	public static enum Direction {NORTH, SOUTH, EAST, WEST};
	
	private MapAnalyser mapAnalyser = new MapAnalyser();
	private PathPlanner pathPlanner;
	private Coordinate destination;
	private Coordinate position;
	private PotentialRoom currentRoom;
	private ReRouter reRouter;
	private Route currentRoute;
	private Direction direction;
	private LinkedList<Action> actionsToExcecute = new LinkedList<Action>();
	private Action nextAction;
	private Car car;
	private float delta;
	

	public MyAIController(Car car) {
		super(car);
		this.car = car;
		pathPlanner = new WallPathPlanner();
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
	}

	public void update(float delta, HashMap<Coordinate, MapTile> tiles) {
		
		this.delta = delta;
		mapAnalyser.update(tiles);
		currentRoute = pathPlanner.findNewRoute(car, mapAnalyser, coor)
		
		
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
		}
		if (nextAction.getMove() == Move.RIGHT_3_PT_TURN) {
			// right 3 pt turn
		}
	}
	
	private void setPathPlanner(PathPlanner pp) {
		this.pathPlanner = pp;
	}

}
