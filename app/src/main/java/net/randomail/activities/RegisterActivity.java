package net.randomail.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.rey.material.widget.EditText;

import net.randomail.R;
import net.randomail.RandomailApplication;
import net.randomail.helpers.PreferencesHelper;
import net.randomail.services.ApiServices;
import net.randomail.ui.Alert;
import net.randomail.ui.Progress;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private String TAG = "Randomail/RegisterActivity";
    private Progress progress;
    private Activity ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText emailField = (EditText) findViewById(R.id.email_field);
        final EditText passwordField = (EditText) findViewById(R.id.password_field);
        final EditText confirmField = (EditText) findViewById(R.id.confirm_field);

        com.rey.material.widget.Button registerBtn = (com.rey.material.widget.Button) findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(passwordField.getText().toString().length()<8){
                    new Alert(ctx, getResources().getString(R.string.password_short));
                }else if(!passwordField.getText().toString().equals(confirmField.getText().toString())){
                    new Alert(ctx, getResources().getString(R.string.password_different));
                }else {
                    register(emailField.getText().toString(), passwordField.getText().toString());
                }
            }
        });


    }

    public void register(String email, String password) {
        progress = new Progress(ctx, R.string.registration_in_progress);
        ApiServices.register(email, password, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        progress.dismiss();

                        try {
                            String authToken = response.getJSONObject("session").getString("authToken");
                            String userId = response.getJSONObject("session").getString("userId");
                            PreferencesHelper.setUserInfo(authToken, userId);
                            RandomailApplication.mUserId = userId;
                            RandomailApplication.mAuthToken = authToken;

                            Intent intent = new Intent(ctx, EmailsActivity.class);
                            if (android.os.Build.VERSION.SDK_INT > 10)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);
                        } catch (JSONException e) {
                            progress.dismiss();
                            new Alert(ctx, getResources().getString(R.string.registration_failed));
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] Header, Throwable err, JSONObject response) {
                        progress.dismiss();
                        Log.d(TAG, new Integer(statusCode).toString());
                        new Alert(ctx, getResources().getString(R.string.registration_failed));
                    }
                }
        );
    }
}
