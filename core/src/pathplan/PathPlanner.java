package pathplan;

import map.MapAnalyser;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;

public interface PathPlanner {
	 abstract Route findNewRoute(Car car, MapAnalyser mapAnalyser, Coordinate wallCoord);
	 abstract Boolean isLogicalAddition(Coordinate[] additionalCoord, Route route);

}
