package minesweeper.trying.android;

import java.util.Iterator;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MineFieldLayout extends LinearLayout {

	//  Mine field views layout parameters.
	private static final LinearLayout.LayoutParams fieldLayoutParams =
		new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				  					  LayoutParams.MATCH_PARENT,
				  					  1);
	
	// Mine field dimensions.
	private int fieldWidth;
	private int fieldLength;

    public MineFieldLayout(Context context, int weightInActivityLayout,
                           MineField mineField, int[] initCellStates)
            throws IllegalArgumentException {
        super(context);

        if (mineField == null || initCellStates == null)
            throw new IllegalArgumentException(
                    "Failed to create mineFieldLayout, invalid parameter(s).");

        // Create the mines views.
        createMinesViews(weightInActivityLayout, mineField, initCellStates);
    }

	public MineFieldLayout(Context context, int weightInActivityLayout, MineField mineField)
            throws IllegalArgumentException {
		super(context);

        if (mineField == null)
            throw new IllegalArgumentException(
                    "Failed to create mineFieldLayout, invalid parameter(s).");

		// Create the mines views.
		createMinesViews(weightInActivityLayout, mineField, null);
	}
	
	// Create mines views.
	private void createMinesViews(
            int weightInActivityLayout, MineField mineField, int[] initCellsStates) {
        // Initiate mine field dimensions
        fieldWidth = mineField.fieldWidth();
        fieldLength = mineField.fieldLength();

		// Set layout options.
		setOrientation(VERTICAL);
		setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, weightInActivityLayout));
		
		// Create mines views.
		for (int i=0; i<mineField.fieldLength(); i++) {
			LinearLayout minesRow = new LinearLayout(getContext());
			minesRow.setOrientation(HORIZONTAL);
			minesRow.setLayoutParams(fieldLayoutParams);
			for (int j=0; j<mineField.fieldWidth(); j++) {
				// Create view.
				MineView mineView = new MineView(getContext(), j, i,
                        mineField.getFieldMap(j, i),
                        initCellsStates == null ?
                                MineView.CELL_COVERED :
                                initCellsStates[i*mineField.fieldWidth()+j]);
				// Layout.
				mineView.setLayoutParams(fieldLayoutParams);
				minesRow.addView(mineView, j);
			}
			addView(minesRow, i);
		}
	}

	// Returns MineView in the specified coordinates.
	private MineView getMineView(int xCoordinate, int yCoordinate) {
		
		MineView mineView = null;
		
		// Get the mine view.
		if ((0 <= xCoordinate && xCoordinate < fieldWidth) &&
		    (0 <= yCoordinate && yCoordinate < fieldLength)) {
			mineView = (MineView)((LinearLayout)getChildAt(yCoordinate))
											   .getChildAt(xCoordinate);
		}
		
		return mineView;		
	}
	
	// Explode all mines.
	public void onUncoveredMine(Iterator minesCoordinates) {
		MineFieldCoordinates mineCoordinate;
		MineView mineView;
		while (minesCoordinates.hasNext()) {
			mineCoordinate = (MineFieldCoordinates)minesCoordinates.next();
			mineView = getMineView(mineCoordinate.getX(), mineCoordinate.getY());
			mineView.setImageResource(R.drawable.explosion);
		}
	}
	
	/**
	 * Propagates open cell uncovered.
	 * @return the number of uncovered neighbors of the empty cell.
	 */
	public int propagateUncoveryOfNonMineView(int xCoordinate, int yCoordinate) {
		
		// Get the mine view.
		MineView uncoveredNoneMineView = getMineView(xCoordinate, yCoordinate);
		
		// Recursively uncover non-mine neighbours.
		return recursivelyUncoverNonMineViews(uncoveredNoneMineView, -1, 1, -1, 1);
	}	
	
	private int recursivelyUncoverNonMineViews(MineView centerView,
                                               int startXDelta, int endXDelta,
                                               int startYDelta, int endYDelta) {
		int centerX = centerView.getXCoordinate();
		int centerY = centerView.getYCoordinate();
		
		// New dimensions deltas.
		int newStartXDelta;
		int newEndXDelta;
		int newStartYDelta;
		int newEndYDelta;
		
		MineView neighborMineView;

        int numOfUncoveredMines = 0;

		for (int i=centerX+startXDelta; i<=centerX+endXDelta; i++) {
			for (int j=centerY+startYDelta; j<=centerY+endYDelta; j++) {
				
				if (!(i == centerX && j == centerY)) {
					
					// Get the neighbour.
					neighborMineView = getMineView(i, j);
					
					if (neighborMineView != null &&
						neighborMineView.getNumOfNeighborMines() != MineField.MINE) {
							
						// Uncover the mine.
                        if (neighborMineView.isCovered()) {
                            neighborMineView.uncoverInResponseToPropagation();
                            numOfUncoveredMines++;
                        }
							
						if (neighborMineView.getNumOfNeighborMines() == 0) {
							// Calculate new index deltas.
							switch (i-centerX) {
								case -1:
									newStartXDelta = -1;
									newEndXDelta = 0;
									break;
								case 1:
									newStartXDelta = 0;
									newEndXDelta = 1;
									break;
								default:
									newStartXDelta = 0;
									newEndXDelta = 0;
							}
							switch (j-centerY) {
								case -1:
									newStartYDelta = -1;
									newEndYDelta = 0;
									break;
								case 1:
									newStartYDelta = 0;
									newEndYDelta = 1;
									break;
								default:
									newStartYDelta = 0;
									newEndYDelta = 0;
							}

							// Recursive call.
                            numOfUncoveredMines +=
                                    recursivelyUncoverNonMineViews(neighborMineView,
                                            newStartXDelta, newEndXDelta,
                                            newStartYDelta, newEndYDelta);
						}
					}
				}
			}
		}
        return numOfUncoveredMines;
	}

    static final String STATE_FIELD_LAYOUT = "fieldLayout";

    public void saveFieldLayoutState(Bundle outState)
            throws IllegalArgumentException {
        if (outState == null)
            throw new IllegalArgumentException(
                    "Failed to save field layout state, invalid parameter(s).");
        // Saving the state of the filed cells.
        int[] fieldLayout1D = new int[fieldWidth*fieldLength];
        for (int i=0; i<fieldWidth; i++) {
            for (int j=0; j<fieldLength; j++) {
                fieldLayout1D[j*fieldWidth+i] = getMineView(i, j).getCellState();
            }
        }
        outState.putIntArray(STATE_FIELD_LAYOUT, fieldLayout1D);
    }

    public boolean areAllNeighborMinesMarked(int xCoordinate, int yCoordinate) {
        if ((0 <= xCoordinate && xCoordinate < fieldWidth) &&
                (0 <= yCoordinate && yCoordinate < fieldLength)) {
            MineView centerMineView = getMineView(xCoordinate, yCoordinate);
            if (centerMineView.getNumOfNeighborMines() <= 0)
                return true;
            int numOfMarkedNeighborCells = 0;
            int numOfMarkedNeighborMines = 0;
            MineView neighborMineView;
            for (int i=xCoordinate-1; i<=xCoordinate+1; i++) {
                for (int j=yCoordinate-1; j<=yCoordinate+1; j++) {
                    if ((0 <= i && i < fieldWidth) && (0 <= j && j < fieldLength)) {
                        if (!(i == xCoordinate && j == yCoordinate)) {
                            neighborMineView = getMineView(i, j);
                            if (neighborMineView.getCellState() == MineView.CELL_MARKED) {
                                numOfMarkedNeighborCells++;
                                if (neighborMineView.getNumOfNeighborMines() == MineField.MINE) {
                                    numOfMarkedNeighborMines++;
                                }
                            }
                        }
                    }
                }
            }
            if (centerMineView.getNumOfNeighborMines() == numOfMarkedNeighborMines &&
                    numOfMarkedNeighborMines == numOfMarkedNeighborCells) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}