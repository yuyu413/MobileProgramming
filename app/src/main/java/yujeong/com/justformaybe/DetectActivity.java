package yujeong.com.justformaybe;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class DetectActivity extends AppCompatActivity {
    class WifiScanReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                scanSuccess();
            } else {
                scanFailure();
            }
        }
    }

    private WifiManager wifiManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);

        String[] PERMS_INITIAL={
                Manifest.permission.ACCESS_FINE_LOCATION,
        };
        ActivityCompat.requestPermissions(this, PERMS_INITIAL, 127);

        context = getApplicationContext();
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiScanReceiver wifiScanReceiver = new WifiScanReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(wifiScanReceiver, intentFilter);

        boolean success = wifiManager.startScan();
        if (!success) {
            scanFailure();
        }

        Log.d("MAYBE", "started to scan");
    }


    private void scanSuccess() {
        List<ScanResult> results = wifiManager.getScanResults();
        int counter = 1;
        for (ScanResult result : results) {
            Log.d("MAYBE", "---- count " + counter + "-----");
            Log.d("MAYBE", "BSSID: " + result.BSSID);
            Log.d("MAYBE", "Capabilities: " + result.capabilities);
            Log.d("MAYBE", "Frequency: " + result.frequency);
            Log.d("MAYBE", "Level: " + result.level);
            Log.d("MAYBE", "channelWidth: " + result.channelWidth);
            Log.d("MAYBE", "operatorFriendlyName: " + result.operatorFriendlyName);
            Log.d("MAYBE", "venueName: " + result.venueName);
            Log.d("MAYBE", "timestamp: " + result.timestamp);
            Log.d("MAYBE", "toString(): " + result.toString());
            Log.d("MAYBE", "---- count " + counter + " end ----");
            counter += 1;
        }
    }

    private void scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        List<ScanResult> results = wifiManager.getScanResults();
        Toast.makeText(context, "Failed to scan wifi..", Toast.LENGTH_LONG).show();
    }
}
