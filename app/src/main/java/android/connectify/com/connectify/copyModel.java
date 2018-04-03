package android.connectify.com.connectify;

/**
 * Created by yakov on 4/1/2018.
 */

public class copyModel {
    int id;
    String text;
    String date;

    public copyModel (int id, String text, String date) {
        this.id = id;
        this.text = text;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
