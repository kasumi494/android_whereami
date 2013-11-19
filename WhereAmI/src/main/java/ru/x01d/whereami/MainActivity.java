package ru.x01d.whereami;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends ActionBarActivity implements LocationListener {
    private LocationManager locationManager;
    private String provider;
    TextView myLocationText;

    private GoogleMap map;
    private Marker marker;

    private static final int ONE_MINUTE = 1000 * 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        myLocationText  = (TextView) findViewById(R.id.myLocationText);
        map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        findCurrentLocation();
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

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, ONE_MINUTE, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        updateWithNewLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "Enabled provider: " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Disabled provider: " + s, Toast.LENGTH_SHORT).show();
    }

    private void findCurrentLocation()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);

        provider = locationManager.getBestProvider(criteria, false);

        boolean enabled = locationManager.isProviderEnabled(provider);

        if (!enabled)
        {
            provider = locationManager.getBestProvider(criteria, true);
            if (provider == null)
            {
                provider = locationManager.getBestProvider(criteria, false);
                Toast.makeText(this, "Enable location service, please", Toast.LENGTH_LONG).show();
            }
        }
        Toast.makeText(this, "Provider: " + provider, Toast.LENGTH_LONG).show();

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        updateWithNewLocation(location);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()), 15));

        locationManager.requestLocationUpdates(provider, ONE_MINUTE, 1, this);
    }

    private void updateWithNewLocation(Location location) {
        String latLongString = getString(R.string.no_location);

        if (marker != null)     marker.remove();

        if (location != null)
        {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latLongString = getString(R.string.latitude) + ": " + lat + "\n"
                    + getString(R.string.longitude) + ": " + lng;

            marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(getString(R.string.you_are_here)));

        }

        myLocationText.setText(getString(R.string.current_position) + "\n" + latLongString);
    }
}
