package android.connectify.com.connectify;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class navigation extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_navigation, container, false);
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) view.findViewById(R.id.navigation);
        bottomNavigationViewEx.setSmallTextSize(10);
        bottomNavigationViewEx.setTextVisibility(false);

        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent myIntent;

                switch (item.getItemId()) {
                    case R.id.navigation_home :
                        myIntent = new Intent(getActivity(), MainActivity.class);
                        getActivity().startActivity(myIntent);
                        break;
                    case R.id.navigation_history :
                        myIntent = new Intent(getActivity(), History.class);
                        getActivity().startActivity(myIntent);
                        break;
                    case R.id.navigation_settings :
                        myIntent = new Intent(getActivity(), SettingsActivity.class);
                        getActivity().startActivity(myIntent);
                        break;
                }
                return false;
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}