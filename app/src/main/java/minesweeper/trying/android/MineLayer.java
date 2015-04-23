package minesweeper.trying.android;


import java.util.Random;

public class MineLayer {
	
	// For randomly selecting a cell within a mine segment.
    private static final Random mineCellSelector1D = new Random();

    // Initiate the mine layer.
	public MineLayer () {
        // do no thing.
	}
	
	// Lays a set of mines in he mine field.
	public static void layMines(MineField mineField)
            throws IllegalArgumentException {
        if (mineField == null) {
            // validating the number of mines and its coordinates
            // is left to the MineField class.
            throw new IllegalArgumentException(
                    "Failed to lay mines: invalid parameter(s).");
        }
        // The mines distribution algorithm provides:
        // 1. Equal distribution of mines by giving each mine an equal share of the available cells.
        // 2. Randomness by randomly picking the mine cell within its cells share.
        // 3. Deterministic termination by avoiding try-and-error approaches for checking whether
        // a mine is placed or not.

        // Calculate the share of each mine, using integer division.
        int mineSegmentWidth =
                (mineField.fieldWidth()*mineField.fieldLength())/mineField.numOfMines();
        if (mineSegmentWidth < 1)
            throw new IllegalArgumentException("Failed to lay mines: mines do not fit into field.");

        // Calculate the position of each mine with its share.
        int mineSegmentStart1D = 0;
        int mineCell1D;
        int x, y;
        for (int i=0; i<mineField.numOfMines(); i++) {
            mineCell1D = mineSegmentStart1D + mineCellSelector1D.nextInt(mineSegmentWidth);
            y = mineCell1D / mineField.fieldWidth();
            x = mineCell1D - (y*mineField.fieldWidth());
            mineField.addMineCoordinates(new MineFieldCoordinates(x, y));
            mineSegmentStart1D += mineSegmentWidth;
        }
	}
}