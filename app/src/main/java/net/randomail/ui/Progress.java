package net.randomail.ui;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by baptistejamin on 02/06/15.
 */
public class Progress {
    private MaterialDialog progress;

    public Progress(Activity context, int stringRessource) {
        progress = new MaterialDialog.Builder(context)
                .content(stringRessource)
                .progress(true, 0)
                .show();
    }

    public void dismiss() {
        if (progress != null) progress.dismiss();
    }
}
