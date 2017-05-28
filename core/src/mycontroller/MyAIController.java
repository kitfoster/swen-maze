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
import world.WorldSpatial;

public class MyAIController extends CarController{
	
	public static enum State {START, EXPLORE, REROUTING, SOLVING};
	
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
	private float delta = 150f;
	

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
		
		// get new route if the current route is out of steps, or if the route is going into a wall/trap
		if (currentRoute.getPath().getFirst() == null || mapAnalyser.getBlockingAt(1, car, direction) == null) {
			//currentRoute = pathPlanner.findNewRoute(car, mapAnalyser, coor)
		}
		else {
			excecuteNextAction(actionsToExcecute);
		}
		
		
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
