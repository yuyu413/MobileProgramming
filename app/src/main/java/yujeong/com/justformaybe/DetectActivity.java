package yujeong.com.justformaybe;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import yujeong.com.justformaybe.core.CamDetectionReceiver;

public class DetectActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);
        context = getApplicationContext();

        int result = getIntent().getIntExtra("result", -1);

        if(result == CamDetectionReceiver.LEVEL_IN_DANGER) {
            Toast.makeText(context, "몰카 위험이 있습니다.", Toast.LENGTH_LONG).show();
        }

        if(result == CamDetectionReceiver.LEVEL_IN_WARNING) {
            Toast.makeText(context, "몰카로 의심되는 물체가 있습니다. 주의하세요", Toast.LENGTH_LONG).show();
        }

        if(result == CamDetectionReceiver.LEVEL_SAFE) {
            Toast.makeText(context, "제가 진단한 바로는 안전한 환경입니다!", Toast.LENGTH_LONG).show();
        }

        if(result == -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("몰카 탐지 실패")
                    .setMessage("몰카를 탐지하는데 실패했어요 ㅠ_ㅠ 다시 시도해주세요.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            DetectActivity.this.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}
