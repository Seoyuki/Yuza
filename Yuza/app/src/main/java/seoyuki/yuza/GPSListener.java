package seoyuki.yuza;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by jaewon on 2016-10-02.
 */

public class GPSListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        String msg = "Latitude :"+latitude+"\nLongitude:"+longitude;
        Log.i("GPSListener",msg);
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


    public void onProviderEnabled(String provider) {

    }

    public void onProviderDisabled(String provider) {

    }
}
