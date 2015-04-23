package minesweeper.trying.android;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MineView extends ImageView {
	
	// Possible cell states.
	static final int CELL_COVERED = 0;
	static final int CELL_MARKED = 1;
    static final int CELL_UNCOVERED = 2;
		
	// Current cell state.
	private int cellState;
	
	// Mine field coordinates.
	private int xCoordinate;
	private int yCoordinate;

    // Number of neighbor mines.
	private int numOfNeighborMines;

    // A count-down-timer class for holding touch down on a mine view.
    public static class HoldTouchCountDownTimer extends CountDownTimer {

        private static final long HOLD_TOUCH_PERIOD_MS = 200;
        private MineView touchedMine = null;

        public HoldTouchCountDownTimer() {
            super(HOLD_TOUCH_PERIOD_MS, HOLD_TOUCH_PERIOD_MS);
        }

        public void setTouchedMine(MineView touchedMine) {
            this.touchedMine = touchedMine;
        }

        public void onTick(long millisUntilFinished) {
            // do no thing.
        }

        public void onFinish() {
            if (touchedMine != null) {
                touchedMine.handleTimedOutInTouchDown();
            }
        }
    }
    // A count-down-timer static member for use by all mine views.
    public static final HoldTouchCountDownTimer holdTouchCountDownTimer =
            new HoldTouchCountDownTimer();

	public MineView(Context context, 
					int xCoordinate, int yCoordinate,
					int numOfNeighborMines,
                    int initCellState) {
		super(context);
		
		// Set mine field properties.
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.numOfNeighborMines = numOfNeighborMines;
				
		// Set initial state.
        cellState = initCellState;
        switch (initCellState) {
            case CELL_COVERED:
                setImageResource(R.drawable.covered);
                break;
            case CELL_MARKED:
                setImageResource(R.drawable.mark);
                break;
            case CELL_UNCOVERED:
                setImageResource(getDrawableResource());
                break;
            default:
                cellState = CELL_COVERED;
                setImageResource(R.drawable.covered);
                break;
        }

        // Set properties.
        setScaleType(ImageView.ScaleType.FIT_XY);
        setPadding(1, 1, 1, 1);
	}

    public boolean onTouchEvent(MotionEvent event) {
        if (!((MineFieldActivity)getContext()).isGameOnGoing())
            return true;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setBackgroundResource(R.drawable.focus);
            MineView.holdTouchCountDownTimer.setTouchedMine(this);
            MineView.holdTouchCountDownTimer.start();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            setBackgroundResource(0);
            MineView.holdTouchCountDownTimer.cancel();
            // Toggle cell mark.
            if (cellState == MineView.CELL_COVERED) {
                cellState = MineView.CELL_MARKED;
                setImageResource(R.drawable.mark);
                reportCellMarked();
            } else if (cellState == MineView.CELL_MARKED) {
                cellState = MineView.CELL_COVERED;
                setImageResource(R.drawable.covered);
                reportCellUnmarked();
            }
        }
        return true;
    }

    public void handleTimedOutInTouchDown() {
        if (cellState == MineView.CELL_COVERED) {
            cellState = MineView.CELL_UNCOVERED;
            setImageResource(getDrawableResource());
            reportCellUncovered();
        } else if (cellState == MineView.CELL_UNCOVERED) {
            reportUncoveredCellPropagated();
        }
    }

	// Return X coordinates.
	public int getXCoordinate() {
		return xCoordinate;
	}
	
	// Return Y coordinate.
	public int getYCoordinate() {
		return yCoordinate;
	}
	
	// Return number of mines.
	public int getNumOfNeighborMines() {
		return numOfNeighborMines;
	}
	
	// Uncover as empty cell neighbor.
	public void uncoverInResponseToPropagation() {
		cellState = MineView.CELL_UNCOVERED;
		setImageResource(getDrawableResource());
	}
	
	private void reportCellUncovered() {
		((MineFieldActivity)getContext()).fieldCellUncovered(xCoordinate, yCoordinate);
	}
	
	private void reportCellMarked() {
		((MineFieldActivity)getContext()).fieldCellMarked(xCoordinate, yCoordinate);
	}
	
	private void reportCellUnmarked() {
		((MineFieldActivity)getContext()).fieldCellUnMarked(xCoordinate, yCoordinate);
	}

    private void reportUncoveredCellPropagated() {
        ((MineFieldActivity)getContext()).fieldUncoveredCellPropagated(xCoordinate, yCoordinate);
    }
	
	private int getDrawableResource() {
		int resourceId;
		switch (numOfNeighborMines) {
		case 0:
			resourceId = R.drawable.zero;
			break;
		case 1:
			resourceId = R.drawable.one;
			break;
		case 2:
			resourceId = R.drawable.two;
			break;
		case 3:
			resourceId = R.drawable.three;
			break;
		case 4:
			resourceId = R.drawable.four;
			break;
		case 5:
			resourceId = R.drawable.five;
			break;
		case 6:
			resourceId = R.drawable.six;
			break;
		case 7:
			resourceId = R.drawable.seven;
			break;
		case 8:
			resourceId = R.drawable.eight;
			break;
		default:
			resourceId = R.drawable.mark;
		}
		return resourceId;
	}

    public boolean isCovered() {
        return cellState == CELL_COVERED;
    }

    public int getCellState() {
        return cellState;
    }
}