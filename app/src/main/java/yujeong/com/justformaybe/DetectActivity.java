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

import yujeong.com.justformaybe.core.CamDetectionReceiver;

public class DetectActivity extends AppCompatActivity {
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
        CamDetectionReceiver wifiScanReceiver = new CamDetectionReceiver(new CamDetectionReceiver.SuccessListener() {
            @Override
            public void whenSuccess(int result) {
                Log.d("MAYBE", "result level: " + result);
                if(result == CamDetectionReceiver.LEVEL_IN_DANGER) {
                    Toast.makeText(context, "몰카 위험이 있습니다.", Toast.LENGTH_LONG).show();
                }

                if(result == CamDetectionReceiver.LEVEL_IN_WARNING) {
                    Toast.makeText(context, "몰카로 의심되는 물체가 있습니다. 주의하세요", Toast.LENGTH_LONG).show();
                }

                if(result == CamDetectionReceiver.LEVEL_SAFE) {
                    Toast.makeText(context, "제가 진단한 바로는 안전한 환경입니다!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void whenFailure(String whyFailed) {
                Log.d("MAYBE", "Failed to scan wifi: " + whyFailed);
            }
        }, wifiManager);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(wifiScanReceiver, intentFilter);
        boolean success = wifiManager.startScan();
        Log.d("MAYBE", "started to scan");
    }
}
