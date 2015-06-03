package net.randomail.helpers;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.os.Build;

public final class Clipboard {

    private static ClipboardImpl IMPL;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            IMPL = new ClipboardImplHoneyComb();
        } else {
            IMPL = new ClipboardImplBase();
        }
    }

    public void copy(Context context, String text) {
        IMPL.copy(context, text);
    }

    private interface ClipboardImpl {
        void copy(Context context, String text);
    }

    private static class ClipboardImplBase implements ClipboardImpl {

        @Override
        public void copy(Context context, String text) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static class ClipboardImplHoneyComb implements ClipboardImpl {

        @Override
        public void copy(Context context, String text) {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("connect copied text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

}