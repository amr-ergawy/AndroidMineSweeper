package minesweeper.trying.android;

import android.os.Bundle;

import java.util.Iterator;
import java.util.Vector;

public class MineField {
	
	// Field parameters.
	private int numOfMines;
	private int fieldWidth;
	private int fieldLength;
	private Vector minesCoordinates;
	private int [][] fieldMap;
	
	// Map constants.
	public static final int MINE = -1; 
	
	// Initiate the mine field.
	public MineField(int numOfMines, int fieldWidth, int fieldLength)
            throws IllegalArgumentException {
		if ((numOfMines == 0) || (fieldWidth*fieldLength < numOfMines)) {
			throw new IllegalArgumentException(
                    "Failed to create a MineField: invalid parameter(s).");
		}
		this.numOfMines = numOfMines;
		this.fieldWidth = fieldWidth;
		this.fieldLength = fieldLength;
		minesCoordinates = new Vector();
        // TODO think about a "fixed maximum size".
		fieldMap = new int [fieldWidth][fieldLength];
		for (int i=0; i<fieldWidth; i++) {
			for (int j=0; j<fieldLength; j++) {
				fieldMap[i][j] = 0;
			}
		}
	}
	
	// Returns the number of mines.
	public int numOfMines() {
       return numOfMines;
	}
	
	// Returns the field width.
	public int fieldWidth() {
		return fieldWidth;
	}

	// Returns the field length.
	public int fieldLength() {
		return fieldLength;
	}
	
	// Gets mine coordinates iterator.
    // TODO check if this is really needed.
	public Iterator getMinesCoordinatesIterator() {
		return minesCoordinates.iterator();
	}
	
	// Adds a mine coordinate to the field.
	public boolean addMineCoordinates(MineFieldCoordinates coordinates) {
		if (minesCoordinates.add(coordinates)) {
			fieldMap[coordinates.getX()][coordinates.getY()] = MINE;
			int leftX = coordinates.getX()-1;
			int rightX = coordinates.getX()+1;
			int upY = coordinates.getY()-1;
			int downY = coordinates.getY()+1;
			for (int i=upY; i<=downY; i++) {
				for (int j=leftX; j<=rightX; j++) {
					if ((i >= 0 && i < fieldLength) &&
						(j >= 0 && j < fieldWidth) &&
						(fieldMap[j][i] != MINE)){
						fieldMap[j][i] += 1;
					}
				}
			}
			return true;
		}
        return false;
	}
	
	// Get the corresponding field map value.
	public int getFieldMap(int xCoordinate, int yCoordinate) {
		return fieldMap[xCoordinate][yCoordinate];
	}

    static final String STATE_FIELD_MAP = "fieldMap";

    public void saveFieldState(Bundle outState)
            throws IllegalArgumentException {
        if (outState == null)
            throw new IllegalArgumentException(
                    "Failed to save field state, invalid parameter(s).");
        // It is enough to save the fieldMap, and on restoration we can
        // populate the both the fieldMap and the minesCoordinates.
        int[] fieldMap1D = new int[fieldWidth*fieldLength];
        for (int i=0; i<fieldWidth; i++) {
            for (int j=0; j<fieldLength; j++) {
                fieldMap1D[j*fieldWidth+i] = fieldMap[i][j];
            }
        }
        outState.putIntArray(STATE_FIELD_MAP, fieldMap1D);
    }

    public void restoreFieldState(Bundle savedInstanceState)
            throws IllegalArgumentException {
        if (savedInstanceState == null)
            throw new IllegalArgumentException(
                    "Failed to restore field state, invalid parameter(s).");
        // restore the fieldMap and the minesCoordinates from the saved
        // filedMap in the bundle.
        int[] fieldMap1D = savedInstanceState.getIntArray(STATE_FIELD_MAP);
        int x, y;
        for (int i=0; i<fieldMap1D.length; i++) {
            y = i/fieldWidth;
            x = i-(y*fieldWidth);
            fieldMap[x][y] = fieldMap1D[i];
            if (fieldMap[x][y] == MINE)
                minesCoordinates.add(new MineFieldCoordinates(x, y));

        }
    }
}