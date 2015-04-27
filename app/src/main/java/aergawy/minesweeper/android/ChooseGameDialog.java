package aergawy.minesweeper.android;

import android.app.Dialog;
import android.content.res.Configuration;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class ChooseGameDialog extends Dialog {

    // Members are static for ensuring saf processing by
    // listener handlers of different control widgets.
    static TextView gameChoiceText;
    static RatingBar gameChoiceRatingBar;
    static ChooseGameDialog chooseGameDialog;

    public ChooseGameDialog() {
        super(MineSweeperActivity.mineSweeperActivity);
        chooseGameDialog = this;

        // Initialization and dialog layout.
        setTitle("Choose game");
        setCancelable(false);

        // Get the screen orientation.
        int screenOrientation = MineSweeperActivity.mineSweeperActivity.
                getBaseContext().getResources().getConfiguration().orientation;
        if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.choose_game_landscape);
        } else {
            setContentView(R.layout.choose_game_portrait);
        }

        // Initialize GameChoiceText.
        gameChoiceText = (TextView)findViewById(R.id.GameChoiceText);
        gameChoiceText.setText(R.string.game_choice_beginner);

        // Initialize GameChoiceRatingBar.
        initGameChoiceRatingBar();

        // Initialize the StartGame button.
        initStartGameButton();

        // Initialize the BackToMenu button.
        initBackToMenuButton();
    }

    private void initGameChoiceRatingBar() {
        gameChoiceRatingBar = (RatingBar)findViewById(R.id.GameChoiceRatingBar);
        gameChoiceRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // Ensure the choice is at least the beginner level.
                if (rating == 0) {
                    ratingBar.setRating(1);
                    rating = 1;
                }
                // Set the game choice
                switch ((int)rating) {
                    case 2:
                        gameChoiceText.setText(R.string.game_choice_intermediate);
                        break;
                    case 3:
                        gameChoiceText.setText(R.string.game_choice_expert);
                        break;
                    case 4:
                        gameChoiceText.setText(R.string.game_choice_custom);
                        break;
                    default:
                        gameChoiceText.setText(R.string.game_choice_beginner);
                        break;
                }
            }
        });
    }

    private void initStartGameButton() {
        final Button startGameButton = (Button)findViewById(R.id.StartGame);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StartGameIntent intent = null;
                int chosenGame = (int) gameChoiceRatingBar.getRating();
                switch (chosenGame) {
                    case 2:
                        intent = new StartGameIntent(GameLevels.LEVEL_INTERMEDIATE);
                        break;
                    case 3:
                        intent = new StartGameIntent(GameLevels.LEVEL_EXPERT);
                        break;
                    case 4:
                        MineSweeperActivity.mineSweeperActivity.updateShownDialog(
                                Dialogs.showCustomGameDialog());
                        break;
                    default:
                        // Do no thing.
                        intent = new StartGameIntent(GameLevels.LEVEL_BEGINNER);
                        break;
                }
                if (intent != null) {
                    MineSweeperActivity.mineSweeperActivity.shownDialogLastInSession();
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    private void initBackToMenuButton() {
        final Button backToMenuButton = (Button)findViewById(R.id.BackToMenu);
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MineSweeperActivity.mineSweeperActivity.shownDialogLastInSession();
            }
        });
    }
}