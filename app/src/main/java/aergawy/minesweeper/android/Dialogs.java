package aergawy.minesweeper.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

public class Dialogs {

    public static final String DEFAULT_FINISH_MESSAGE = "Finished with no message.";
    public static final String DEFAULT_NO_MESSAGE = "No message to display.";

    static Activity activityToFinish = null;
    public static Dialog showExitMessageDialog(final Activity activityToFinish, String message) {
        if (activityToFinish == null) {
            // there is no thing to do.
            return null;
        }
        Dialogs.activityToFinish = activityToFinish;
        if (message == null)
            message = DEFAULT_FINISH_MESSAGE;
        AlertDialog.Builder finishDialogBuilder = new AlertDialog.Builder(activityToFinish);
        finishDialogBuilder.setMessage(message);
        finishDialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Dialogs.activityToFinish.finish();
            }
        });
        AlertDialog exitDialog = finishDialogBuilder.create();
        exitDialog.setCancelable(false);
        exitDialog.show();
        return exitDialog;
    }

    public static Dialog showMessageDialog(Activity activity, String message) {
        if (activity == null) {
            // there is no thing to do.
            return null;
        }
        if (message == null)
            message = DEFAULT_NO_MESSAGE;
        AlertDialog.Builder msgDialogBuilder = new AlertDialog.Builder(activity);
        msgDialogBuilder.setMessage(message);
        msgDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog msgDialog = msgDialogBuilder.create();
        msgDialog.setCancelable(false);
        msgDialog.show();
        return msgDialog;
    }

    static Dialog  chooseGameDialog = null;
    public static Dialog showChooseGameDialog() {
        chooseGameDialog = new ChooseGameDialog();
        chooseGameDialog.show();
        return chooseGameDialog;
    }

    static CustomGameDialog customGameDialog = null;
    public static Dialog showCustomGameDialog() {
        customGameDialog = new CustomGameDialog();
        customGameDialog.show();
        return customGameDialog;
    }
}
