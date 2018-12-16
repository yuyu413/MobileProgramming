package yujeong.com.justformaybe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import yujeong.com.justformaybe.core.CamDetectionReceiver;

public class MainActivity extends AppCompatActivity {
    private WifiManager wifiManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startDetection(View view) {
        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        if (wifi != null && wifi.isWifiEnabled()){
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
                    Intent intent = new Intent(MainActivity.this, DetectActivity.class);
                    intent.putExtra("result", result);
                    startActivity(intent);
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
            if(!success) {
                Toast.makeText(this, "처음에 뜬 권한 요청을 허용해주세요. ㅠ_ㅠ", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Wifi가 활성화된 상태에서만 사용 가능합니다. ㅠ_ㅠ", Toast.LENGTH_LONG).show();
        }
    }
}
