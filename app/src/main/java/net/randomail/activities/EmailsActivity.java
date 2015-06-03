package net.randomail.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.cocosw.bottomsheet.BottomSheet;
import com.loopj.android.http.JsonHttpResponseHandler;

import net.randomail.R;
import net.randomail.RandomailApplication;
import net.randomail.adapters.EmailsAdapter;
import net.randomail.helpers.Clipboard;
import net.randomail.models.Email;
import net.randomail.models.User;
import net.randomail.services.ApiServices;
import net.randomail.ui.RecyclerItemClickListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class EmailsActivity extends AppCompatActivity implements MaterialTabListener, SearchView.OnQueryTextListener {
    private Activity ctx = this;
    private SwipeRefreshLayout[] mSwipeRefreshLayout = new SwipeRefreshLayout[2];
    private RecyclerView[] EmailsRecyclerView = new RecyclerView[2];
    private MaterialTabHost tabHost;
    private EmailsAdapter[] EmailsAdapter = new EmailsAdapter[2];
    private ViewFlipper ListViewFlipper;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emails);

        ListViewFlipper = (ViewFlipper) findViewById(R.id.listviews_flipper);
        mSwipeRefreshLayout[0] = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout1);
        mSwipeRefreshLayout[1] = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout2);
        EmailsRecyclerView[0] = (RecyclerView) findViewById(R.id.active_emails);
        EmailsRecyclerView[1] = (RecyclerView) findViewById(R.id.paused_emails);

        for (int i = 0; i < 2; i++) {
            EmailsRecyclerView[i].setLayoutManager(new LinearLayoutManager(this));
            EmailsAdapter[i] = new EmailsAdapter(this, new ArrayList<Email>(), R.layout.email_listview);
            EmailsRecyclerView[i].setAdapter(EmailsAdapter[i]);

            mSwipeRefreshLayout[i].setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshEmails();
                }
            });

            EmailsRecyclerView[i].addOnItemTouchListener(
                    new RecyclerItemClickListener(ctx, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int index) {
                            showQuickSettings(EmailsAdapter[position].getItem(index));
                        }
                    })
            );
        }

        tabHost = (MaterialTabHost) findViewById(R.id.emailTabs);
        tabHost.addTab(new MaterialTab(ctx, false).setText(getString(R.string.active_aliases)).setTabListener(this));
        tabHost.addTab(new MaterialTab(ctx, false).setText(getString(R.string.paused_aliases)).setTabListener(this));


        com.rey.material.widget.FloatingActionButton createAliasBtn = (com.rey.material.widget.FloatingActionButton) findViewById(R.id.create_alias);
        createAliasBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createAlias();
            }
        });

        getEmails();
        getUser();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_emails, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
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

    void getEmails() {
        EmailsAdapter[0].clear();
        EmailsAdapter[1].clear();
        ApiServices.getEmails(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                onItemsLoadComplete();
                try {
                    JSONArray emails = response.getJSONArray("emails");
                    for (int i = 0; i < emails.length(); i++) {
                        if (!emails.getJSONObject(i).getBoolean("paused")) {
                            EmailsAdapter[0].addItem(new Email(ctx, emails.getJSONObject(i)));
                        } else {
                            EmailsAdapter[1].addItem(new Email(ctx, emails.getJSONObject(i)));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] Header, Throwable err, JSONObject response) {
                onItemsLoadComplete();
            }
        });
    }

    void refreshEmails() {
        getEmails();
        getUser();
    }

    void onItemsLoadComplete() {
        mSwipeRefreshLayout[0].setRefreshing(false);
        mSwipeRefreshLayout[1].setRefreshing(false);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        position = materialTab.getPosition();
        tabHost.setSelectedNavigationItem(position);
        ListViewFlipper.setDisplayedChild(position);
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        EmailsAdapter[0].getFilter().filter(newText);
        EmailsAdapter[1].getFilter().filter(newText);
        return false;
    }

    public void showQuickSettings(final Email email) {
        int menu;
        if (email.paused) menu = (R.menu.menu_quick_email_settings_paused);
        else {
            menu = (R.menu.menu_quick_email_settings_active);
        }
        new BottomSheet.Builder(this).title(email.email).sheet(menu).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.pause:
                        email.paused = true;
                        updateEmail(email);
                        break;
                    case R.id.active:
                        email.paused = false;
                        updateEmail(email);
                        break;
                    case R.id.copy:
                        Clipboard clip = new Clipboard();
                        clip.copy(ctx, email.email);
                        Toast.makeText(getApplicationContext(), getString(R.string.email_copied), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.delete:
                        deleteEmail(email);
                        break;
                }
            }
        }).show();
    }

    public void deleteEmail(Email email) {
        ApiServices.deleteEmail(email, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                getEmails();
                getUser();
            }

            @Override
            public void onFailure(int statusCode, Header[] Header, Throwable err, JSONObject response) {
                getEmails();
                getUser();
            }
        });
    }

    public void updateEmail(Email email) {
        ApiServices.updateEmail(email, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                getEmails();
                getUser();
            }

            @Override
            public void onFailure(int statusCode, Header[] Header, Throwable err, JSONObject response) {
                getEmails();
                getUser();
            }
        });
    }

    public void getUser() {
        ApiServices.getUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    RandomailApplication.user = new User(ctx, response.getJSONObject("user"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] Header, Throwable err, JSONObject response) {
            }
        });
    }

    public void createAlias() {
        Intent intent = new Intent(EmailsActivity.this, EmailSettingsActivity.class);
        startActivity(intent);
    }
}
