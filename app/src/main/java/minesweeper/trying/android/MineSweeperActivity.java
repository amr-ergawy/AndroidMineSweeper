package minesweeper.trying.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MineSweeperActivity extends Activity {

    // Members are static for ensuring saf processing by
    // listener handlers of different control widgets.
    static MineSweeperActivity mineSweeperActivity;

    // The currently shown dialog.
    private Dialog shownDialog = null;

    private static final String MESSAGE_FINISH_NOT_COMPATIBLE =
            "At least Android 2.3 and touch screen are required, exiting game!";

    private static final String MESSAGE_HELP =
            "- Touch covered field cell to mark/un-mark it.\n" +
                    "- Hold-touch covered field cell to open it.\n" +
                    "- Hold-touch opened field cell to open more cells.";

    private static final String MESSAGE_CREDITS =
            "Development: Amr Ergawy\n" +
                    "Photoshop: Amira ElGamal";

    // The ID of the latest pressed button.
    private int latestPressedButtonID = 0;
    static final String STATE_LATEST_PRESSED_BUTTON_ID = "latestPressedButtonId";

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mineSweeperActivity = this;

        // Verify the system requirements.
        verifySystemRequirements();

        // Handle "Play" button.
        final Button playButton = (Button)findViewById(R.id.Play);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // This is to prevent accidental leakage of dialogs when the button is not
                // responsive and saves multiple clicks.
                handleShownDialog();
                latestPressedButtonID = R.id.Play;
                shownDialog = Dialogs.showChooseGameDialog();
            }
        });

        //Handle "Help" button.
        final Button helpButton = (Button) findViewById(R.id.Help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // This is to prevent accidental leakage of dialogs when the button is not
                // responsive and saves multiple clicks.
                handleShownDialog();
                latestPressedButtonID = R.id.Help;
                shownDialog = Dialogs.showMessageDialog(mineSweeperActivity, MESSAGE_HELP);
            }
        });

        //Handle "Credits" button.
        final Button creditsAndContactButton = (Button) findViewById(R.id.Credits);
        creditsAndContactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // This is to prevent accidental leakage of dialogs when the button is not
                // responsive and saves multiple clicks.
                handleShownDialog();
                latestPressedButtonID = R.id.Credits;
                shownDialog = Dialogs.showMessageDialog(mineSweeperActivity, MESSAGE_CREDITS);
            }
        });
    }

    private void verifySystemRequirements() {
        // Verify the minimum Android version.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            shownDialog = Dialogs.showExitMessageDialog(this, MESSAGE_FINISH_NOT_COMPATIBLE);
        }
        // Verify that the system has a touch screen.
        if (getBaseContext().getResources().getConfiguration().touchscreen ==
                Configuration.TOUCHSCREEN_NOTOUCH) {
            shownDialog = Dialogs.showExitMessageDialog(this, MESSAGE_FINISH_NOT_COMPATIBLE);
        }
        // No need to inform the user that requirements are verified.
    }

    public void onSaveInstanceState (Bundle outState) {
        if (shownDialog != null) {
            outState.putInt(STATE_LATEST_PRESSED_BUTTON_ID, latestPressedButtonID);
        }
    }

    public void onRestoreInstanceState (Bundle savedInstanceState) {
        latestPressedButtonID = savedInstanceState.getInt(STATE_LATEST_PRESSED_BUTTON_ID);
        switch (latestPressedButtonID) {
            case R.id.Play:
                shownDialog = Dialogs.showChooseGameDialog();
                break;
            case R.id.Help:
                shownDialog = Dialogs.showMessageDialog(mineSweeperActivity, MESSAGE_HELP);
                break;
            case R.id.Credits:
                shownDialog = Dialogs.showMessageDialog(mineSweeperActivity, MESSAGE_CREDITS);
                break;
            default:
                latestPressedButtonID = 0;
                break;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        handleShownDialog();
    }

    private void handleShownDialog() {
        if (shownDialog != null) {
            if (shownDialog.isShowing()) {
                shownDialog.dismiss();
            }
            shownDialog = null;
        }
    }

    public void shownDialogLastInSession() {
        handleShownDialog();
    }

    public void updateShownDialog(Dialog newShownDialog) {
        handleShownDialog();
        shownDialog = newShownDialog;
    }
}