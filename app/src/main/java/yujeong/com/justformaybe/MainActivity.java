package yujeong.com.justformaybe;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startDetection(View view) {
        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        if (wifi != null && wifi.isWifiEnabled()){
            Intent intent = new Intent(MainActivity.this, DetectActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Wifi가 활성화된 상태에서만 사용 가능합니다. ㅠ_ㅠ", Toast.LENGTH_LONG).show();
        }
    }
}
