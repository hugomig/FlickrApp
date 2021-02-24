package e.g.hugom.flickrapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnGetImg;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetImg = findViewById(R.id.btnGetImage);
        btnGetImg.setOnClickListener(new GetImageOnClickListener(this));

        findViewById(R.id.btnListActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnStartPrefs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PreferenceActivity.class);
                startActivity(intent);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        findViewById(R.id.btnLocation).setOnClickListener(this);
    }

    public static String getUrl(Context context, Activity activity){
        //Accede au preference utilisateur pour la recherche
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String searchTag = prefs.getString(PreferenceActivity.keySearch,"trees");

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //Si l'utilisateur n'as pas encore accepté l'application a avoir acces a la localisation lui demande d'accepte
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        }
        Location location = null;
        Location locationWifi = null;
        //Si l'utilisateur a accepte l'application a avoir acces a la localisation
        if (!(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationWifi = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if(location != null) {
            Log.i("Hugo",location.getLongitude() + "gps");
            return "https://www.flickr.com/services/feeds/photos_public.gne?tags="+searchTag+"&format=json"+"&has_geo=1&lat=" + location.getLatitude() +
                    "&lon=" + location.getLongitude();
        }
        else if(locationWifi != null){
            Log.i("Hugo",locationWifi.getLongitude()+"wifi");
            return "https://www.flickr.com/services/feeds/photos_public.gne?tags="+searchTag+"&format=json"+"&has_geo=1&lat=" + locationWifi.getLatitude() +
                    "&lon=" + locationWifi.getLongitude();
        }
        else {
            Log.i("Hugo","Position nulle");
            return "https://www.flickr.com/services/feeds/photos_public.gne?tags="+searchTag+"&format=json";
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnLocation){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
            }
            else {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location locationWifi = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(location != null) {
                    Log.i("Hugo",location.getLongitude() + "");
                }
                else if(locationWifi != null){
                    Log.i("Hugo",locationWifi.getLongitude()+"");
                }
                else {
                    Log.i("Hugo","Position nulle");
                }
            }
        }
    }
}