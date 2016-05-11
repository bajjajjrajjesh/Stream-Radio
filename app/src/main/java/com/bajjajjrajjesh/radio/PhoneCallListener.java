package com.bajjajjrajjesh.radio;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by User on 2014.07.11..
 */
class PhoneCallListener extends PhoneStateListener {
    private boolean start = false;
    private boolean notRunWhenStart = true;

    public void onCallStateChanged(int state, String incomingNumber) {
        if (notRunWhenStart)
            notRunWhenStart = false;
        else {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    try {
                        if (start) {
                            MusicPlayer.playRealMediaPlayer();
                            start = false;
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}