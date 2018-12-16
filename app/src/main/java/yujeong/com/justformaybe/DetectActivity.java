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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import yujeong.com.justformaybe.core.CamDetectionReceiver;

public class DetectActivity extends AppCompatActivity {
    private Context context;

    private RelativeLayout background;
    private ImageView mainImage;
    private TextView mainText;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);
        context = getApplicationContext();

        background = (RelativeLayout) findViewById(R.id.background);
        mainImage = (ImageView) findViewById(R.id.main_image);
        mainText = (TextView) findViewById(R.id.main_text);
        description = (TextView) findViewById(R.id.description);

        int result = getIntent().getIntExtra("result", -1);

        if(result == CamDetectionReceiver.LEVEL_IN_DANGER) {
            background.setBackgroundColor(getResources().getColor(R.color.colorInDanger));
            mainImage.setImageDrawable(getResources().getDrawable(R.drawable.danger));
            mainText.setBackgroundColor(getResources().getColor(R.color.colorInDangerText));
            description.setBackgroundColor(getResources().getColor(R.color.colorInDangerDescription));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mainImage.getLayoutParams();
            params.setMargins(0, 50, 0, 0);
            mainImage.setLayoutParams(params);
            mainText.setText("DANGER");
            description.setText("몰카로 의심되는 와이파이가 있습니다.");
        }

        if(result == CamDetectionReceiver.LEVEL_IN_WARNING) {
            background.setBackgroundColor(getResources().getColor(R.color.colorInWarning));
            mainImage.setImageDrawable(getResources().getDrawable(R.drawable.warning));
            mainText.setBackgroundColor(getResources().getColor(R.color.colorInWarningText));
            description.setBackgroundColor(getResources().getColor(R.color.colorInWarningDescription));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mainImage.getLayoutParams();
            params.setMargins(0, 20, 0, 0);
            mainImage.setLayoutParams(params);
            mainText.setText("WARNING");
            description.setText("출처가 확실하지 않은 와이파이가 있습니다.");
        }

        if(result == CamDetectionReceiver.LEVEL_SAFE) {
            background.setBackgroundColor(getResources().getColor(R.color.colorInSafe));
            mainImage.setImageDrawable(getResources().getDrawable(R.drawable.safe));
            mainText.setBackgroundColor(getResources().getColor(R.color.colorInSafeText));
            description.setBackgroundColor(getResources().getColor(R.color.colorInSafeDescription));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mainImage.getLayoutParams();
            params.setMargins(0, 20, 0, 0);
            mainImage.setLayoutParams(params);
            mainText.setText("SAFE");
            description.setText("몰카로부터 안전합니다. 그래도 혹시 모르니 주의해주세요.");
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

    public void showInfo(View view) {

    }
}
