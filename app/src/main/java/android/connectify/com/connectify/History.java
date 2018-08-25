package android.connectify.com.connectify;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class History extends SidebarMenuActivity implements apiActivity {
    ListView copiesList;
    List<copyModel> copies = new ArrayList<copyModel>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    int counter = 0;
    Handler h = new Handler();
    LoginHelper loginHelper = new LoginHelper(this);
    SettingsHelper settingsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsHelper = new SettingsHelper(getBaseContext());

        if (!loginHelper.isLogged()) {
            finish();
        }

        setContentView(R.layout.activity_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        copiesList = (ListView) findViewById(R.id.history_copies_list);

        // Refresh list
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.historySwipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshHistory();
            }
        });

        refreshHistory();

        copiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                switch(counter) {
                    case 0: //first tap
                        counter++; //increase the counter
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                counter = 0;
                            }
                        }, 2000); //set the counter to 0 after 2 seconds (2000 milliseconds)
                        break;
                    case 1: //second tap
                        counter = 0; //reset the counter
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Connectify", copies.get(position).getText());
                        clipboard.setPrimaryClip(clip);

                        Toast.makeText(History.this, "Copied to your clipboard", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void refreshHistory() {
        Uri.Builder builder = new Uri.Builder().appendQueryParameter("login_hash", loginHelper.getLoginHash());
        String query = builder.build().getEncodedQuery();
        API api = new API(query, 1, History.this, true);
        api.execute(settingsHelper.getValueByName("api_path") + "/api/get_copies.php");
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void apiCallback(int code, String response) {
        if (code == 1) {
            try {
                copies.clear();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray responseJson = jsonObject.getJSONArray("copies");

                for (int i = 0; i < responseJson.length(); ++i) {
                    JSONObject item = responseJson.getJSONObject(i);
                    int id = item.getInt("id");
                    String text = item.getString("text");
                    String date = item.getString("date");
                    copyModel copy = new copyModel(id, text, date);
                    copies.add(copy);
                }

                CustomAdapter customAdapter = new CustomAdapter();
                copiesList.setAdapter(customAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class CustomAdapter extends BaseAdapter {
        public int getCount () {
            return copies.size();
        }

        public Object getItem (int i) {
            return null;
        }

        public long getItemId (int i) {
            return 0;
        }

        public View getView (int i, View view, ViewGroup viewGroup) {
            try {
                view = getLayoutInflater().inflate(R.layout.history_list_item, null);

                TextView copyText = (TextView) view.findViewById(R.id.historyListItemCopyText);

                copyText.setText(copies.get(i).getText());
            } catch (NullPointerException exception) {
                Log.i("nullll", "llll");
            }

            return view;
        }
    }
}
