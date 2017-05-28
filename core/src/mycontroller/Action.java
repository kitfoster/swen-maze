package mycontroller;

public class Action {
	
	public static enum Move {LEFT_TURN, RIGHT_TURN, FORWARD, REVERSE, LEFT_3_PT_TURN, RIGHT_3_PT_TURN};
	private Move move;
	
	public Move getMove() {
		return move;
	}
	public void setMove(Move move) {
		this.move = move;
	}
}
