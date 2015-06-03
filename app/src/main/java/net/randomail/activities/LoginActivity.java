package net.randomail.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class LoginActivity extends AppCompatActivity {
    private String TAG = "Randomail/LoginActivity";
    private Progress progress;
    private Activity ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailField = (EditText) findViewById(R.id.email_field);
        final EditText passwordField = (EditText) findViewById(R.id.password_field);

        com.rey.material.widget.Button loginBtn = (com.rey.material.widget.Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progress = new Progress(ctx, R.string.login_in_progress);
                ApiServices.login(emailField.getText().toString(), passwordField.getText().toString(), new JsonHttpResponseHandler() {
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
                                    new Alert(ctx, getResources().getString(R.string.login_failed));
                                }

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] Header, Throwable err, JSONObject response) {
                                progress.dismiss();
                                Log.d(TAG, new Integer(statusCode).toString());
                                new Alert(ctx, getResources().getString(R.string.login_failed));
                            }
                        }
                );

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
