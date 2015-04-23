package minesweeper.trying.android;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MineFieldActivity extends Activity {

    // The activity layout.
    private static final LinearLayout.LayoutParams activityLayoutParams =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1);

	// The mine field.
	private MineField mineField = null;

    // The activity layout.
    private LinearLayout activityLayout = null;

	// The mine field layout.
	private MineFieldLayout mineFieldLayout = null;
    private static final int LAYOUT_WEIGHT_MINE_FIELD = 1;

    // Counters layout.
    private GameCountersLayout gameCounters;
    private static final int LAYOUT_WEIGHT_GAME_COUNTERS = 6;

    // Marked cells.
    private int markedCells;

	// Detected mines.
	private int detectedMines;

    // Uncovered cells.
    private int uncoveredCells;

    // Number of no-mines cells;
    private int numOfNoMinesCells;
	
	// Game result dialogs.
    private static final String FINISH_MESSAGE_WON = "Congrats, won!";
	private static final String FINISH_MESSAGE_LOST = "Sorry, lost!";
    private static final String FINISH_MESSAGE_INIT_ERROR =
            "Error starting game, try again or contact developers.";

    // Game result constants.
    private static final int GAME_RESULT_ON_GOING = 0;
    private static final int GAME_RESULT_WON = 1;
    private static final int GAME_RESULT_LOST = 2;

    // Game result.
    private int gameResult = GAME_RESULT_ON_GOING;

    // The currently shown dialog.
    Dialog shownDialog = null;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) return;

        int gameLevel = 
        	getIntent().getIntExtra(GameLevels.GAME_LEVEL, GameLevels.LEVEL_BEGINNER);

        // Initialize the mine field.
		try {
			switch (gameLevel) {
				case GameLevels.LEVEL_BEGINNER:
					mineField = new MineField(5, 5, 5);
					break;
				case GameLevels.LEVEL_INTERMEDIATE:
					mineField = new MineField(10, 7, 7);
					break;
				case GameLevels.LEVEL_EXPERT:
					mineField = new MineField(20, 10, 10);
					break;
                case GameLevels.LEVEL_CUSTOM:
                    int fieldRows = getIntent().getIntExtra(GameLevels.GAME_FIELD_ROWS, 2);
                    int fieldCols = getIntent().getIntExtra(GameLevels.GAME_FIELD_COLS, 2);
                    int numOfMines = getIntent().getIntExtra(GameLevels.GAME_NUM_OF_MINES, 2);
                    mineField = new MineField(numOfMines, fieldCols, fieldRows);
                    break;
				default:
                    shownDialog = Dialogs.showExitMessageDialog(this, FINISH_MESSAGE_INIT_ERROR);
                    break;
			}
		} catch (Exception exp) {
            mineField = null;
            shownDialog = Dialogs.showExitMessageDialog(this, FINISH_MESSAGE_INIT_ERROR);
		}

        // Lay mines.
		if (mineField != null) {
            try {
                MineLayer.layMines(mineField);
            } catch(Exception e) {
                mineField = null;
                shownDialog = Dialogs.showExitMessageDialog(this, FINISH_MESSAGE_INIT_ERROR);
            }
		}

        // Initiate marked cells.
        markedCells = 0;

		// Initiate detected mines.
		detectedMines = 0;

        // Initiate uncovered mines
        uncoveredCells = 0;

        // Initiate the number of no-mines cells.
        numOfNoMinesCells = (mineField.fieldWidth()*mineField.fieldLength())-mineField.numOfMines();

        // Create the mine field layout.
        try {
            mineFieldLayout = new MineFieldLayout(this, LAYOUT_WEIGHT_MINE_FIELD, mineField);
        } catch (Exception e) {
            mineField = null;
            mineFieldLayout = null;
            shownDialog = Dialogs.showExitMessageDialog(this, FINISH_MESSAGE_INIT_ERROR);
        }

        // Create the counters layout.
        gameCounters = new GameCountersLayout(
                this, LAYOUT_WEIGHT_GAME_COUNTERS, mineField.numOfMines());

        // Finally, create the activity layout.
        addActivityLayout();
    }

    private void addActivityLayout() {
        // Create the activity view.
        activityLayout = new LinearLayout(this);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setLayoutParams(activityLayoutParams);

        // Add the sub-views.
        // 1. Add the game status view.
        activityLayout.addView(gameCounters);
        // 2. Add the mineField View.
        activityLayout.addView(mineFieldLayout);

        // Set the content view.
        setContentView(activityLayout);
    }
    
    public void fieldCellUncovered(int xCoordinate, int yCoordinate) {
    	if (mineField.getFieldMap(xCoordinate, yCoordinate) == MineField.MINE) {
    		mineFieldLayout.onUncoveredMine(mineField.getMinesCoordinatesIterator());
            setGameResult(GAME_RESULT_LOST);
            // Perhaps it is redundant to call return, since the FinishMessageDialog will terminate
            // the activity. But it is better to leave it for code readability.
            return;
    	}
    	if (mineField.getFieldMap(xCoordinate, yCoordinate) == 0) {
            uncoveredCells += (1 + mineFieldLayout.propagateUncoveryOfNonMineView(xCoordinate, yCoordinate));
    	} else {
            uncoveredCells++;
        }
        // The user does not need to mark all cells.
        if (uncoveredCells == numOfNoMinesCells) {
            setGameResult(GAME_RESULT_WON);
        }
    }
    
    public void fieldCellMarked(int xCoordinate, int yCoordinate) {
        markedCells++;
    	if (mineField.getFieldMap(xCoordinate, yCoordinate)== MineField.MINE) {
    		if (mineField.numOfMines() == ++detectedMines &&
                    markedCells == mineField.numOfMines()) {
                setGameResult(GAME_RESULT_WON);
    		}
        }
        gameCounters.setNumOfMarks(markedCells);
    }
    
    public void fieldCellUnMarked(int xCoordinate, int yCoordinate) {
        markedCells--;
    	if (mineField.getFieldMap(xCoordinate, yCoordinate)== MineField.MINE) {
    		--detectedMines;
        }
        gameCounters.setNumOfMarks(markedCells);
    }

    public void fieldUncoveredCellPropagated(int xCoordinate, int yCoordinate) {
        if ((mineField.getFieldMap(xCoordinate, yCoordinate) > 0) &&
                mineFieldLayout.areAllNeighborMinesMarked(xCoordinate, yCoordinate)) {
            uncoveredCells += mineFieldLayout.propagateUncoveryOfNonMineView(xCoordinate, yCoordinate);
            // The user does not need to mark all cells.
            if (uncoveredCells == numOfNoMinesCells) {
                setGameResult(GAME_RESULT_WON);
            }
        }
    }

    static final String STATE_FILED_WIDTH = "fieldWidth";
    static final String STATE_FILED_LENGTH = "fieldLength";
    static final String STATE_FIELD_NUM_OF_MINES = "fieldNumOfMines";
    static final String STATE_MARKED_CELLS = "markedCells";
    static final String STATE_DETECTED_MINES = "detectedMines";
    static final String STATE_UNCOVERED_CELLS = "uncoveredCells";
    static final String STATE_GAME_RESULT = "gameResult";

    public void onSaveInstanceState (Bundle outState) {

        // Saving the field dimensions.
        outState.putInt(STATE_FILED_WIDTH, mineField.fieldWidth());
        outState.putInt(STATE_FILED_LENGTH, mineField.fieldLength());
        outState.putInt(STATE_FIELD_NUM_OF_MINES, mineField.numOfMines());

        mineField.saveFieldState(outState);

        // Saving the cells state counters.
        outState.putInt(STATE_MARKED_CELLS, markedCells);
        outState.putInt(STATE_DETECTED_MINES, detectedMines);
        outState.putInt(STATE_UNCOVERED_CELLS, uncoveredCells);

        // No need to save numOfNoMinesCells, will be calculated on restoration.

        // Saving the game result.
        outState.putInt(STATE_GAME_RESULT, gameResult);

        mineFieldLayout.saveFieldLayoutState(outState);

        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Restoring the field dimensions.
        mineField = new MineField(
                savedInstanceState.getInt(STATE_FIELD_NUM_OF_MINES),
                savedInstanceState.getInt(STATE_FILED_WIDTH),
                savedInstanceState.getInt(STATE_FILED_LENGTH));

        mineField.restoreFieldState(savedInstanceState);

        // Restoring the cells state counters.
        markedCells = savedInstanceState.getInt(STATE_MARKED_CELLS);
        detectedMines = savedInstanceState.getInt(STATE_DETECTED_MINES);
        uncoveredCells = savedInstanceState.getInt(STATE_UNCOVERED_CELLS);

        // Not saved, but restored by calculating.
        numOfNoMinesCells = (mineField.fieldWidth()*mineField.fieldLength())-mineField.numOfMines();

        // Create the mineFieldLayout.
        mineFieldLayout = new MineFieldLayout(this, LAYOUT_WEIGHT_MINE_FIELD, mineField,
                savedInstanceState.getIntArray(MineFieldLayout.STATE_FIELD_LAYOUT));

        // Create the gameCountersLayout.
        gameCounters = new GameCountersLayout(
                this, LAYOUT_WEIGHT_GAME_COUNTERS, mineField.numOfMines());
        gameCounters.setNumOfMarks(markedCells);

        // setContentView(mineFieldLayout);
        addActivityLayout();

        // Restoring the game result.
        setGameResult(savedInstanceState.getInt(STATE_GAME_RESULT));
    }

    public void onDestroy() {
        super.onDestroy();
        MineView.holdTouchCountDownTimer.cancel();
        if (shownDialog != null && shownDialog.isShowing())
            shownDialog.dismiss();
        shownDialog = null;
    }

    private void setGameResult(int gameResult) {
        switch(gameResult) {
            case GAME_RESULT_WON:
                this.gameResult = gameResult;
                showGameResultDialog();
                break;
            case GAME_RESULT_LOST:
                this.gameResult = gameResult;
                mineFieldLayout.onUncoveredMine(mineField.getMinesCoordinatesIterator());
                showGameResultDialog();
                break;
            default:
                this.gameResult = GAME_RESULT_ON_GOING;
                break;
        }
    }

    private void showGameResultDialog() {
        if (shownDialog != null)
            return;
        if (gameResult == GAME_RESULT_WON) {
            shownDialog = Dialogs.showExitMessageDialog(this, FINISH_MESSAGE_WON);
        } else if (gameResult == GAME_RESULT_LOST) {
            shownDialog = Dialogs.showExitMessageDialog(this, FINISH_MESSAGE_LOST);
        }
    }

    public boolean isGameOnGoing() {
        return gameResult == GAME_RESULT_ON_GOING;
    }
}