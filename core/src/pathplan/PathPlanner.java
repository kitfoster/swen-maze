package pathplan;

import map.MapAnalyser;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

public interface PathPlanner {
	 abstract Route findNewRoute(Car car, MapAnalyser mapAnalyser, Coordinate wallCoord, WorldSpatial.RelativeDirection relDirec);
	 abstract Boolean isLogicalAddition(Coordinate[] additionalCoord, Route route);

}
