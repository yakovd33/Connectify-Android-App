package android.connectify.com.connectify;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

public class MainActivity extends SidebarMenuActivity {
    SettingsHelper settingsHelper = new SettingsHelper(this);
    LoginHelper loginHelper = new LoginHelper(this);
    Button serverPathBtn;

    public static final int INITIALIZATION_API_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if logged
        //loginHelper.login("440E759A46EEC4A1C5CB9DCAC0B9F2AA");

        if (loginHelper.isLogged()) {
            // Logged
            setContentView(R.layout.activity_main_logged);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        } else {
            // Not Logged
            setContentView(R.layout.activity_main);
            Button loginBtn = (Button) findViewById(R.id.main_not_logged_login_btn);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });

            serverPathBtn = (Button) findViewById(R.id.server_path_btn);
            serverPathBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
                    View mView = layoutInflaterAndroid.inflate(R.layout.dialog_box, null);
                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilderUserInput.setView(mView);

                    final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                    userInputDialogEditText.setText(settingsHelper.getValueByName("api_path"));
                    alertDialogBuilderUserInput
                            .setCancelable(false)
                            .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    // ToDo get user input here
                                    if (settingsHelper.getValueByName("api_path") != "") {
                                        // Update
                                        settingsHelper.delete("api_path");
                                    }

                                    settingsHelper.add("api_path", userInputDialogEditText.getText().toString());
                                }
                            })

                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogBox, int id) {
                                            dialogBox.cancel();
                                        }
                                    });

                    AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                    alertDialogAndroid.show();
                }
            });
        }
    }
}
