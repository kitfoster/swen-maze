package mycontroller;

import java.util.ArrayList;

import controller.CarController;
import map.MapAnalyser;
import map.PotentialRoom;
import pathplan.Action;
import pathplan.PathPlanner;
import pathplan.ReRouter;
import pathplan.Route;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;

public class MyAIController extends CarController{
	
	public enum State {START, EXPLORE, REROUTING, SOLVING};
	public enum Direction {NORTH, SOUTH, EAST, WEST};
	
	private MapAnalyser map = new MapAnalyser();
	private PathPlanner pathPlanner;
	private Coordinate destination;
	private PotentialRoom currentRoom;
	private ReRouter reRouter;
	private Route currentRoute;
	private Direction direction;
	private ArrayList<Action> actionsToExcecute = new ArrayList<Action>();
	

	public MyAIController(Car car) {
		super(car);
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	public void update(float delta, MapTile[][] tiles) {
		// TODO Auto-generated method stub
		
	}
	
	public Coordinate findDestTile() {
		return destination;
	}
	
	private void updateDestination(Coordinate destination) {
		this.destination = destination;
	}
	
	private void excecuteNextAction(ArrayList<Action> actions) {
		
	}
	
	private void setPathPlanner(PathPlanner pp) {
		this.pathPlanner = pp;
	}

}
