package aergawy.minesweeper.android;

import android.app.Dialog;
import android.content.res.Configuration;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class CustomGameDialog extends Dialog {

    // Members are static for ensuring saf processing by
    // listener handlers of different control widgets.
    static TextView fieldRowsText;
    static TextView fieldColsText;
    static TextView minesText;
    static SeekBar fieldRowsSeekBar;
    static SeekBar fieldColsSeekBar;
    static SeekBar minesSeekBar;

    public CustomGameDialog() {
        super(MineSweeperActivity.mineSweeperActivity);

        // Initialization and dialog layout.
        setTitle("Max field is 15x15");
        setCancelable(false);
        // Get the screen orientation.
        int screenOrientation = MineSweeperActivity.mineSweeperActivity.
                getBaseContext().getResources().getConfiguration().orientation;
        if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.custom_game_landscape);
        } else {
            setContentView(R.layout.custom_game_portrait);
        }

        // Initializing the static members.
        fieldRowsText = (TextView)findViewById(R.id.FieldRowsText);
        fieldColsText = (TextView)findViewById(R.id.FieldColsText);
        minesText = (TextView)findViewById(R.id.MinesText);
        fieldRowsSeekBar = (SeekBar)findViewById(R.id.FieldRowsSeekBar);
        fieldColsSeekBar = (SeekBar)findViewById(R.id.FieldColsSeekBar);
        minesSeekBar = (SeekBar)findViewById(R.id.MinesSeekBar);

        // Initialize the fieldRowsSeekBar.
        initFieldRowsSeekBar();

        // Initialize the fieldColsSeekBar.
        initFieldColsSeekBar();

        // Initialize the minesSeekBar.
        initMinesSeekBar();

        // Initialize the confirmButton.
        initConfirmButton();

        // Initialize the changeGameButton.
        initChangeGameButton();
    }

    static void applyFieldDimensionsLimits(SeekBar seekBar, int progress) {
        if (progress < 2)
            seekBar.setProgress(2);
    }

    static void updateMinesSeekBarAndTextOnFieldDimensionsChange() {
        int maxMinesSeekBar = fieldRowsSeekBar.getProgress()*fieldColsSeekBar.getProgress();
        minesSeekBar.setProgress(2);
        minesSeekBar.setMax(maxMinesSeekBar);
        minesText.setText(String.valueOf(minesSeekBar.getProgress()));
    }

    private void initFieldRowsSeekBar() {
        fieldRowsText.setText(String.valueOf(fieldRowsSeekBar.getProgress()));
        fieldRowsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                applyFieldDimensionsLimits(seekBar, progress);
                updateMinesSeekBarAndTextOnFieldDimensionsChange();
                fieldRowsText.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do not thing.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do not thing.
            }
        });
    }

    private void initFieldColsSeekBar() {
        fieldColsText.setText(String.valueOf(fieldColsSeekBar.getProgress()));
        fieldColsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                applyFieldDimensionsLimits(seekBar, progress);
                updateMinesSeekBarAndTextOnFieldDimensionsChange();
                fieldColsText.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do not thing.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do not thing.
            }
        });
    }

    private void initMinesSeekBar() {
        minesText.setText(String.valueOf(minesSeekBar.getProgress()));
        minesSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                applyFieldDimensionsLimits(seekBar, progress);
                minesText.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do not thing.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do not thing.
            }
        });
    }

    private void initConfirmButton() {
        final Button confirmButton = (Button)findViewById(R.id.Confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StartGameIntent intent = new StartGameIntent(GameLevels.LEVEL_CUSTOM,
                        fieldRowsSeekBar.getProgress(), fieldColsSeekBar.getProgress(),
                        minesSeekBar.getProgress());
                MineSweeperActivity.mineSweeperActivity.shownDialogLastInSession();
                v.getContext().startActivity(intent);
            }
        });
    }

    private void initChangeGameButton() {
        final Button changeGameButton = (Button)findViewById(R.id.ChangeGame);
        changeGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MineSweeperActivity.mineSweeperActivity.shownDialogLastInSession();
            }
        });
    }
}
