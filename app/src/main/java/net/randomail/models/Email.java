package net.randomail.models;

/**
 * Created by baptistejamin on 02/06/15.
 */

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Email {

    public String userId = "";
    public String email = "";
    public String label = "";
    public boolean paused = false;
    public boolean active = false;
    public ArrayList<String> forwardTo = new ArrayList<String>();

    public Email(Context context, JSONObject data) {

        try {
            this.userId = data.getString("userId");
        } catch (JSONException e) {
        }

        try {
            this.email = data.getString("email");
        } catch (JSONException e) {
        }

        try {
            this.label = data.getString("label");
        } catch (JSONException e) {
        }

        try {
            this.paused = data.getBoolean("paused");
        } catch (JSONException e) {
        }

        try {
            this.active = data.getBoolean("paused");
        } catch (JSONException e) {
        }

        try {
            for (int i = 0; i < data.getJSONArray("forwardTo").length(); i++) {
                forwardTo.add(data.getJSONArray("forwardTo").getString(i));
            }
        } catch (JSONException e) {
        }
    }
}