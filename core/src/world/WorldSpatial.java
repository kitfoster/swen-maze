package world;

/**
 * Defines the degrees that you can refer to if you want to create your
 * own code for turningithe car.
 *
 */
public class WorldSpatial {
	
	public enum Direction { EAST, WEST, SOUTH, NORTH}
	
	public static enum RelativeDirection { LEFT, RIGHT };
	
	public static Direction getDirection(RelativeDirection relDirect, Direction direct){
		
		if( relDirect.equals(WorldSpatial.RelativeDirection.LEFT)){
			if(direct.equals(WorldSpatial.Direction.EAST)){
				return WorldSpatial.Direction.NORTH;
			}else if(direct.equals(WorldSpatial.Direction.WEST)){
				return WorldSpatial.Direction.SOUTH;
			}else if(direct.equals(WorldSpatial.Direction.NORTH)){
				return WorldSpatial.Direction.WEST;
			}else if(direct.equals(WorldSpatial.Direction.SOUTH)){
				return WorldSpatial.Direction.EAST;
			}
			
		}else{
			if(direct.equals(WorldSpatial.Direction.EAST)){
				return WorldSpatial.Direction.SOUTH;
			}else if(direct.equals(WorldSpatial.Direction.WEST)){
				return WorldSpatial.Direction.NORTH;
			}else if(direct.equals(WorldSpatial.Direction.NORTH)){
				return WorldSpatial.Direction.EAST;
			}else if(direct.equals(WorldSpatial.Direction.SOUTH)){
				return WorldSpatial.Direction.WEST;
			}
		}
		return null;		
	}
	
	public final static int EAST_DEGREE_MIN = 0;
	public final static int EAST_DEGREE_MAX = 360;
	public final static int NORTH_DEGREE = 90;
	public final static int WEST_DEGREE = 180;
	public final static int SOUTH_DEGREE = 270;
}
