package net.randomail.ui;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import net.randomail.R;


/**
 * Created by baptistejamin on 28/01/15.
 */
public class Alert {

    public Alert(Activity context, String text) {
        new MaterialDialog.Builder(context)
                .title(text)
                .icon(context.getResources().getDrawable(R.mipmap.ic_launcher))
                .theme(Theme.DARK)
                .positiveText("OK")
                .show();
    }
}
