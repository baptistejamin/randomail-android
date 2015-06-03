package net.randomail.models;

/**
 * Created by baptistejamin on 02/06/15.
 */

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {


    public String email = "";
    public boolean active = false;
    public ArrayList<String> domains = new ArrayList<String>();

    public User(Context context, JSONObject data) {
        try {
            this.email = data.getString("email");
        } catch (JSONException e) {
        }

        try {
            this.active = data.getBoolean("paused");
        } catch (JSONException e) {
        }

        try {
            for (int i = 0; i < data.getJSONArray("domains").length(); i++) {
                domains.add(data.getJSONArray("domains").getJSONObject(i).getString("domain"));
            }
        } catch (JSONException e) {
        }
    }
}