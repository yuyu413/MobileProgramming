package yujeong.com.justformaybe.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

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
              이름이 15자 이상인 와이파이가 하나 존재
              마지막으로 발견된 시간이 최근 1분 이내인 경우
            경고(WARNING) :
              RSSI 신호가 40 이상,
              이름이 15자 이상인 와이파이가 하나 존재
              마지막으로 발견된 시간이 최근 5분 이내인 경우
            안전(CLEAN) :
              이름이 15자 이상인 와이파이가 없는 경우
        **/

        int camDetectionLevel = LEVEL_SAFE;
        List<ScanResult> results = wifiManager.getScanResults();
        for (ScanResult result : results ) {
            String ssid = result.SSID;
            int level = result.level;
            long timestamp = result.timestamp;
            long lastSeen = System.currentTimeMillis() - result.timestamp;
            lastSeen = (long) lastSeen / 1000;

            if(ssid.length() > 15 && level < -80 && lastSeen < 61) {
                camDetectionLevel = LEVEL_IN_DANGER;
            }

            if(camDetectionLevel != LEVEL_IN_DANGER && ssid.length() > 15 && level < -40 && lastSeen < 60 * 5) {
                camDetectionLevel = LEVEL_IN_WARNING;
            }
        }

        listener.whenSuccess(camDetectionLevel);
    }

    private void scanFailure() {
        listener.whenFailure("몰카를 탐지하는데 실패했습니다.");
    }
}
