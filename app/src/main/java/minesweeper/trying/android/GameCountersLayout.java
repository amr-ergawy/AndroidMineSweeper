package minesweeper.trying.android;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by amr on 2015-02-16.
 */
public class GameCountersLayout extends LinearLayout {

    //  Game counters views layout parameters.
    private static final LinearLayout.LayoutParams countersLayoutParams =
            new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);

    private static final String PREFIX_NUM_OF_MARKS = "Marks: ";
    private static final String PREFIX_NUM_OF_MINES = "Mines: ";

    private TextView numOfMarks;
    private TextView numOfMines;

    public GameCountersLayout(Context context, int weightInActivityLayout, int numOfMines) {
        super(context);

        // Set the layout parameters.
        setOrientation(HORIZONTAL);
        setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, weightInActivityLayout));

        // Initiate the text views.
        numOfMarks = new TextView(context);
        numOfMarks.setLayoutParams(countersLayoutParams);
        numOfMarks.setGravity(Gravity.CENTER);
        numOfMarks.setTextColor(Color.YELLOW);
        setNumOfMarks(0);
        addView(numOfMarks);
        this.numOfMines = new TextView(context);
        this.numOfMines.setLayoutParams(countersLayoutParams);
        this.numOfMines.setGravity(Gravity.CENTER);
        this.numOfMines.setText(PREFIX_NUM_OF_MINES+String.valueOf(numOfMines));
        this.numOfMines.setTextColor(Color.YELLOW);
        addView(this.numOfMines);
    }

    public void setNumOfMarks(int numOfMarks) {
        this.numOfMarks.setText(PREFIX_NUM_OF_MARKS+String.valueOf(numOfMarks));
    }
}
