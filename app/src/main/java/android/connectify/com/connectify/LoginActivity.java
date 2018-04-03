package android.connectify.com.connectify;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements apiActivity {
    EditText emailEt, passwordEt;
    Button loginBtn;
    LoginHelper loginHelper = new LoginHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        emailEt = (EditText) findViewById(R.id.login_email);
        passwordEt = (EditText) findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.loginSubmit);

        loginBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        login();
                    }
                }
        );

        passwordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void login () {
        Uri.Builder builder = new Uri.Builder().appendQueryParameter("email", emailEt.getText().toString()).appendQueryParameter("password", passwordEt.getText().toString());
        String query = builder.build().getEncodedQuery();
        API api = new API(query, 1, LoginActivity.this, true);
        api.execute("http://connectify.rf.gd/api/login.php");
    }

    @Override
    public void apiCallback(int code, String response) {
        if (code == 1) {
            try {
                Log.i("Responsiiii is", response);
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")) {
                    // Login successful
                    loginHelper.login(jsonObject.getString("login_hash"));
                    finish();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    if (jsonObject.getBoolean("empty_fields")) {
                        Toast.makeText(this, "Don't leave any fields empty.", Toast.LENGTH_LONG).show();
                    } else {
                        if (!jsonObject.getBoolean("email_exists")) {
                            Toast.makeText(this, "Email address does not exist.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Wrong password.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
