package android.connectify.com.connectify;

import android.content.Context;

/**
 * Created by yakov on 4/1/2018.
 */

public class LoginHelper {
    SettingsHelper settingsHelper;

    public LoginHelper (Context context) {
        this.settingsHelper = new SettingsHelper(context);
    }

    public boolean isLogged() {
        return (this.settingsHelper.getValueByName("login_hash") != "");
    }

    public void logout () {
        this.settingsHelper.delete("login_hash");
    }

    public boolean login (String login_hash, int user_id) {
        if (!this.isLogged()) {
            this.settingsHelper.add("login_hash", login_hash);
            this.settingsHelper.add("user_id", Integer.toString(user_id));
            return true;
        } else {
            return false;
        }
    }

    public String getLoginHash () {
        return this.settingsHelper.getValueByName("login_hash");
    }
}
