package me.gevorg.birthday.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import me.gevorg.birthday.receiver.AlarmReceiver;

/**
 * Used to set alarm.
 *
 * @author Gevorg Harutyunyan.
 */
public class AlarmUtil {
    // Alarm period.
    private static final long PERIOD = AlarmManager.INTERVAL_HALF_DAY;

    /**
     * Set alarm.
     *
     * @param context context.
     */
    public static void setAlarm(Context context) {
        // Get alarm manager.
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Prepare intent.
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        try {
            // Cancel current one.
            alarmMgr.cancel(alarmIntent);
        } catch (Exception ignored) {}

        // Set alarm.
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, PERIOD, PERIOD,
                alarmIntent);
    }
}
