package net.randomail.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.rey.material.widget.EditText;
import com.rey.material.widget.Spinner;

import net.randomail.R;
import net.randomail.RandomailApplication;
import net.randomail.models.Email;
import net.randomail.services.ApiServices;
import net.randomail.ui.Alert;
import net.randomail.ui.Progress;

import org.apache.http.Header;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

public class EmailSettingsActivity extends AppCompatActivity {
    private String TAG = "Randomail/EmailSettingsActivity";
    private Progress progress;
    private Activity ctx = this;
    private EditText emailField;
    private EditText labelField;
    private Spinner domains;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_settings);

        emailField = (EditText) findViewById(R.id.email);
        labelField = (EditText) findViewById(R.id.label);

        ImageButton reloadBtn = (ImageButton) findViewById(R.id.reload_btn);
        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geretateRandomString();
            }
        });

        domains = (Spinner) findViewById(R.id.domains);
        ArrayList<String> items = new ArrayList<String>();
        items.add("@alpha.randomail.net");
        if (RandomailApplication.user != null) {
            for (String domain : RandomailApplication.user.domains) {
                items.add(domain);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, items);
        domains.setAdapter(adapter);

        com.rey.material.widget.Button createAlias = (com.rey.material.widget.Button) findViewById(R.id.create_alias);
        createAlias.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createAlias();
            }
        });

    }


    public void geretateRandomString() {
        emailField.setText(new BigInteger(64, new SecureRandom()).toString(32).toLowerCase());
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

    void createAlias() {
        Email alias = new Email(ctx, new JSONObject());
        alias.email = emailField.getText().toString() + domains.getSelectedItem().toString();
        if (labelField.getText().toString().length() > 0) {
            alias.label = labelField.getText().toString();
        }

        progress = new Progress(ctx, R.string.alias_creation_in_progress);
        ApiServices.createEmail(alias, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progress.dismiss();
                Intent intent = new Intent(EmailSettingsActivity.this, EmailsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] Header, Throwable err, JSONObject response) {
                progress.dismiss();
                new Alert(ctx, getResources().getString(R.string.alias_creation_failed));
            }
        });
    }
}
