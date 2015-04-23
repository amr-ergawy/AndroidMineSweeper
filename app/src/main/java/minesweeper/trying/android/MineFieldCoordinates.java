package minesweeper.trying.android;

public class MineFieldCoordinates {
	
	// Coordinates.
	private int x;
	private int y;
	
	// Initiate the coordinates.
	public MineFieldCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	// Get the x coordinate.
	public int getX() {
        return x;
	}
	
	// Get the y coordinate.
	public int getY() {
		return y;
	}
	
	// Implementation of Object.equals().
	public boolean equals(Object object) {
		if (object instanceof MineFieldCoordinates) {
			if ((x == ((MineFieldCoordinates)object).getX()) &&
				(y == ((MineFieldCoordinates)object).getY())) {
				return true;
			}
		}
		return false;
	}
}