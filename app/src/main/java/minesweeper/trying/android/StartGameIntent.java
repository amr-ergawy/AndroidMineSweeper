package minesweeper.trying.android;

import android.content.ComponentName;
import android.content.Intent;

public class StartGameIntent extends Intent {
	public StartGameIntent(int level) {
		// Mine field activity.
		ComponentName mineFieldActivity = 
			new ComponentName("minesweeper.trying.android", 
							  "minesweeper.trying.android.MineFieldActivity");
		setComponent(mineFieldActivity);
		
		// Pass game level.
		putExtra(GameLevels.GAME_LEVEL, level);
	}

    public StartGameIntent(int level, int fieldRows, int fieldCols, int numOfMines) {
        this(level);

        // Pass game parameter.
        putExtra(GameLevels.GAME_FIELD_ROWS, fieldRows);
        putExtra(GameLevels.GAME_FIELD_COLS, fieldCols);
        putExtra(GameLevels.GAME_NUM_OF_MINES, numOfMines);
    }
}
