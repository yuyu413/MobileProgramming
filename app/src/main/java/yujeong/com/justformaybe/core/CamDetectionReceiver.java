package yujeong.com.justformaybe.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

public class CamDetectionReceiver extends BroadcastReceiver {
    public static final int LEVEL_IN_DANGER = 1;
    public static final int LEVEL_IN_WARNING = 2;
    public static final int LEVEL_SAFE = 3;

    public interface SuccessListener {
        void whenSuccess(int result);
        void whenFailure(String whyFailed);
    }

    private SuccessListener listener;
    private WifiManager wifiManager;

    public CamDetectionReceiver(SuccessListener listener, WifiManager wifiManager){
        this.listener = listener;
        this.wifiManager = wifiManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
        if (success) {
            scanSuccess();
        } else {
            scanFailure();
        }
    }

    private void scanSuccess(){
        /*
            위험(DANGER) :
              RSSI 신호가 80 이상,
              이름이 25자 이상인 와이파이가 하나 존재
              마지막으로 발견된 시간이 최근 5분 이내인 경우
            경고(WARNING) :
              RSSI 신호가 60 이상,
              이름이 25자 이상인 와이파이가 하나 존재
              마지막으로 발견된 시간이 최근 10분 이내인 경우
            안전(CLEAN) :
              그 외의 경우
        **/

        int camDetectionLevel = LEVEL_SAFE;
        List<ScanResult> results = wifiManager.getScanResults();
        for (ScanResult result : results ) {
            String ssid = result.SSID;
            int level = result.level;
            long bootTime = android.os.SystemClock.elapsedRealtime();
            long lastSeen = bootTime - (result.timestamp / 1000);

            if(ssid.length() > 25 && level < -80 && lastSeen < 60 * 5) {
                camDetectionLevel = LEVEL_IN_DANGER;
            }

            if(camDetectionLevel != LEVEL_IN_DANGER && ssid.length() > 25 && level < -60 && lastSeen < 60 * 10) {
                camDetectionLevel = LEVEL_IN_WARNING;
            }
        }

        listener.whenSuccess(camDetectionLevel);
    }

    private void scanFailure() {
        listener.whenFailure("몰카를 탐지하는데 실패했습니다.");
    }
}
