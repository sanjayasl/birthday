package me.gevorg.birthday.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.gevorg.birthday.util.AlarmUtil;

/**
 * Broadcast receiver to handle reboots.
 *
 * @author Gevorg Harutyunyan
 */
public class BootReceiver extends BroadcastReceiver {
    /**
     * Reboot intent received.
     *
     * @param context application context.
     * @param intent intent.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmUtil.setAlarm(context);
        }
    }
}
