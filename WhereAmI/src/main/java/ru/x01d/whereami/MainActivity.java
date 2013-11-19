package ru.x01d.whereami;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(context);

        String provider = LocationManager.GPS_PROVIDER;
        Location location =
                locationManager.getLastKnownLocation(provider);

        PlaceholderFragment placeholderFragment = new PlaceholderFragment(location);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, placeholderFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {

        View rootView;
        TextView myLocationText;
        Location location;

        public PlaceholderFragment(Location location) {
            this.location = location;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            myLocationText = (TextView) rootView.findViewById(R.id.myLocationText);

            updateWithNewLocation(location);
            return rootView;
        }

        private void updateWithNewLocation(Location location) {
            String latLongString;

            if (location != null)
            {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                latLongString = "Lat:" + lat + "\nLong:" + lng;
            } else latLongString = "No location found";

            myLocationText.setText("Your Current Position is:\n" +
                    latLongString);
        }
    }

}
